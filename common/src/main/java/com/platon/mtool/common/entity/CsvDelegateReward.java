package com.platon.mtool.common.entity;

import java.math.BigInteger;

public class CsvDelegateReward {

  private String delegateAddress;

  private BigInteger blockAmount;

  private BigInteger stakingAmount;

  private BigInteger tradeAmount;

  private String rewardProcess;

  private BigInteger totalAmount;

  private Integer settlePeriod;

  private BigInteger closedAmount;
  // 待发放金额
  private BigInteger divideAmount;
  // 调账金额
  private BigInteger adjustAmount;
  // 转账金额
  private BigInteger transferAmount;

  private BigInteger transactionFee;

  private String transactionHash;

  public String getDelegateAddress() {
    return delegateAddress;
  }

  public void setDelegateAddress(String delegateAddress) {
    this.delegateAddress = delegateAddress;
  }

  public BigInteger getBlockAmount() {
    return blockAmount;
  }

  public void setBlockAmount(BigInteger blockAmount) {
    this.blockAmount = blockAmount;
  }

  public BigInteger getStakingAmount() {
    return stakingAmount;
  }

  public void setStakingAmount(BigInteger stakingAmount) {
    this.stakingAmount = stakingAmount;
  }

  public BigInteger getTradeAmount() {
    return tradeAmount;
  }

  public void setTradeAmount(BigInteger tradeAmount) {
    this.tradeAmount = tradeAmount;
  }

  public String getRewardProcess() {
    return rewardProcess;
  }

  public void setRewardProcess(String rewardProcess) {
    this.rewardProcess = rewardProcess;
  }

  public BigInteger getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigInteger totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Integer getSettlePeriod() {
    return settlePeriod;
  }

  public void setSettlePeriod(Integer settlePeriod) {
    this.settlePeriod = settlePeriod;
  }

  public BigInteger getClosedAmount() {
    return closedAmount;
  }

  public void setClosedAmount(BigInteger closedAmount) {
    this.closedAmount = closedAmount;
  }

  public BigInteger getDivideAmount() {
    return divideAmount;
  }

  public void setDivideAmount(BigInteger divideAmount) {
    this.divideAmount = divideAmount;
  }

  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public BigInteger getAdjustAmount() {
    return adjustAmount;
  }

  public void setAdjustAmount(BigInteger adjustAmount) {
    this.adjustAmount = adjustAmount;
  }

  public BigInteger getTransactionFee() {
    return transactionFee;
  }

  public void setTransactionFee(BigInteger transactionFee) {
    this.transactionFee = transactionFee;
  }

  public BigInteger getTransferAmount() {
    return transferAmount;
  }

  public void setTransferAmount(BigInteger transferAmount) {
    this.transferAmount = transferAmount;
  }
}
