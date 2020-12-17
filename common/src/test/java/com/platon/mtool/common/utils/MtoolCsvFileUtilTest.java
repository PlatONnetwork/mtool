package com.platon.mtool.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.entity.CsvDelegateReward;
import com.platon.mtool.common.entity.CsvRewardSummary;
import com.platon.mtool.common.entity.RewardConfigTotal;
import com.platon.mtool.common.enums.FuncTypeEnum;
import com.platon.mtool.common.enums.MtoolCsvEnum;
import com.platon.mtool.common.enums.RewardProcess;
import com.platon.mtool.common.resolver.StakingParamResolver;
import com.platon.mtool.common.web3j.TransactionEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class MtoolCsvFileUtilTest {

  private static Path resourceDirectory = Paths.get("src", "test", "resources");

  private Pair<CsvRewardSummary, List<CsvDelegateReward>> mockData() throws IOException {
    CsvRewardSummary summary = new CsvRewardSummary();
    // language=JSON
    RewardConfigTotal rewardConfig =
        JSON.parseObject(
            Files.newInputStream(resourceDirectory.resolve("reward_config.json")),
            RewardConfigTotal.class);
    summary.setRewardConfig(rewardConfig);
    summary.setStartBlockNumber(BigInteger.valueOf(1441));
    summary.setEndBlockNumber(BigInteger.valueOf(1600));
    summary.setTotalBlockAmount(PlatOnUnit.latToVon(BigInteger.valueOf(20000)));
    summary.setTotalStakingAmount(PlatOnUnit.latToVon(BigInteger.valueOf(10000)));
    summary.setTotalTradeAmount(PlatOnUnit.latToVon(BigInteger.valueOf(30000)));
    summary.setTotalRewardAmount(PlatOnUnit.latToVon(BigInteger.valueOf(60000)));
    summary.setNodeName("liyf-test");
    summary.setBenefitAddressList(
        Collections.singletonList("0xa1548dd61010a742cd66fb86324ab3e29355864a"));
    summary.setBenefitAmountList(
        Collections.singletonList(PlatOnUnit.latToVon(BigInteger.valueOf(50000))));
    summary.setTotalDivideAmount(PlatOnUnit.latToVon(BigInteger.valueOf(46000)));
    summary.setTotalTransactionFee(PlatOnUnit.latToVon(BigInteger.valueOf(10)));
    summary.setAdjust(true);
    // 0.7.3
    summary.setTotalAdjustAmount(PlatOnUnit.latToVon(BigInteger.valueOf(46000)));
    summary.setTotalTransactionFee(new BigInteger("21000000000000"));
    List<CsvDelegateReward> rewardList = new ArrayList<>();
    CsvDelegateReward delegateReward = new CsvDelegateReward();
    rewardList.add(delegateReward);
    delegateReward.setDelegateAddress("0xc1553f9deadecdbb304e4f557fca196f81ea02cd");
    delegateReward.setBlockAmount(PlatOnUnit.latToVon(BigInteger.valueOf(20000)));
    delegateReward.setStakingAmount(PlatOnUnit.latToVon(BigInteger.valueOf(10000)));
    delegateReward.setTradeAmount(PlatOnUnit.latToVon(BigInteger.valueOf(30000)));
    delegateReward.setTotalAmount(PlatOnUnit.latToVon(BigInteger.valueOf(60000)));
    delegateReward.setClosedAmount(PlatOnUnit.latToVon(BigInteger.valueOf(6000)));
    delegateReward.setDivideAmount(PlatOnUnit.latToVon(BigInteger.valueOf(54000)));
    delegateReward.setTransactionHash(
        "0x4e5e2f63c8c3d63424749fccfafa4f08e73ce7cdfb1f2fdad5da2147357a72d9");
    delegateReward.setRewardProcess(RewardProcess.DONE.name());
    // 0.7.3
    delegateReward.setAdjustAmount(PlatOnUnit.latToVon(BigInteger.valueOf(54000)));
    delegateReward.setTransactionFee(new BigInteger("21000000000000"));
    delegateReward.setTransferAmount(
        delegateReward.getAdjustAmount().subtract(delegateReward.getTransactionFee()));
    return Pair.of(summary, rewardList);
  }

  @Test
  void toRewardFileBytes() throws IOException {
    Pair<CsvRewardSummary, List<CsvDelegateReward>> mockData = mockData();
    byte[] bytes =
        MtoolCsvFileUtil.toRewardFileBytes(
            mockData.getLeft(), mockData.getRight(), MtoolCsvEnum.DELEGATE_REWARD);
    System.out.println(new String(bytes));
    String expect =
        new BufferedReader(
                new InputStreamReader(
                    Objects.requireNonNull(
                        ClassLoader.getSystemResourceAsStream(
                            "csvfile/delegate_reward_expect.csv"))))
            .lines()
            .collect(Collectors.joining())
            .trim();
    assertEquals(expect, new String(bytes).replaceAll("\\R", "").trim());
  }

  @Test
  void toRewardSummaryFileBytes() throws IOException {
    Pair<CsvRewardSummary, List<CsvDelegateReward>> mockData = mockData();
    byte[] bytes =
        MtoolCsvFileUtil.toRewardFileBytes(
            mockData.getLeft(), mockData.getRight(), MtoolCsvEnum.DELEGATE_REWARD_SUMMARY);
    System.out.println(new String(bytes));
    String expect =
        new BufferedReader(
                new InputStreamReader(
                    Objects.requireNonNull(
                        ClassLoader.getSystemResourceAsStream(
                            "csvfile/delegate_reward_summary_expect.csv"))))
            .lines()
            .collect(Collectors.joining())
            .trim();
    assertEquals(expect, new String(bytes).replaceAll("\\R", "").trim());
  }

  @Test
  void toRewardResultFileBytes() throws IOException {
    Pair<CsvRewardSummary, List<CsvDelegateReward>> mockData = mockData();
    byte[] bytes =
        MtoolCsvFileUtil.toRewardFileBytes(
            mockData.getLeft(), mockData.getRight(), MtoolCsvEnum.DELEGATE_REWARD_RESULT);
    System.out.println(new String(bytes));
    String expect =
        new BufferedReader(
                new InputStreamReader(
                    Objects.requireNonNull(
                        ClassLoader.getSystemResourceAsStream(
                            "csvfile/delegate_reward_result_expect.csv"))))
            .lines()
            .collect(Collectors.joining())
            .trim();
    assertEquals(expect, new String(bytes).replaceAll("\\R", "").trim());
  }

  @Test
  void readRewardFromFile() throws IOException, URISyntaxException {
    String filepath =
        Paths.get(ClassLoader.getSystemResource("csvfile/delegate_reward_expect.csv").toURI())
            .toAbsolutePath()
            .toString();
    Pair<CsvRewardSummary, List<CsvDelegateReward>> pair =
        MtoolCsvFileUtil.readRewardFromFile(filepath, MtoolCsvEnum.DELEGATE_REWARD);
    CsvRewardSummary summary = pair.getLeft();
    List<CsvDelegateReward> rewardList = pair.getRight();
    assertEquals(BigInteger.valueOf(1441), summary.getStartBlockNumber());
    assertEquals(BigInteger.valueOf(1600), summary.getEndBlockNumber());
    assertEquals((PlatOnUnit.latToVon(BigInteger.valueOf(46000))), summary.getTotalDivideAmount());
    assertEquals(
        "0xa1548dd61010a742cd66fb86324ab3e29355864a", summary.getBenefitAddressList().get(0));
    assertEquals("50000000000000000000000", summary.getBenefitAmountList().get(0).toString());

    assertEquals(
        (PlatOnUnit.latToVon(BigInteger.valueOf(20000))), rewardList.get(0).getBlockAmount());
    assertEquals(
        (PlatOnUnit.latToVon(BigInteger.valueOf(30000))), rewardList.get(0).getTradeAmount());
    assertEquals(
        (PlatOnUnit.latToVon(BigInteger.valueOf(10000))), rewardList.get(0).getStakingAmount());
    assertEquals(
        (PlatOnUnit.latToVon(BigInteger.valueOf(60000))), rewardList.get(0).getTotalAmount());

    RewardConfigTotal rewardConfig = summary.getRewardConfig();
    RewardConfigTotal expectRewardConfig =
        JSON.parseObject(
            Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("reward_config.json")),
            RewardConfigTotal.class);
    assertTrue(
        EqualsBuilder.reflectionEquals(expectRewardConfig.getStaking(), rewardConfig.getStaking()));
    assertTrue(
        EqualsBuilder.reflectionEquals(expectRewardConfig.getBlock(), rewardConfig.getBlock()));
    assertTrue(
        EqualsBuilder.reflectionEquals(expectRewardConfig.getTrade(), rewardConfig.getTrade()));
    assertTrue(
        EqualsBuilder.reflectionEquals(
            expectRewardConfig.getFeePayer(), rewardConfig.getFeePayer()));
  }

  @Test
  void readRewardSummaryFromFile() throws IOException, URISyntaxException {
    String filepath =
        Paths.get(
                ClassLoader.getSystemResource("csvfile/delegate_reward_summary_expect.csv").toURI())
            .toAbsolutePath()
            .toString();
    Pair<CsvRewardSummary, List<CsvDelegateReward>> pair =
        MtoolCsvFileUtil.readRewardFromFile(filepath, MtoolCsvEnum.DELEGATE_REWARD_SUMMARY);
    CsvRewardSummary summary = pair.getLeft();
    List<CsvDelegateReward> rewardList = pair.getRight();
    assertEquals(BigInteger.valueOf(1441), summary.getStartBlockNumber());
    assertEquals(BigInteger.valueOf(1600), summary.getEndBlockNumber());
    assertEquals(
        "0xa1548dd61010a742cd66fb86324ab3e29355864a", summary.getBenefitAddressList().get(0));
    assertEquals("50000000000000000000000", summary.getBenefitAmountList().get(0).toString());
    assertEquals(new BigInteger("46000000000000000000000"), summary.getTotalAdjustAmount());
    assertTrue(summary.getAdjust());

    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(20000)), rewardList.get(0).getBlockAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(30000)), rewardList.get(0).getTradeAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(10000)), rewardList.get(0).getStakingAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(6000)), rewardList.get(0).getClosedAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(54000)), rewardList.get(0).getDivideAmount());
    assertEquals(
        (PlatOnUnit.latToVon(BigInteger.valueOf(54000))), rewardList.get(0).getAdjustAmount());
    RewardConfigTotal rewardConfig = summary.getRewardConfig();
    RewardConfigTotal expectRewardConfig =
        JSON.parseObject(
            Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("reward_config.json")),
            RewardConfigTotal.class);
    assertTrue(
        EqualsBuilder.reflectionEquals(expectRewardConfig.getStaking(), rewardConfig.getStaking()));
    assertTrue(
        EqualsBuilder.reflectionEquals(expectRewardConfig.getBlock(), rewardConfig.getBlock()));
    assertTrue(
        EqualsBuilder.reflectionEquals(expectRewardConfig.getTrade(), rewardConfig.getTrade()));
    assertTrue(
        EqualsBuilder.reflectionEquals(
            expectRewardConfig.getFeePayer(), rewardConfig.getFeePayer()));
  }

  @Test
  void readRewardResultFromFile() throws IOException, URISyntaxException {
    String filepath =
        Paths.get(
                ClassLoader.getSystemResource("csvfile/delegate_reward_result_expect.csv").toURI())
            .toAbsolutePath()
            .toString();
    Pair<CsvRewardSummary, List<CsvDelegateReward>> pair =
        MtoolCsvFileUtil.readRewardFromFile(filepath, MtoolCsvEnum.DELEGATE_REWARD_RESULT);
    CsvRewardSummary summary = pair.getLeft();
    List<CsvDelegateReward> rewardList = pair.getRight();
    assertEquals(BigInteger.valueOf(1441), summary.getStartBlockNumber());
    assertEquals(BigInteger.valueOf(1600), summary.getEndBlockNumber());
    assertEquals("liyf-test", summary.getNodeName());
    assertEquals(PlatOnUnit.latToVon(BigInteger.valueOf(60000)), summary.getTotalRewardAmount());
    assertEquals(PlatOnUnit.latToVon(BigInteger.valueOf(10000)), summary.getTotalStakingAmount());
    assertEquals(PlatOnUnit.latToVon(BigInteger.valueOf(30000)), summary.getTotalTradeAmount());
    assertEquals(PlatOnUnit.latToVon(BigInteger.valueOf(20000)), summary.getTotalBlockAmount());
    assertTrue(summary.getAdjust());
    assertEquals(
        "0xa1548dd61010a742cd66fb86324ab3e29355864a", summary.getBenefitAddressList().get(0));
    assertEquals("50000000000000000000000", summary.getBenefitAmountList().get(0).toString());

    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(20000)), rewardList.get(0).getBlockAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(30000)), rewardList.get(0).getTradeAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(10000)), rewardList.get(0).getStakingAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(6000)), rewardList.get(0).getClosedAmount());
    assertEquals(
        PlatOnUnit.latToVon(BigInteger.valueOf(54000)), rewardList.get(0).getDivideAmount());
    assertEquals(new BigInteger("53999999979000000000000"), rewardList.get(0).getTransferAmount());

    assertEquals("DONE", rewardList.get(0).getRewardProcess());
    assertEquals(
        "0x4e5e2f63c8c3d63424749fccfafa4f08e73ce7cdfb1f2fdad5da2147357a72d9",
        rewardList.get(0).getTransactionHash());
  }

  @Test
  void getTemplateList() throws IOException {
    List<String> list =
        MtoolCsvFileUtil.getTemplateList(MtoolCsvEnum.DELEGATE_REWARD.getFilename());
    assertEquals(11, list.size());
    list = MtoolCsvFileUtil.getTemplateList(MtoolCsvEnum.DELEGATE_REWARD_RESULT.getFilename());
    assertEquals(13, list.size());
    list = MtoolCsvFileUtil.getTemplateList(MtoolCsvEnum.DELEGATE_REWARD_SUMMARY.getFilename());
    assertEquals(12, list.size());
    list = MtoolCsvFileUtil.getTemplateList(MtoolCsvEnum.TRANSACTION_DETAIL.getFilename());
    assertEquals(1, list.size());
  }

  @Test
  void toTransactionDetailBytes() throws IOException {
//    TransactionEntity entity =
//        JSON.parseObject(
//            Files.newInputStream(resourceDirectory.resolve("transaction_detail.json")),
//            TransactionEntity.class);
//    StakingParamResolver resolver = new StakingParamResolver();
//    StakingParamResolver.StakingParam param = resolver.resolv(entity.getData());
//    entity.setType(FuncTypeEnum.getFromCode(param.getFuncType()));
//    entity.setAccountType(param.getStakingAmountType().name());
//    entity.setAmount(param.getAmount());
//    entity.setHash("hash");
//
//    byte[] bytes = MtoolCsvFileUtil.toTransactionDetailBytes(Collections.singletonList(entity));
//    System.out.println(new String(bytes));
//    String expect =
//        new BufferedReader(
//                new InputStreamReader(
//                    Objects.requireNonNull(
//                        ClassLoader.getSystemResourceAsStream(
//                            "csvfile/transaction_detail_expect.csv"))))
//            .lines()
//            .collect(Collectors.joining())
//            .trim();
//    assertEquals(expect, new String(bytes).replaceAll("\\R", "").trim());
  }

  @Test
  void readTransactionDetailFromFile() throws IOException, URISyntaxException {
    String filepath =
        Paths.get(ClassLoader.getSystemResource("csvfile/transaction_detail_expect.csv").toURI())
            .toAbsolutePath()
            .toString();
    List<TransactionEntity> transactionEntityList =
        MtoolCsvFileUtil.readTransactionDetailFromFile(filepath);
    assertEquals(1, transactionEntityList.size());
    TransactionEntity entity = transactionEntityList.get(0);
    assertEquals("atx1592gm4sszzn59ntxlwrryj4nu2f4tpj2jyfnyc", entity.getFrom());
    assertEquals("atx1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzrzv4mm", entity.getTo());
    assertEquals(LocalDateTime.parse("2020-05-12T10:42:14.125"), entity.getCreateTime());
    assertEquals(103L, entity.getChainId());
    assertEquals(FuncTypeEnum.STAKING, entity.getType());
    assertEquals(PlatOnUnit.latToVon(BigInteger.valueOf(1000000)), entity.getAmount());
    assertEquals("9547f37c486f1ab6b6ff6fb096ec10fc", entity.getHash());
  }
}
