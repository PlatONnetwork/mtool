package com.platon.mtool.common.entity;

import com.platon.mtool.common.enums.FeePayerEnum;

/** Created by liyf. */
public class RewardConfigTotal {

  private RewardConfigDetail staking;
  private RewardConfigDetail block;
  private RewardConfigDetail trade;
  private FeePayerEnum feePayer;

  public RewardConfigDetail getStaking() {
    return staking;
  }

  public void setStaking(RewardConfigDetail staking) {
    this.staking = staking;
  }

  public RewardConfigDetail getBlock() {
    return block;
  }

  public void setBlock(RewardConfigDetail block) {
    this.block = block;
  }

  public RewardConfigDetail getTrade() {
    return trade;
  }

  public void setTrade(RewardConfigDetail trade) {
    this.trade = trade;
  }

  public FeePayerEnum getFeePayer() {
    return feePayer;
  }

  public void setFeePayer(FeePayerEnum feePayer) {
    this.feePayer = feePayer;
  }
}
