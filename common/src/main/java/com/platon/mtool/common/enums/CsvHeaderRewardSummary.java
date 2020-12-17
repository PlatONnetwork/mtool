package com.platon.mtool.common.enums;

/**
 * 奖励汇总文件标题
 *
 * <p>Created by liyf.
 */
public enum CsvHeaderRewardSummary {
  // Delegator,Block reward,Fee reward,Staking reward,Issued reward,Actual reward
  ADDRESS("验证人地址"),
  BLOCK_AMOUNT("出块奖励金额"),
  TRADE_AMOUNT("交易手续费金额"),
  STAKING_AMOUNT("质押奖励金额"),
  CLOSED_AMOUNT("已结算金额"),
  DIVIDE_AMOUNT("待发放金额"),
  ADJUST_AMOUNT("调账后金额");
  private String title;

  CsvHeaderRewardSummary(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
