package com.platon.mtool.common.entity.http;

import java.math.BigInteger;

/** Created by liyf. */
public class PeriodInfo {

  private BigInteger startBlockNumber;
  private BigInteger endBlockNumber;
  private Integer settlePeriod;

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

  public Integer getSettlePeriod() {
    return settlePeriod;
  }

  public void setSettlePeriod(Integer settlePeriod) {
    this.settlePeriod = settlePeriod;
  }
}
