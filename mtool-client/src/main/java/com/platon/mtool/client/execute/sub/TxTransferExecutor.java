package com.platon.mtool.client.execute.sub;

import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.platon.mtool.client.execute.MtoolExecutor;
import com.platon.mtool.client.options.TxOptions.TransferOption;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.AllCommands.Tx;
import com.platon.mtool.common.entity.AdditionalInfo;
import com.platon.mtool.common.entity.QrCodeData;
import com.platon.mtool.common.entity.QrCodeData.QrCodeTransaction;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.enums.FuncTypeEnum;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.AddressUtil;
import com.platon.mtool.common.utils.GasProviderUtils;
import com.platon.mtool.common.utils.HashUtil;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.utils.MtoolCsvFileUtil;
import com.platon.mtool.common.utils.PlatOnUnit;
import com.platon.mtool.common.web3j.Keystore;
import com.platon.mtool.common.web3j.Keystore.Type;
import com.platon.mtool.common.web3j.MtoolTransactionManager;
import com.platon.mtool.common.web3j.TransactionEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.tx.RawTransactionManager;
import com.alaya.tx.TransactionManager;
import com.alaya.tx.Transfer;
import com.alaya.tx.gas.GasProvider;
import com.alaya.utils.Convert.Unit;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 转账
 *
 * <p>Created by liyf
 */
public class TxTransferExecutor extends MtoolExecutor<TransferOption> {
  private static final Logger logger = LoggerFactory.getLogger(TxTransferExecutor.class);
  private BlockChainService blockChainService = BlockChainService.singleton();

  public TxTransferExecutor(JCommander commander, TransferOption commonOption) {
    super(commander, commonOption);
  }

  protected Web3j getWeb3j(ValidatorConfig validatorConfig) {
    return com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
  }

  public Transfer getTransfer(Web3j web3j, Credentials credentials, Long chainId) {
    TransactionManager transactionManager =
        new RawTransactionManager(web3j, credentials, chainId);
    return new Transfer(web3j, transactionManager);
  }

  public Transfer getTransfer(Web3j web3j, String fromAddress, Long chainId) {
    TransactionManager transactionManager =
        new MtoolTransactionManager(web3j, fromAddress, chainId);
    return new Transfer(web3j, transactionManager);
  }

  @Override
  public void execute(TransferOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg(Tx.TRANSFER).kv("option", option));
    String toAddress = option.getTo();
    if (!WalletUtils.isValidAddress(toAddress)) {
      throw new MtoolClientException("Invalid recipient address");
    }

    if (toAddress.equals(option.getKeystore().getAddress().getTestnet())||toAddress.equals(option.getKeystore().getAddress().getMainnet())) {
      throw new MtoolClientException("Could not send to yourself!");
    }

    if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),toAddress)){
      throw new MtoolClientException("is not a legal address of chain["+CLIENT_CONFIG.getTargetChainId()+"]");
    }

    Web3j web3j = getWeb3j(option.getConfig());
    GasProvider gasProvider =
        GasProviderUtils.getGasProvider(
            option.getGasProvider(), () -> GasProviderUtils.getTransferGasProvider(web3j));

    blockChainService.validBalanceEnough(
        option.getKeystore().getAddress(),
        PlatOnUnit.latToVon(option.getLat()),
        gasProvider,
        web3j, StakingAmountType.FREE_AMOUNT_TYPE);

    if (option.getKeystore().getType().equals(Type.NORMAL)) {
      Transfer transfer =
          getTransfer(
              web3j, option.getKeystore().getCredentials(), CLIENT_CONFIG.getTargetChainId());
      TransactionReceipt receipt =
          transfer
              .sendFunds(
                  toAddress,
                  option.getLat(),
                  Unit.ATP,
                  gasProvider.getGasPrice(),
                  gasProvider.getGasLimit())
              .send();

      PrintUtils.echo("Send transaction success");
      PrintUtils.echo("Txhash: %s", receipt.getTransactionHash());
    } else if (option.getKeystore().getType().equals(Keystore.Type.OBSERVE)) {
      String targetChainAddress = AddressUtil.getTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),option.getKeystore().getAddress().getMainnet());

      Transfer transfer =
          getTransfer(web3j, targetChainAddress, CLIENT_CONFIG.getTargetChainId());
      TransactionReceipt receipt =
          transfer
              .sendFunds(
                  toAddress,
                  option.getLat(),
                  Unit.ATP,
                  gasProvider.getGasPrice(),
                  gasProvider.getGasLimit())
              .send();

      TransactionEntity entity =
          JSON.parseObject(receipt.getTransactionHash(), TransactionEntity.class);
      entity.setType(FuncTypeEnum.TRANSFER);
      entity.setAmount(PlatOnUnit.latToVon(option.getLat()));
      AdditionalInfo additionalInfo = new AdditionalInfo();
      BeanUtils.copyProperties(additionalInfo, option.getConfig());
      entity.setAdditionalInfo(JSON.toJSONString(additionalInfo));
      entity.setHash(HashUtil.hashTransaction(entity));
      byte[] bytes = MtoolCsvFileUtil.toTransactionDetailBytes(Collections.singletonList(entity));
      String filename =
          String.format(
              "transaction_detail_%s.csv",
              LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
      String filepath =
          ResourceUtils.getTransactionDetailsPath().resolve(filename).toAbsolutePath().toString();
      FileUtils.writeByteArrayToFile(new File(filepath), bytes);

      PrintUtils.echo(
          "Transfer unsigned raw: %s", getQrCodeString(Collections.singletonList(entity)));
      PrintUtils.echo("File generated on: %s", filepath);
    }
  }

  protected String getQrCodeString(List<TransactionEntity> entityList) {
    QrCodeData qrCodeData = new QrCodeData();
    List<QrCodeTransaction> qrCodeTransactionList = new ArrayList<>(entityList.size());
    for (TransactionEntity entity : entityList) {
      QrCodeTransaction transaction = new QrCodeTransaction();
      transaction.setFunctionType(
          FuncTypeEnum.TRANSFER.equals(entity.getType()) ? 0 : entity.getType().getCode());
      transaction.setAmount(entity.getAmount());
      transaction.setChainId(CLIENT_CONFIG.getTargetChainId());
      transaction.setFrom(entity.getFrom());
      transaction.setTo(entity.getTo());
      transaction.setGasPrice(entity.getGasPrice());
      transaction.setGasLimit(entity.getGasLimit());
      transaction.setNonce(entity.getNonce());
      qrCodeTransactionList.add(transaction);
    }
    qrCodeData.setQrCodeTransactionList(qrCodeTransactionList);
    qrCodeData.setTimestamp(System.currentTimeMillis());
    return qrCodeData.toString();
  }
}