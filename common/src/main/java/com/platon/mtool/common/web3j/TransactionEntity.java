package com.platon.mtool.common.web3j;

import com.platon.mtool.common.enums.FuncTypeEnum;
import com.platon.mtool.common.enums.TransactionStatus;
import com.alaya.crypto.RawTransaction;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 区块链事务实体定义
 *
 * <p>Created by liyf.
 */
public class TransactionEntity extends RawTransaction {

  // 基础字段
  private BigInteger nonce;
  private BigInteger gasPrice;
  private BigInteger gasLimit;
  private String to;
  private BigInteger value;
  private String data;

  // 加工字段
  // Type,From,To,Account Type,Amount,Gas Price,Gas Limit,Fee,Nonce,Create Time,Additional Info,
  // TX data,Signed Time,Signed Data
  private FuncTypeEnum type;
  private String from;
  private String accountType;
  private BigInteger amount;
  private BigInteger fee;
  private LocalDateTime createTime;
  private Long chainId;
  private String additionalInfo;
  private LocalDateTime signTime;
  private String signData;

  // 转账交易结果回填保存字段
  private String transactionHash;
  private TransactionStatus transactionStatus;

  private String hash;

  public TransactionEntity(
      BigInteger nonce,
      BigInteger gasPrice,
      BigInteger gasLimit,
      String to,
      BigInteger value,
      String data) {
    super(nonce, gasPrice, gasLimit, to, value, data);
  }

  public TransactionEntity(RawTransaction rawTransaction) {
    this(
        rawTransaction.getNonce(),
        rawTransaction.getGasPrice(),
        rawTransaction.getGasLimit(),
        rawTransaction.getTo(),
        rawTransaction.getValue(),
        rawTransaction.getData());
    this.nonce = rawTransaction.getNonce();
    this.gasPrice = rawTransaction.getGasPrice();
    this.gasLimit = rawTransaction.getGasLimit();
    this.to = rawTransaction.getTo();
    this.value = rawTransaction.getValue();
    this.data = rawTransaction.getData();
  }

  public TransactionEntity() {
    this(null, null, null, null, null, null);
  }

  @Override
  public BigInteger getNonce() {
    return nonce;
  }

  public void setNonce(BigInteger nonce) {
    this.nonce = nonce;
  }

  @Override
  public BigInteger getGasPrice() {
    return gasPrice;
  }

  public void setGasPrice(BigInteger gasPrice) {
    this.gasPrice = gasPrice;
  }

  @Override
  public BigInteger getGasLimit() {
    return gasLimit;
  }

  public void setGasLimit(BigInteger gasLimit) {
    this.gasLimit = gasLimit;
  }

  @Override
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  @Override
  public BigInteger getValue() {
    return value;
  }

  public void setValue(BigInteger value) {
    this.value = value;
  }

  @Override
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public FuncTypeEnum getType() {
    return type;
  }

  public void setType(FuncTypeEnum type) {
    this.type = type;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public BigInteger getAmount() {
    return amount;
  }

  public void setAmount(BigInteger amount) {
    this.amount = amount;
  }

  public BigInteger getFee() {
    return fee;
  }

  public void setFee(BigInteger fee) {
    this.fee = fee;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getSignTime() {
    return signTime;
  }

  public void setSignTime(LocalDateTime signTime) {
    this.signTime = signTime;
  }

  public String getSignData() {
    return signData;
  }

  public void setSignData(String signData) {
    this.signData = signData;
  }

  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public TransactionStatus getTransactionStatus() {
    return transactionStatus;
  }

  public void setTransactionStatus(TransactionStatus transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  public String getHash() {
    return hash;
  }

  public TransactionEntity setHash(String hash) {
    this.hash = hash;
    return this;
  }

  public Long getChainId() {
    return chainId;
  }

  public void setChainId(Long chainId) {
    this.chainId = chainId;
  }
}
