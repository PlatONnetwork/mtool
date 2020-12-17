package com.platon.mtool.common.enums;

/**
 * 交易状态
 *
 * <p>Created by liyf.
 */
public enum TransactionStatus {
  FAILURE,
  SUCCESS;

  public static TransactionStatus getFromBoolean(boolean flag) {
    return flag ? SUCCESS : FAILURE;
  }
}
