package com.platon.mtool.common.entity;

import java.math.BigInteger;
import java.util.List;

/** Created by liyf. */
public class CsvRewardSummary {

  private BigInteger startBlockNumber;
  private BigInteger endBlockNumber;
  private RewardConfigTotal rewardConfig;
  private String nodeName; // validatorName
  private List<String> benefitAddressList;
  private List<BigInteger> benefitAmountList;
  private BigInteger totalRewardAmount;
  private BigInteger totalBlockAmount;
  private BigInteger totalTradeAmount;
  private BigInteger totalStakingAmount;
  private BigInteger totalDivideAmount;
  // 0.7.3
  private Boolean isAdjust = false;
  private BigInteger totalTransactionFee;
  private BigInteger totalAdjustAmount;
  private String divideStatus;

  public BigInteger getTotalAdjustAmount() {
    return totalAdjustAmount;
  }

  public void setTotalAdjustAmount(BigInteger totalAdjustAmount) {
    this.totalAdjustAmount = totalAdjustAmount;
  }

  public BigInteger getStartBlockNumber() {
    return startBlockNumber;
  }

  public void setStartBlockNumber(BigInteger startBlockNumber) {
    this.startBlockNumber = startBlockNumber;
  }

  public BigInteger getEndBlockNumber() {
    return endBlockNumber;
  }

  public void setEndBlockNumber(BigInteger endBlockNumber) {
    this.endBlockNumber = endBlockNumber;
  }

  public RewardConfigTotal getRewardConfig() {
    return rewardConfig;
  }

  public void setRewardConfig(RewardConfigTotal rewardConfig) {
    this.rewardConfig = rewardConfig;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public List<String> getBenefitAddressList() {
    return benefitAddressList;
  }

  public void setBenefitAddressList(List<String> benefitAddressList) {
    this.benefitAddressList = benefitAddressList;
  }

  public List<BigInteger> getBenefitAmountList() {
    return benefitAmountList;
  }

  public void setBenefitAmountList(List<BigInteger> benefitAmountList) {
    this.benefitAmountList = benefitAmountList;
  }

  public BigInteger getTotalRewardAmount() {
    return totalRewardAmount;
  }

  public void setTotalRewardAmount(BigInteger totalRewardAmount) {
    this.totalRewardAmount = totalRewardAmount;
  }

  public BigInteger getTotalBlockAmount() {
    return totalBlockAmount;
  }

  public void setTotalBlockAmount(BigInteger totalBlockAmount) {
    this.totalBlockAmount = totalBlockAmount;
  }

  public BigInteger getTotalTradeAmount() {
    return totalTradeAmount;
  }

  public void setTotalTradeAmount(BigInteger totalTradeAmount) {
    this.totalTradeAmount = totalTradeAmount;
  }

  public BigInteger getTotalStakingAmount() {
    return totalStakingAmount;
  }

  public void setTotalStakingAmount(BigInteger totalStakingAmount) {
    this.totalStakingAmount = totalStakingAmount;
  }

  public BigInteger getTotalDivideAmount() {
    return totalDivideAmount;
  }

  public void setTotalDivideAmount(BigInteger totalDivideAmount) {
    this.totalDivideAmount = totalDivideAmount;
  }

  public String getDivideStatus() {
    return divideStatus;
  }

  public void setDivideStatus(String divideStatus) {
    this.divideStatus = divideStatus;
  }

  public BigInteger getTotalTransactionFee() {
    return totalTransactionFee;
  }

  public void setTotalTransactionFee(BigInteger totalTransactionFee) {
    this.totalTransactionFee = totalTransactionFee;
  }

  public Boolean getAdjust() {
    return isAdjust;
  }

  public void setAdjust(Boolean adjust) {
    isAdjust = adjust;
  }
}
