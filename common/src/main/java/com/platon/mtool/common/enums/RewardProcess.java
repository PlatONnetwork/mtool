package com.platon.mtool.common.enums;

import com.platon.mtool.common.exception.MtoolException;

/**
 * 奖励分配状态
 *
 * <p>Created by liyf.
 */
public enum RewardProcess {
  INIT(1, "Init"),
  DOING(2, "Doing"),
  DONE(3, "Success"),
  FAIL(4, "Fail");

  private Integer code;
  private String title;

  RewardProcess(Integer code, String title) {
    this.code = code;
    this.title = title;
  }

  public static RewardProcess getFromTitle(String title) {
    for (RewardProcess value : RewardProcess.values()) {
      if (value.getTitle().equals(title)) {
        return value;
      }
    }
    throw new MtoolException("RewardProcess not found, title:" + title);
  }

  public static RewardProcess getFromName(String name) {
    for (RewardProcess value : RewardProcess.values()) {
      if (value.name().equals(name)) {
        return value;
      }
    }
    throw new MtoolException("RewardProcess not found, name:" + name);
  }

  public Integer getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }
}
