package com.platon.mtool.common.entity;

import com.platon.mtool.common.enums.RewardMethod;

/** Created by liyf. */
public class RewardConfigDetail {

  private RewardMethod rewardMethod;
  private float commissionRatio;

  public RewardMethod getRewardMethod() {
    return rewardMethod;
  }

  public void setRewardMethod(RewardMethod rewardMethod) {
    this.rewardMethod = rewardMethod;
  }

  public float getCommissionRatio() {
    return commissionRatio;
  }

  public void setCommissionRatio(float commissionRatio) {
    this.commissionRatio = commissionRatio;
  }
}
