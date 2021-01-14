package com.platon.mtool.common.entity;

import com.platon.contracts.ppos.dto.enums.StakingAmountType;

import java.math.BigInteger;

/** Created by liyf. */
public class StakingAmount {

  private BigInteger amount;
  private StakingAmountType amountType;

  public BigInteger getAmount() {
    return amount;
  }

  public void setAmount(BigInteger amount) {
    this.amount = amount;
  }

  public StakingAmountType getAmountType() {
    return amountType;
  }

  public void setAmountType(StakingAmountType amountType) {
    this.amountType = amountType;
  }
}
