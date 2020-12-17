package com.platon.mtool.common.utils;

import com.alaya.bech32.Bech32;
import com.alaya.parameters.NetworkParameters;
import com.platon.mtool.common.entity.CsvDelegateReward;
import com.platon.mtool.common.entity.CsvRewardSummary;
import com.platon.mtool.common.entity.RewardConfigDetail;
import com.platon.mtool.common.entity.RewardConfigTotal;
import com.platon.mtool.common.enums.*;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.web3j.TransactionEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.crypto.Address;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * csv文件生成与解析工具
 *
 * <p>Created by liyf.
 */
public class MtoolCsvFileUtil {

  // 内置合约地址
  private static final List<String> INNER_ADDRESS = Arrays.asList(
          "0x1000000000000000000000000000000000000001",
          "0x1000000000000000000000000000000000000002",
          "0x1000000000000000000000000000000000000003",
          "0x1000000000000000000000000000000000000004",
          "0x1000000000000000000000000000000000000005"
  );
  private static final Set<String> CONTRACT_ADDRESS_SET = new HashSet<>();
  private static final List<Address> CONTRACTS_ADDRESS = new ArrayList<>();

  static {
    INNER_ADDRESS.forEach(address->{
      CONTRACTS_ADDRESS.add(new Address(
              Bech32.addressEncode(NetworkParameters.MainNetParams.getHrp(),address),
              Bech32.addressEncode(NetworkParameters.TestNetParams.getHrp(),address)
      ));
    });
    CONTRACTS_ADDRESS.forEach(address->{
      CONTRACT_ADDRESS_SET.add(address.getMainnet());
      CONTRACT_ADDRESS_SET.add(address.getTestnet());
    });
  }

  private static final Logger logger = LoggerFactory.getLogger(MtoolCsvFileUtil.class);

  private static final int DECIMAL_SCALE = 12;

  private MtoolCsvFileUtil() {}

