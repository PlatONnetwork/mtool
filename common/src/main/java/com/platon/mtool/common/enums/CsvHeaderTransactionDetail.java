package com.platon.mtool.common.enums;

/**
 * 离线签名交易明细文件
 *
 * <p>Created by liyf.
 */
public enum CsvHeaderTransactionDetail {
  // Type,From,To,Account Type,Amount,Gas Price,Gas Limit,Fee,Nonce,Create Time,Additional Info,
  // TX data,Signed Time,Signed Data
  TYPE("类型"),
  FROM("发送地址"),
  TO("接收地址"),
  ACCOUNT_TYPE("账户类型"),
  AMOUNT("金额"),
  GAS_PRICE("gasPrice"),
  GAS_LIMIT("gasLimit"),
  FEE("费用"),
  NONCE("nonce"),
  CREATE_TIME("创建时间"),
  CHAIN_ID("chainId"),
  ADDITIONAL_INFO("额外信息"),
  HASH("Unique Code"),
  TX_DATA("data"),
  SIGNED_TIME("签名时间"),
  SIGNED_DATA("签名后数据");

  private String title;

  CsvHeaderTransactionDetail(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
