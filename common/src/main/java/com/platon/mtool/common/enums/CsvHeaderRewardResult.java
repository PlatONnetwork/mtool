package com.platon.mtool.common.enums;

/**
 * 奖励结果文件标题
 *
 * <p>Created by liyf.
 */
public enum CsvHeaderRewardResult {
  // Delegator,Block reward,Fee reward,Staking reward,Issued reward,Actual reward,Txn hash, status
  ADDRESS("验证人地址"),
  BLOCK_AMOUNT("出块奖励金额"),
  TRADE_AMOUNT("交易手续费金额"),
  STAKING_AMOUNT("质押奖励金额"),
  CLOSED_AMOUNT("已结算金额"),
  DIVIDE_AMOUNT("待发放金额"),
  ADJUST_AMOUNT("调账后金额"),
  TRANSACTION_FEE("手续费"),
  TRANSFER_AMOUNT("实际转账金额") /*实际奖励金额，无用字段*/,
  TRANSACTION_HASH("交易哈希"),
  REWARD_PROCESS("交易状态");
  private String title;

  CsvHeaderRewardResult(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