  /**
   * 生成奖励文件字节.
   *
   * @param summary 奖励汇总
   * @param delegateRewards 委托人奖励明细
   * @param csvRewardEnum 奖励类型
   * @return 奖励文件字节
   * @throws IOException csv文件失败
   */
  public static byte[] toRewardFileBytes(
      CsvRewardSummary summary, List<CsvDelegateReward> delegateRewards, MtoolCsvEnum csvRewardEnum)
      throws IOException {
    LogUtils.info(logger, () -> Log.newBuilder().kv("summary", summary));
    List<String> template = getTemplateList(csvRewardEnum.getFilename());
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
        CSVPrinter csvPrinter =
            new CSVPrinter(
                writer,
                CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withSkipHeaderRecord()
                    .withHeader(CsvHeaderReward.class))) {
      // 写文件头
      csvPrinter.printRecord(
          String.format(
              template.get(0), summary.getStartBlockNumber(), summary.getEndBlockNumber()));
      DecimalFormat decimalFormat = new DecimalFormat("#.00%");
      csvPrinter.printRecord(
          String.format(
              template.get(1), // Plan
              decimalFormat.format(summary.getRewardConfig().getBlock().getCommissionRatio()),
              summary.getRewardConfig().getBlock().getRewardMethod(),
              decimalFormat.format(summary.getRewardConfig().getTrade().getCommissionRatio()),
              summary.getRewardConfig().getTrade().getRewardMethod(),
              decimalFormat.format(summary.getRewardConfig().getStaking().getCommissionRatio()),
              summary.getRewardConfig().getStaking().getRewardMethod(),
              summary.getRewardConfig().getFeePayer().name()));
      csvPrinter.printRecord(String.format(template.get(2), summary.getNodeName())); // Validator
      csvPrinter.printRecord(
          String.format(
              template.get(3), // Reward address
              toRewardAddress(summary.getBenefitAddressList(), summary.getBenefitAmountList())));
      csvPrinter.printRecord(
          String.format(
              template.get(4), // Total reward
              formatAmount(summary.getTotalRewardAmount())));
      csvPrinter.printRecord(
          String.format(
              template.get(5), // Total block reward
              formatAmount(summary.getTotalBlockAmount())));
      csvPrinter.printRecord(
          String.format(
              template.get(6), // Total fee reward
              formatAmount(summary.getTotalTradeAmount())));
      csvPrinter.printRecord(
          String.format(
              template.get(7), // Total staking reward
              formatAmount(summary.getTotalStakingAmount())));
      csvPrinter.printRecord(String.format(template.get(8), delegateRewards.size())); // Delegators
      csvPrinter.printRecord(
          String.format(
              template.get(9), // Total distribution
              formatAmount(summary.getTotalDivideAmount())));

      boolean isAdjust = summary.getAdjust();
      if (csvRewardEnum == MtoolCsvEnum.DELEGATE_REWARD) {
        // Delegator,Block reward,Fee reward,Staking reward,Total reward
        // 写列表头
        csvPrinter.printRecord((Object[]) template.get(10).split(",")); // title
        for (CsvDelegateReward delegateReward : delegateRewards) {
          csvPrinter.printRecord(
              delegateReward.getDelegateAddress(),
              formatAmount(delegateReward.getBlockAmount()),
              formatAmount(delegateReward.getTradeAmount()),
              formatAmount(delegateReward.getStakingAmount()),
              formatAmount(delegateReward.getTotalAmount()));
        }
      } else if (csvRewardEnum == MtoolCsvEnum.DELEGATE_REWARD_SUMMARY) {
        // Delegator,Block reward,Fee reward,Staking reward,Issued reward,Actual reward
        csvPrinter.printRecord(
            isAdjust
                ? String.format(template.get(10), formatAmount(summary.getTotalAdjustAmount()))
                : ""); // Adjusted Total Distribution
        // 写列表头
        csvPrinter.printRecord((Object[]) template.get(11).split(",")); // title
        for (CsvDelegateReward delegateReward : delegateRewards) {
          csvPrinter.printRecord(
              delegateReward.getDelegateAddress(),
              formatAmount(delegateReward.getBlockAmount()),
              formatAmount(delegateReward.getTradeAmount()),
              formatAmount(delegateReward.getStakingAmount()),
              formatAmount(delegateReward.getClosedAmount()),
              formatAmount(delegateReward.getDivideAmount()),
              formatAmount(delegateReward.getAdjustAmount()));
        }
      } else if (csvRewardEnum == MtoolCsvEnum.DELEGATE_REWARD_RESULT) {
        csvPrinter.printRecord(
            isAdjust
                ? String.format(template.get(10), formatAmount(summary.getTotalAdjustAmount()))
                : ""); // Adjusted Total Distribution
        csvPrinter.printRecord(
            String.format(
                template.get(11), // Total txn fee
                formatAmount(summary.getTotalTransactionFee())));
        // 写列表头
        csvPrinter.printRecord((Object[]) template.get(12).split(",")); // title

        for (CsvDelegateReward delegateReward : delegateRewards) {
          csvPrinter.printRecord(
              delegateReward.getDelegateAddress(),
              formatAmount(delegateReward.getBlockAmount()),
              formatAmount(delegateReward.getTradeAmount()),
              formatAmount(delegateReward.getStakingAmount()),
              formatAmount(delegateReward.getClosedAmount()),
              formatAmount(delegateReward.getDivideAmount()),
              formatAmount(delegateReward.getAdjustAmount()),
              formatAmount(delegateReward.getTransactionFee()),
              formatAmount(delegateReward.getTransferAmount()),
              delegateReward.getTransactionHash(),
              RewardProcess.getFromName(delegateReward.getRewardProcess()).getTitle());
        }
      }
      csvPrinter.flush();
      return baos.toByteArray();
    }
  }

  /**
   * 读取奖励文件数据.
   *
   * @param filepath 奖励文件地址
   * @param csvRewardEnum 奖励文件类型
   * @return 奖励汇总和委托人奖励明细
   * @throws IOException io失败
   */
  public static Pair<CsvRewardSummary, List<CsvDelegateReward>> readRewardFromFile(
      String filepath, MtoolCsvEnum csvRewardEnum) throws IOException {
    List<String> template = getTemplateList(csvRewardEnum.getFilename());
    Reader in = new FileReader(filepath);
    CSVParser csvParser =
        CSVFormat.RFC4180.withDelimiter(',').withHeader(csvRewardEnum.getHeaderClass()).parse(in);
    List<CSVRecord> csvRecords = csvParser.getRecords();
    CsvRewardSummary summary = new CsvRewardSummary();
    List<CsvDelegateReward> rewardList = new ArrayList<>();
    // 查找区块区间
    Pattern contractPattern =
        Pattern.compile(String.format(template.get(0), "(\\d*)", "(\\d*)")); // Period
    Matcher matcher = contractPattern.matcher(csvRecords.get(0).get(0));
    if (matcher.find()) {
      summary.setStartBlockNumber(new BigInteger(matcher.group(1)));
      summary.setEndBlockNumber(new BigInteger(matcher.group(2)));
    }
    // 收益人地址与收益人账户余额
    Pair<List<String> /*benefitAddressList*/, List<BigInteger> /*benefitAmountList*/> benefitPair =
        fromRewardAddress(csvRecords.get(3).get(0), template.get(3));
    summary.setBenefitAddressList(benefitPair.getLeft());
    summary.setBenefitAmountList(benefitPair.getRight());

    // Plan
    String percentRegexStr = "([0-9\\.]*)%";
    String methodRegexStr = "(AVERAGE|PERCENT)";
    Pattern planPattern =
        Pattern.compile(
            String.format(
                escapeRegex(template.get(1)),
                percentRegexStr,
                methodRegexStr,
                percentRegexStr,
                methodRegexStr,
                percentRegexStr,
                methodRegexStr,
                "(DELEGATOR|VALIDATOR)"));
    Matcher planMatcher = planPattern.matcher(csvRecords.get(1).get(0));
    if (planMatcher.find()) {
      RewardConfigTotal rewardConfig = new RewardConfigTotal();
      RewardConfigDetail stakingConfig = new RewardConfigDetail();
      RewardConfigDetail tradeConfig = new RewardConfigDetail();
      RewardConfigDetail blockConfig = new RewardConfigDetail();
      rewardConfig.setStaking(stakingConfig);
      rewardConfig.setTrade(tradeConfig);
      rewardConfig.setBlock(blockConfig);
      summary.setRewardConfig(rewardConfig);
      blockConfig.setCommissionRatio(Float.parseFloat(planMatcher.group(1)) / 100);
      blockConfig.setRewardMethod(RewardMethod.valueOf(planMatcher.group(2)));
      tradeConfig.setCommissionRatio(Float.parseFloat(planMatcher.group(3)) / 100);
      tradeConfig.setRewardMethod(RewardMethod.valueOf(planMatcher.group(4)));
      stakingConfig.setCommissionRatio(Float.parseFloat(planMatcher.group(5)) / 100);
      stakingConfig.setRewardMethod(RewardMethod.valueOf(planMatcher.group(6)));
      rewardConfig.setFeePayer(FeePayerEnum.valueOf(planMatcher.group(7)));
    }
    // Validator
    Pattern validatorPattern = Pattern.compile(String.format(template.get(2), "(.*)"));
    Matcher validatorMatcher = validatorPattern.matcher(csvRecords.get(2).get(0));
    if (validatorMatcher.find()) {
      summary.setNodeName(validatorMatcher.group(1));
    }

    String amountRegexStr = "([0-9\\.]*)";

    // Total reward
    Pattern totalRewardPattern = Pattern.compile(String.format(template.get(4), amountRegexStr));
    Matcher totalRewardMatcher = totalRewardPattern.matcher(csvRecords.get(4).get(0));
    if (totalRewardMatcher.find()) {
      summary.setTotalRewardAmount(
          PlatOnUnit.latToVon(new BigDecimal(totalRewardMatcher.group(1))));
    }
    // Total block reward
    Pattern totalBlockRewardPattern =
        Pattern.compile(String.format(template.get(5), amountRegexStr));
    Matcher totalBlockRewardMatcher = totalBlockRewardPattern.matcher(csvRecords.get(5).get(0));
    if (totalBlockRewardMatcher.find()) {
      summary.setTotalBlockAmount(
          PlatOnUnit.latToVon(new BigDecimal(totalBlockRewardMatcher.group(1))));
    }
    // Total fee reward
    Pattern totalTradeRewardPattern =
        Pattern.compile(String.format(template.get(6), amountRegexStr));
    Matcher totalTradeRewardMatcher = totalTradeRewardPattern.matcher(csvRecords.get(6).get(0));
    if (totalTradeRewardMatcher.find()) {
      summary.setTotalTradeAmount(
          PlatOnUnit.latToVon(new BigDecimal(totalTradeRewardMatcher.group(1))));
    }
    // Total staking reward
    Pattern totalStakingRewardPattern =
        Pattern.compile(String.format(template.get(7), amountRegexStr));
    Matcher totalStakingRewardMatcher = totalStakingRewardPattern.matcher(csvRecords.get(7).get(0));
    if (totalStakingRewardMatcher.find()) {
      summary.setTotalStakingAmount(
          PlatOnUnit.latToVon(new BigDecimal(totalStakingRewardMatcher.group(1))));
    }
    // Total divide amount
    Pattern totalDivideAmountPattern =
        Pattern.compile(String.format(template.get(9), amountRegexStr));
    Matcher totalDivideAmountMatcher = totalDivideAmountPattern.matcher(csvRecords.get(9).get(0));
    if (totalDivideAmountMatcher.find()) {
      summary.setTotalDivideAmount(
          PlatOnUnit.latToVon(new BigDecimal(totalDivideAmountMatcher.group(1))));
    }

    if (csvRewardEnum == MtoolCsvEnum.DELEGATE_REWARD) {
      for (int i = csvRewardEnum.getSummaryLines() /*文件头不处理*/; i < csvRecords.size(); i++) {
        CSVRecord record = csvRecords.get(i);
        String address = record.get(CsvHeaderReward.ADDRESS);
        String blockAmount = record.get(CsvHeaderReward.BLOCK_AMOUNT);
        String tradeAmount = record.get(CsvHeaderReward.TRADE_AMOUNT);
        String stakingAmount = record.get(CsvHeaderReward.STAKING_AMOUNT);
        String totalAmount = record.get(CsvHeaderReward.TOTAL_AMOUNT);
        CsvDelegateReward delegateReward = new CsvDelegateReward();
        delegateReward.setDelegateAddress(address);
        delegateReward.setBlockAmount(PlatOnUnit.latToVon(new BigDecimal(blockAmount)));
        delegateReward.setStakingAmount(PlatOnUnit.latToVon(new BigDecimal(stakingAmount)));
        delegateReward.setTradeAmount(PlatOnUnit.latToVon(new BigDecimal(tradeAmount)));
        delegateReward.setTotalAmount(PlatOnUnit.latToVon(new BigDecimal(totalAmount)));

        rewardList.add(delegateReward);
      }
    } else if (csvRewardEnum == MtoolCsvEnum.DELEGATE_REWARD_SUMMARY) {
      // totalAdjust
      Pattern totalAdjustAmountPattern =
          Pattern.compile(String.format(escapeRegex(template.get(10)), amountRegexStr));
      Matcher totalAdjustAmountMatcher =
          totalAdjustAmountPattern.matcher(csvRecords.get(10).get(0));
      if (totalAdjustAmountMatcher.find()) {
        summary.setTotalAdjustAmount(
            PlatOnUnit.latToVon(new BigDecimal(totalAdjustAmountMatcher.group(1))));
        summary.setAdjust(true);
      }

      for (int i = csvRewardEnum.getSummaryLines() /*文件头不处理*/; i < csvRecords.size(); i++) {
        CSVRecord record = csvRecords.get(i);
        String address = record.get(CsvHeaderRewardSummary.ADDRESS);
        String blockAmount = record.get(CsvHeaderRewardSummary.BLOCK_AMOUNT);
        String tradeAmount = record.get(CsvHeaderRewardSummary.TRADE_AMOUNT);
        String stakingAmount = record.get(CsvHeaderRewardSummary.STAKING_AMOUNT);
        String closedAmount = record.get(CsvHeaderRewardSummary.CLOSED_AMOUNT);
        String divideAmount = record.get(CsvHeaderRewardSummary.DIVIDE_AMOUNT);
        CsvDelegateReward delegateReward = new CsvDelegateReward();
        delegateReward.setDelegateAddress(address);
        delegateReward.setBlockAmount(PlatOnUnit.latToVon(new BigDecimal(blockAmount)));
        delegateReward.setStakingAmount(PlatOnUnit.latToVon(new BigDecimal(stakingAmount)));
        delegateReward.setTradeAmount(PlatOnUnit.latToVon(new BigDecimal(tradeAmount)));
        delegateReward.setClosedAmount(PlatOnUnit.latToVon(new BigDecimal(closedAmount)));
        delegateReward.setDivideAmount(PlatOnUnit.latToVon(new BigDecimal(divideAmount)));
        delegateReward.setAdjustAmount(
            PlatOnUnit.latToVon(new BigDecimal(record.get(CsvHeaderRewardSummary.ADJUST_AMOUNT))));
        rewardList.add(delegateReward);
      }
    } else if (csvRewardEnum == MtoolCsvEnum.DELEGATE_REWARD_RESULT) {
      summary.setAdjust(!StringUtils.isEmpty(csvRecords.get(10).get(0)));
      for (int i = csvRewardEnum.getSummaryLines() /*文件头不处理*/; i < csvRecords.size(); i++) {
        CSVRecord record = csvRecords.get(i);
        String address = record.get(CsvHeaderRewardResult.ADDRESS);
        String blockAmount = record.get(CsvHeaderRewardResult.BLOCK_AMOUNT);
        String tradeAmount = record.get(CsvHeaderRewardResult.TRADE_AMOUNT);
        String stakingAmount = record.get(CsvHeaderRewardResult.STAKING_AMOUNT);
        String closedAmount = record.get(CsvHeaderRewardResult.CLOSED_AMOUNT);
        String divideAmount = record.get(CsvHeaderRewardResult.DIVIDE_AMOUNT);
        String transactionHash = record.get(CsvHeaderRewardResult.TRANSACTION_HASH);
        String rewardProcess = record.get(CsvHeaderRewardResult.REWARD_PROCESS);
        CsvDelegateReward delegateReward = new CsvDelegateReward();
        delegateReward.setDelegateAddress(address);
        delegateReward.setBlockAmount(PlatOnUnit.latToVon(new BigDecimal(blockAmount)));
        delegateReward.setStakingAmount(PlatOnUnit.latToVon(new BigDecimal(stakingAmount)));
        delegateReward.setTradeAmount(PlatOnUnit.latToVon(new BigDecimal(tradeAmount)));
        delegateReward.setClosedAmount(PlatOnUnit.latToVon(new BigDecimal(closedAmount)));
        delegateReward.setDivideAmount(PlatOnUnit.latToVon(new BigDecimal(divideAmount)));
        delegateReward.setTransactionHash(transactionHash);
        delegateReward.setAdjustAmount(
            PlatOnUnit.latToVon(new BigDecimal(record.get(CsvHeaderRewardResult.ADJUST_AMOUNT))));
        delegateReward.setTransactionFee(
            PlatOnUnit.latToVon(new BigDecimal(record.get(CsvHeaderRewardResult.TRANSACTION_FEE))));
        delegateReward.setTransferAmount(
            PlatOnUnit.latToVon(new BigDecimal(record.get(CsvHeaderRewardResult.TRANSFER_AMOUNT))));
        delegateReward.setRewardProcess(RewardProcess.getFromTitle(rewardProcess).name());
        rewardList.add(delegateReward);
      }
    }

    return ImmutablePair.of(summary, rewardList);
  }

  /**
   * 获取模板列表.
   *
   * @param filename 文件路径
   * @return 模板文件每行字符串
   * @throws IOException 文件读取错误
   */
  public static List<String> getTemplateList(String filename) throws IOException {
    try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename)) {
      BufferedReader br =
          new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
      return br.lines().collect(Collectors.toList());
    }
  }

  private static String formatAmount(BigInteger bigInteger) {
    return PlatOnUnit.vonToLat(bigInteger)
        .setScale(DECIMAL_SCALE, RoundingMode.DOWN)
        .stripTrailingZeros()
        .toPlainString();
  }

  /**
   * 交易明细写入csv文件流.
   *
   * @param transactionEntityList 交易明细
   * @return csv文件流
   * @throws IOException io错误
   */
  public static byte[] toTransactionDetailBytes(List<TransactionEntity> transactionEntityList)
      throws IOException {
    LogUtils.info(
        logger,
        () ->
            Log.newBuilder()
                .msg("toTransactionDetailBytes")
                .kv("size", transactionEntityList.size()));
    List<String> template = getTemplateList(MtoolCsvEnum.TRANSACTION_DETAIL.getFilename());
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
        CSVPrinter csvPrinter =
            new CSVPrinter(
                writer,
                CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withSkipHeaderRecord()
                    .withHeader(CsvHeaderTransactionDetail.class))) {
      // 写列表头
      csvPrinter.printRecord((Object[]) template.get(0).split(",")); // title

      for (TransactionEntity transactionEntity : transactionEntityList) {
        // Type,From,To,Account Type,Amount,Gas Price,Gas Limit,Fee,Create Time,Additional Info,
        // TX data,Signed Time,Signed Data
        csvPrinter.printRecord(
            transactionEntity.getType().getCommandName(),
            transactionEntity.getFrom(),
            transactionEntity.getTo(),
            transactionEntity.getAccountType(),
            transactionEntity.getAmount(),
            transactionEntity.getGasPrice(),
            transactionEntity.getGasLimit(),
            transactionEntity.getFee(),
            transactionEntity.getNonce(),
            transactionEntity.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            transactionEntity.getChainId(),
            transactionEntity.getAdditionalInfo(),
            transactionEntity.getHash(),
            transactionEntity.getData(),
            transactionEntity.getSignTime(),
            transactionEntity.getSignData());
      }

      csvPrinter.flush();
      return baos.toByteArray();
    }
  }

  public static List<TransactionEntity> readTransactionDetailFromFile(String filepath)
      throws IOException {
    List<TransactionEntity> transactionEntityList = new ArrayList<>();
    Reader in = new FileReader(filepath);
    CSVParser csvParser =
        CSVFormat.RFC4180
            .withDelimiter(',')
            .withHeader(MtoolCsvEnum.TRANSACTION_DETAIL.getHeaderClass())
            .parse(in);
    List<CSVRecord> csvRecords = csvParser.getRecords();
    for (int i = MtoolCsvEnum.TRANSACTION_DETAIL.getSummaryLines() /*文件头不处理*/;
        i < csvRecords.size();
        i++) {
      CSVRecord record = csvRecords.get(i);
      TransactionEntity transactionEntity = new TransactionEntity();
      transactionEntity.setNonce(new BigInteger(record.get(CsvHeaderTransactionDetail.NONCE)));
      transactionEntity.setGasPrice(
          new BigInteger(record.get(CsvHeaderTransactionDetail.GAS_PRICE)));
      transactionEntity.setGasLimit(
          new BigInteger(record.get(CsvHeaderTransactionDetail.GAS_LIMIT)));
      transactionEntity.setTo(record.get(CsvHeaderTransactionDetail.TO));
      transactionEntity.setData(record.get(CsvHeaderTransactionDetail.TX_DATA));
      transactionEntity.setType(
          FuncTypeEnum.getFromCommandName(record.get(CsvHeaderTransactionDetail.TYPE)));
      transactionEntity.setFrom(record.get(CsvHeaderTransactionDetail.FROM));
      transactionEntity.setAccountType(record.get(CsvHeaderTransactionDetail.ACCOUNT_TYPE));
      transactionEntity.setAmount(new BigInteger(record.get(CsvHeaderTransactionDetail.AMOUNT)));
      transactionEntity.setFee(new BigInteger(record.get(CsvHeaderTransactionDetail.FEE)));
      transactionEntity.setChainId(Long.parseLong(record.get(CsvHeaderTransactionDetail.CHAIN_ID)));
      transactionEntity.setAdditionalInfo(record.get(CsvHeaderTransactionDetail.ADDITIONAL_INFO));
      transactionEntity.setHash(record.get(CsvHeaderTransactionDetail.HASH));
      transactionEntity.setCreateTime(
          StringUtils.isEmpty(record.get(CsvHeaderTransactionDetail.CREATE_TIME))
              ? null
              : LocalDateTime.parse(
                  record.get(CsvHeaderTransactionDetail.CREATE_TIME),
                  DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      transactionEntity.setSignTime(
          StringUtils.isEmpty(record.get(CsvHeaderTransactionDetail.SIGNED_TIME))
              ? null
              : LocalDateTime.parse(
                  record.get(CsvHeaderTransactionDetail.SIGNED_TIME),
                  DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      transactionEntity.setSignData(record.get(CsvHeaderTransactionDetail.SIGNED_DATA));
      // 内置合约value为0， 非内置合约为amount
      if (CONTRACT_ADDRESS_SET.contains(transactionEntity.getTo())) {
        transactionEntity.setValue(BigInteger.ZERO);
      } else {
        transactionEntity.setValue(transactionEntity.getAmount());
      }
      transactionEntityList.add(transactionEntity);
    }
    return transactionEntityList;
  }

  private static String toRewardAddress(
      List<String> benefitAddressList, List<BigInteger> benefitAmountList) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < benefitAddressList.size(); i++) {
      String rewardAddressFormat = "%s (Balance: %s ATP)";
      s.append(
          String.format(
              rewardAddressFormat,
              benefitAddressList.get(i),
              formatAmount(benefitAmountList.get(i))));
      if (i != benefitAddressList.size() - 1) {
        s.append(",");
      }
    }
    return s.toString();
  }

  private static Pair<List<String> /*benefitAddressList*/, List<BigInteger> /*benefitAmountList*/>
      fromRewardAddress(String rewardAddressStr, String template) {
    // Reward address: 0xa1548dd61010a742cd66fb86324ab3e29355864a (Balance:
    // 50000000000000000000000 ATP)
    Pattern pattern = Pattern.compile(String.format(template, "(.*)"));
    Matcher matcher = pattern.matcher(rewardAddressStr);
    List<String> addressList = new ArrayList<>();
    List<BigInteger> amountList = new ArrayList<>();
    if (matcher.find()) {
      String[] part = matcher.group(1).split(",");
      Pattern benefitPattern = Pattern.compile("(^0x\\w*) \\(Balance: ([0-9.]*) ATP");
      for (String s : part) {
        Matcher benefitMatcher = benefitPattern.matcher(s);
        if (benefitMatcher.find()) {
          addressList.add(benefitMatcher.group(1));
          amountList.add(PlatOnUnit.latToVon(new BigDecimal(benefitMatcher.group(2))));
        }
      }
    }
    return Pair.of(addressList, amountList);
  }

  private static String escapeRegex(String s) {
    return s.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
  }
}
