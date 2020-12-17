package com.platon.mtool.common.enums;

/**
 * mtool产生的csv文件枚举
 *
 * <p>Created by liyf.
 */
public enum MtoolCsvEnum {
  DELEGATE_REWARD(11, "delegate_reward.csv", CsvHeaderReward.class),
  DELEGATE_REWARD_SUMMARY(12, "delegate_reward_summary.csv", CsvHeaderRewardSummary.class),
  DELEGATE_REWARD_RESULT(13, "delegate_reward_result.csv", CsvHeaderRewardResult.class),
  TRANSACTION_DETAIL(1, "transaction_detail.csv", CsvHeaderTransactionDetail.class),
  ;
  // 模板文件名称
  private String filename;
  // 表头统计汇总行数
  private Integer summaryLines;
  // 表头标题枚举
  private Class<? extends Enum<?>> headerClass;

  MtoolCsvEnum(Integer summaryLines, String filename, Class<? extends Enum<?>> headerClass) {
    this.summaryLines = summaryLines;
    this.filename = filename;
    this.headerClass = headerClass;
  }

  public String getFilename() {
    return filename;
  }

  public Class<? extends Enum<?>> getHeaderClass() {
    return headerClass;
  }

  public Integer getSummaryLines() {
    return summaryLines;
  }
}
