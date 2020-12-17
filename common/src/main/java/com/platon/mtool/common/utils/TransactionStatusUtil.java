package com.platon.mtool.common.utils;

import com.platon.mtool.common.enums.TransactionStatus;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.resolver.ReceiptDataResponseResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.protocol.core.methods.response.TransactionReceipt;

import java.util.Arrays;
import java.util.List;

/**
 * 交易状态
 *
 * <p>Created by liyf.
 */
public abstract class TransactionStatusUtil {

  // 内置合约
  private static final List<String> CONTRACTS_ADDRESS =
      Arrays.asList(
          "0x1000000000000000000000000000000000000001",
          "0x1000000000000000000000000000000000000002",
          "0x1000000000000000000000000000000000000003",
          "0x1000000000000000000000000000000000000004",
          "0x1000000000000000000000000000000000000005");
  private static final Logger logger = LoggerFactory.getLogger(TransactionStatusUtil.class);

  private TransactionStatusUtil() {}

  public static TransactionStatus getTransactionStatus(
      String toAddress, TransactionReceipt platonReceipt) {
    if (platonReceipt == null) {
      LogUtils.warn(logger, () -> Log.newBuilder().msg("receipt is null"));
      return TransactionStatus.FAILURE;
    }
    TransactionStatus trasactionStatus;
    try {
      if (!platonReceipt.getLogs().isEmpty()) {
        ReceiptDataResponseResolver resolver = new ReceiptDataResponseResolver();
        ReceiptDataResponseResolver.ReceiptDataResponse response =
            resolver.resolv(platonReceipt.getLogs().get(0).getData());
        if (response != null) {
          trasactionStatus = TransactionStatus.getFromBoolean(response.getStatus());
        } else {
          trasactionStatus = TransactionStatus.FAILURE;
        }
      } else {
        // 如果是内置合约， logs为空是失败， 否则logs为空是成功
        if (CONTRACTS_ADDRESS.contains(toAddress)) {
          trasactionStatus = TransactionStatus.FAILURE;
        } else {
          trasactionStatus = TransactionStatus.SUCCESS;
        }
      }
    } catch (Exception e) {
      LogUtils.warn(logger, () -> Log.newBuilder().msg("getTransactionStatus error"));
      trasactionStatus = TransactionStatus.FAILURE;
    }
    return trasactionStatus;
  }
}
