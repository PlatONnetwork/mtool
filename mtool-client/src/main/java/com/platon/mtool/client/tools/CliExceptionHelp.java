package com.platon.mtool.client.tools;

import com.platon.mtool.common.exception.MtoolException;
import com.platon.mtool.common.exception.MtoolPlatonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.protocol.exceptions.NoTransactionReceiptException;
import com.alaya.protocol.exceptions.TransactionException;

import java.net.ConnectException;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.TimeoutException;

/**
 * 客户端通用异常帮助类
 *
 * <p>Created by liyf.
 */
public abstract class CliExceptionHelp {

  private static final Logger logger = LoggerFactory.getLogger(CliExceptionHelp.class);

  private CliExceptionHelp() {}

  public static void stopAndPrintError(Exception e) {
    logger.error("execute", e);
    if (PrintUtils.isJunitTest()) {
      ProgressBar.stop("error occur");
      throw new MtoolException(e);
    }
    if (e instanceof TimeoutException
        || e instanceof ConnectException
        || e instanceof java.net.SocketTimeoutException) {
      ProgressBar.stop("Unable to connect to node");
    } else if (e instanceof NoTransactionReceiptException) {
      // sdk
      ProgressBar.stop(e.getMessage());
    } else if (e instanceof TransactionException) {
      // sdk
      ProgressBar.stop(e.getMessage());
    } else if (e instanceof MtoolPlatonException) {
      ProgressBar.stop(e.getMessage());
    } else if (e instanceof NoSuchFileException) {
      ProgressBar.stop("No such file: " + e.getMessage());
    } else if (e.getMessage() != null) {
      ProgressBar.stop(e.getMessage());
    } else {
      ProgressBar.stop("error occur");
    }
  }
}
