package com.platon.mtool.client.execute.sub;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.platon.mtool.client.execute.MtoolExecutor;
import com.platon.mtool.client.options.TxOptions;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.entity.AdditionalInfo;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.enums.FuncTypeEnum;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.AddressUtil;
import com.platon.mtool.common.utils.HashUtil;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.utils.MtoolCsvFileUtil;
import com.platon.mtool.common.web3j.Keystore;
import com.platon.mtool.common.web3j.MtoolTransactionManager;
import com.platon.mtool.common.web3j.TransactionEntity;
import com.alaya.contracts.ppos.DelegateContract;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.tx.TransactionManager;
import com.alaya.tx.gas.GasProvider;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 委托
 *
 * <p>Created by liyf
 */
public class TxDelegateExecutor extends MtoolExecutor<TxOptions.DelegateOption> {

  private static final Logger logger = LoggerFactory.getLogger(TxDelegateExecutor.class);

  public TxDelegateExecutor(JCommander commander, TxOptions.DelegateOption delegateOption) {
    super(commander, delegateOption);
  }

  private BlockChainService blockChainService = BlockChainService.singleton();

  @Override
  public void execute(TxOptions.DelegateOption option) throws Exception {
    ValidatorConfig validatorConfig = option.getConfig();
    Web3j web3j = getWeb3j(validatorConfig);
    blockChainService.validNodeExist(web3j, option.getNodeId());

    Credentials credentials = option.getKeystore().getCredentials();
    if (option.getKeystore().getType().equals(Keystore.Type.NORMAL)) {
      DelegateContract delegateContract =
          getDelegateContract(web3j, credentials, CLIENT_CONFIG.getTargetChainId());
      GasProvider gasProvider =
          delegateContract.getDelegateGasProvider(
              option.getNodeId(),
              option.getAmount().getAmountType(),
              option.getAmount().getAmount());
      GasProvider gasProviderEst =
          new GasProvider() {
            @Override
            public BigInteger getGasPrice() {
              return gasProvider.getGasPrice();
            }

            @Override
            public BigInteger getGasLimit() {
              return gasProvider.getGasLimit().add(BigInteger.valueOf(1000));
            }
          };

      blockChainService.validBalanceEnough(
          option.getKeystore().getAddress(), option.getAmount().getAmount(), gasProviderEst, web3j,option.getAmount().getAmountType());
      PlatonSendTransaction transaction =
          delegateContract
              .delegateReturnTransaction(
                  option.getNodeId(),
                  option.getAmount().getAmountType(),
                  option.getAmount().getAmount(),
                  gasProviderEst)
              .send();
      TransactionResponse response = delegateContract.getTransactionResponse(transaction).send();
      LogUtils.info(
          logger,
          () -> Log.newBuilder().msg(AllCommands.Tx.DELEGATE).kv("transaction", transaction));
      LogUtils.info(
          logger, () -> Log.newBuilder().msg(AllCommands.Tx.DELEGATE).kv("response", response));
      echoResult(
          transaction,
          response,
          validatorConfig.getNodePublicKey(),
          blockChainService.getCostAmount(option.getAmount().getAmount(), gasProvider));
    } else if (option.getKeystore().getType().equals(Keystore.Type.OBSERVE)) {
        String targetChainAddress = AddressUtil.getTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),option.getKeystore().getAddress().getMainnet());

        DelegateContract delegateContract =
          getDelegateContract(web3j, targetChainAddress, CLIENT_CONFIG.getTargetChainId());
      GasProvider gasProvider =
          delegateContract.getDelegateGasProvider(
              option.getNodeId(),
              option.getAmount().getAmountType(),
              option.getAmount().getAmount());
      GasProvider gasProviderEst =
          new GasProvider() {
            @Override
            public BigInteger getGasPrice() {
              return gasProvider.getGasPrice();
            }

            @Override
            public BigInteger getGasLimit() {
              return gasProvider.getGasLimit().add(BigInteger.valueOf(1000));
            }
          };
      blockChainService.validBalanceEnough(
          option.getKeystore().getAddress(), option.getAmount().getAmount(), gasProviderEst, web3j,option.getAmount().getAmountType());

      PlatonSendTransaction transaction =
          delegateContract
              .delegateReturnTransaction(
                  option.getNodeId(),
                  option.getAmount().getAmountType(),
                  option.getAmount().getAmount(),
                  gasProviderEst)
              .send();

      TransactionEntity entity =
          JSON.parseObject(transaction.getTransactionHash(), TransactionEntity.class);

      entity.setType(FuncTypeEnum.DELEGATE);
      entity.setAmount(option.getAmount().getAmount());
      entity.setAccountType(option.getAmount().getAmountType().name());
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
      PrintUtils.echo("File generated on: %s", filepath);
    }
  }

  protected Web3j getWeb3j(ValidatorConfig validatorConfig) {
    return com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
  }

  public DelegateContract getDelegateContract(
      Web3j web3j, Credentials credentials, Long chainId) {
    return DelegateContract.load(web3j, credentials, chainId);
  }

  public DelegateContract getDelegateContract(Web3j web3j, String fromAddress, Long chainId) {
    TransactionManager transactionManager =
        new MtoolTransactionManager(web3j, fromAddress, chainId);
    return DelegateContract.load(web3j, transactionManager,chainId);
  }
}
