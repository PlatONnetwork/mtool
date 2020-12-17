package com.platon.mtool.common.enums;

/**
 * 奖励分配方式
 *
 * <p>Created by liyf.
 */
public enum RewardMethod {
  AVERAGE(0, "平均分配"),
  PERCENT(1, "百分比");

  private Integer code;
  private String title;

  RewardMethod(Integer code, String title) {
    this.code = code;
    this.title = title;
  }

  public Integer getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }
}
