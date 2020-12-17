package com.platon.mtool.common.utils;

import com.platon.mtool.common.logger.Log;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * 日志工具
 *
 * <p>Created by liyf.
 */
public class LogUtils {

  private LogUtils() {}

  /**
   * info 日志.
   *
   * @param logger 日志
   * @param supplier 日志建造者Supplier
   */
  public static void info(Logger logger, Supplier<Log.Builder> supplier) {
    if (logger.isInfoEnabled()) {
      logger.info(
          supplier
              .get()
              .op(Thread.currentThread().getStackTrace()[2].getMethodName())
              .build()
              .getTextMsg());
    }
  }

  /**
   * warn 日志.
   *
   * @param logger 日志
   * @param supplier 日志建造者Supplier
   */
  public static void warn(Logger logger, Supplier<Log.Builder> supplier) {
    if (logger.isInfoEnabled()) {
      logger.warn(
          supplier
              .get()
              .op(Thread.currentThread().getStackTrace()[2].getMethodName())
              .build()
              .getTextMsg());
    }
  }

  /**
   * error 日志.
   *
   * @param logger 日志
   * @param supplier 日志建造者Supplier
   */
  public static void error(Logger logger, Supplier<Log.Builder> supplier) {
    if (logger.isInfoEnabled()) {
      logger.error(
          supplier
              .get()
              .op(Thread.currentThread().getStackTrace()[2].getMethodName())
              .build()
              .getTextMsg());
    }
  }

  /**
   * debug 日志.
   *
   * @param logger 日志
   * @param supplier 日志建造者Supplier
   */
  public static void debug(Logger logger, Supplier<Log.Builder> supplier, Throwable e) {
    if (logger.isDebugEnabled()) {
      logger.debug(
          supplier
              .get()
              .op(Thread.currentThread().getStackTrace()[2].getMethodName())
              .build()
              .getTextMsg(),
          e);
    }
  }
}
