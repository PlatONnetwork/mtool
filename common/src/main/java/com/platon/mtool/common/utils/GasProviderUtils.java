package com.platon.mtool.common.utils;

import com.platon.mtool.common.exception.MtoolException;
import com.platon.protocol.Web3j;
import com.platon.tx.Transfer;
import com.platon.tx.gas.GasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * GasProvider工具
 *
 * <p>Created by liyf
 */
public abstract class GasProviderUtils {
  private static final Logger logger = LoggerFactory.getLogger(GasProviderUtils.class);

  public static GasProvider getGasProvider(
      GasProvider paramGasProvider, Supplier<GasProvider> gasProviderSupplier) {
    if (paramGasProvider.getGasLimit() != null && paramGasProvider.getGasPrice() != null) {
      return paramGasProvider;
    }
    GasProvider gasProvider = gasProviderSupplier.get();
    return new GasProvider() {
      @Override
      public BigInteger getGasPrice() {
        return paramGasProvider.getGasPrice() == null
            ? gasProvider.getGasPrice()
            : paramGasProvider.getGasPrice();
      }

      @Override
      public BigInteger getGasLimit() {
        return paramGasProvider.getGasLimit() == null
            ? gasProvider.getGasLimit()
            : paramGasProvider.getGasLimit();
      }
    };
  }

  public static GasProvider getTransferGasProvider(Web3j web3j) {
    BigInteger gasPrice;
    try {
      gasPrice = web3j.platonGasPrice().send().getGasPrice();
    } catch (IOException e) {
      logger.error("platonGasPrice error", e);
      throw new MtoolException("platonGasPrice error");
    }
    BigInteger finalGasPrice = gasPrice;
    return new GasProvider() {
      @Override
      public BigInteger getGasPrice() {
        return finalGasPrice;
      }

      @Override
      public BigInteger getGasLimit() {
        return Transfer.GAS_LIMIT;
      }
    };
  }
}
