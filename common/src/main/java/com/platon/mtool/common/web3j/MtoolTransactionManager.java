package com.platon.mtool.common.web3j;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import com.alaya.crypto.RawTransaction;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.core.methods.response.PlatonGetTransactionCount;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.protocol.exceptions.TransactionException;
import com.alaya.tx.TransactionManager;

/**
 * 自定义区块链事务管理器，用来参与冷钱包业务
 * Created by liyf. */
public class MtoolTransactionManager extends TransactionManager {

  private final Long chainId;
  private final Web3j web3j;
  private final String fromAddress;
  private BigInteger nonce = null;

  public MtoolTransactionManager(Web3j web3j, String fromAddress, Long chainId) {
    super(web3j, fromAddress);
    this.chainId = chainId;
    this.web3j = web3j;
    this.fromAddress = fromAddress;
  }

  private BigInteger getNonce() throws IOException {
    if (nonce == null) {
      PlatonGetTransactionCount ethGetTransactionCount =
          web3j.platonGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();

      if (ethGetTransactionCount.getTransactionCount().intValue() == 0) {
        ethGetTransactionCount =
            web3j.platonGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
      }

      nonce = ethGetTransactionCount.getTransactionCount();
    } else {
      nonce = nonce.add(BigInteger.ONE);
    }

    return nonce;
  }

  @Override
  protected TransactionReceipt executeTransaction(
      BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
      throws IOException, TransactionException {
    PlatonSendTransaction ethSendTransaction = sendTransaction(gasPrice, gasLimit, to, data, value);
    TransactionReceipt receipt = new TransactionReceipt();
    receipt.setTransactionHash(ethSendTransaction.getTransactionHash());
    return receipt;
  }

  @Override
  public PlatonSendTransaction sendTransaction(
      BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
      throws IOException {
    RawTransaction rawTransaction =
        RawTransaction.createTransaction(getNonce(), gasPrice, gasLimit, to, value, data);

    TransactionEntity entity = new TransactionEntity(rawTransaction);
    entity.setCreateTime(LocalDateTime.now());
    entity.setFee(gasLimit.multiply(gasPrice));
    entity.setFrom(fromAddress);
    entity.setChainId(chainId);
    PlatonSendTransaction platonSendTransaction = new PlatonSendTransaction();
    platonSendTransaction.setResult(JSON.toJSONString(entity));
    return platonSendTransaction;
  }

  @Override
  public TransactionReceipt getTransactionReceipt(PlatonSendTransaction ethSendTransaction)
      throws IOException, TransactionException {
    return super.getTransactionReceipt(ethSendTransaction);
  }
}
