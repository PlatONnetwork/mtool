package com.platon.mtool.common.utils;

import com.platon.mtool.common.web3j.TransactionEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 交易hash校验工具
 *
 * <p>Created by liyf.
 */
public class HashUtil {

  private HashUtil() {}

  /**
   * 生成一致性hash字符串.
   *
   * @param entity TransactionEntity
   * @return hash string
   */
  public static String hashTransaction(TransactionEntity entity) {
    String msg =
        entity.getTo()
            + entity.getNonce()
            + entity.getGasPrice()
            + entity.getGasLimit()
            + entity.getValue()
            + entity.getData();
    msg =
        msg
            + entity.getType().name()
            + entity.getFrom()
            + entity.getAmount()
            + StringUtils.trimToEmpty(entity.getAccountType());
    return DigestUtils.md5Hex(msg);
  }

  public static boolean isTransactionValid(TransactionEntity entity) {
    return entity.getHash() != null && entity.getHash().equals(hashTransaction(entity));
  }
}
