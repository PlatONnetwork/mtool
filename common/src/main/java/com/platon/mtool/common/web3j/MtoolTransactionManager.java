package com.platon.mtool.common.web3j;

import com.alibaba.fastjson.JSON;
import com.platon.crypto.RawTransaction;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.PlatonGetTransactionCount;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.exceptions.TransactionException;
import com.platon.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 自定义区块链事务管理器，用来参与冷钱包业务
 * Created by liyf. */
public class MtoolTransactionManager extends TransactionManager {

  private final Web3j web3j;
  private final String fromAddress;
  private BigInteger nonce = null;

  public MtoolTransactionManager(Web3j web3j, String fromAddress) {
    super(web3j, fromAddress);
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
    //entity.setChainId(chainId);
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
