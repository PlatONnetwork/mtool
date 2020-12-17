package com.platon.mtool.common.enums;

/**
 * 奖励文件标题
 *
 * <p>Created by liyf.
 */
public enum CsvHeaderReward {
  // Delegator,Block reward,Fee reward,Staking reward,Total reward
  ADDRESS("验证人地址"),
  BLOCK_AMOUNT("出块奖励金额"),
  TRADE_AMOUNT("交易手续费金额"),
  STAKING_AMOUNT("质押奖励金额"),
  TOTAL_AMOUNT("总金额");
  private String title;

  CsvHeaderReward(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
