package com.platon.mtool.client.execute;

import com.beust.jcommander.JCommander;
import com.platon.mtool.client.ClientConsts;
import com.platon.mtool.client.options.SendSignedTxOption;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.enums.FuncTypeEnum;
import com.platon.mtool.common.enums.TransactionStatus;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.utils.MtoolCsvFileUtil;
import com.platon.mtool.common.utils.TransactionStatusUtil;
import com.platon.mtool.common.web3j.EmptyContract;
import com.platon.mtool.common.web3j.MtoolTransactionManager;
import com.platon.mtool.common.web3j.TransactionEntity;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.protocol.core.methods.response.TransactionReceipt;

import java.util.List;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 发送签名文件到链上
 *
 * <p>Created by liyf.
 */
public class SendSignedTxExecutor extends MtoolExecutor<SendSignedTxOption> {

  private static final Logger logger = LoggerFactory.getLogger(SendSignedTxExecutor.class);
  private static final String FAIL_TEMPLATE = "transaction %s failure, %s";

  public SendSignedTxExecutor(JCommander commander, SendSignedTxOption commonOption) {
    super(commander, commonOption);
  }

  protected Web3j getWeb3j(ValidatorConfig validatorConfig) {
    return com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
  }

  protected EmptyContract getEmptyContract(Web3j web3j) {
    return EmptyContract.load(web3j);
  }

  @Override
  public void execute(SendSignedTxOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg("SendSignedTx").kv("option", option));
    // 统计信息
    int successNum = 0;
    int failureNum = 0;
    int totalNum;

    List<TransactionEntity> entities =
        MtoolCsvFileUtil.readTransactionDetailFromFile(
            option.getFilelocation().toAbsolutePath().toString());

    // 校验文件
    for (TransactionEntity entity : entities) {
      if (StringUtils.isEmpty(entity.getSignData())) {
        throw new MtoolClientException("transaction details must be signed");
      }
    }

    totalNum = entities.size();

    String process = PrintUtils.interact("Send Transaction? (yes|no)", "yes|no", true);
    if ("yes".equalsIgnoreCase(process)) {
      ValidatorConfig validatorConfig = option.getConfig();
      Web3j web3j = getWeb3j(validatorConfig);
      EmptyContract emptyContract = getEmptyContract(web3j);
      int index = 0;
      for (TransactionEntity entity : entities) {
        index++;
        try {
          PlatonSendTransaction transaction =
              web3j.platonSendRawTransaction(entity.getSignData()).send();
          MtoolTransactionManager transactionManager =
              new MtoolTransactionManager(web3j, entity.getFrom(), CLIENT_CONFIG.getTargetChainId());

          if (FuncTypeEnum.TRANSFER.getCode() == entity.getType().getCode()) {
            // 普通转账
            TransactionReceipt receipt = transactionManager.getTransactionReceipt(transaction);
            TransactionStatus transactionStatus =
                TransactionStatusUtil.getTransactionStatus(receipt.getTo(), receipt);
            if (TransactionStatus.SUCCESS.equals(transactionStatus)) {
              entity.setTransactionStatus(TransactionStatus.SUCCESS);
              successNum += 1;
              PrintUtils.echo("transaction %s success", index);
            } else {
              entity.setTransactionStatus(TransactionStatus.FAILURE);
              failureNum += 1;
              PrintUtils.echo(FAIL_TEMPLATE, index, transaction.getError());
            }
            PrintUtils.echo(receipt.getTransactionHash());
            PrintUtils.echo(ClientConsts.SUCCESS);
          } else {
            // 内置合约
            TransactionResponse response = emptyContract.getTransactionResponse(transaction).send();
            echoResult(
                transaction,
                response,
                validatorConfig.getNodePublicKey(),
                entity.getAmount().add(entity.getFee()));
            successNum += 1;
          }
        } catch (Exception e) {
          logger.error("execute", e);
          entity.setTransactionStatus(TransactionStatus.FAILURE);
          PrintUtils.echo(FAIL_TEMPLATE, index, e.getMessage());
          failureNum += 1;
          if (PrintUtils.isJunitTest()) {
            throw e;
          }
        }
      }

      PrintUtils.echo("total: %s", totalNum);
      PrintUtils.echo("success: %s, failure: %s", successNum, failureNum);
    }
  }
}
