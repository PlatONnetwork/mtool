package com.platon.mtool.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * 区块链单位换算
 *
 * <p>Created by liyf.
 */
public abstract class PlatOnUnit {

  private static BigInteger vonFactor = BigInteger.TEN.pow(18);

  private PlatOnUnit() {}

  public static BigInteger latToVon(BigInteger lat) {
    return lat.multiply(vonFactor);
  }

  public static BigInteger latToVon(BigDecimal lat) {
    return lat.multiply(new BigDecimal(vonFactor)).toBigInteger();
  }

  public static BigDecimal vonToLat(BigInteger von) {
    return new BigDecimal(von).divide(new BigDecimal(vonFactor), 18, RoundingMode.HALF_UP);
  }
}
