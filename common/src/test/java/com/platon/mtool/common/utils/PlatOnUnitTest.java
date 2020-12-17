package com.platon.mtool.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class PlatOnUnitTest {

  private static BigInteger ZERO_18 = BigInteger.valueOf(1_000_000_000_000_000_000L);

  @Test
  void latToVon() {
    BigInteger von = PlatOnUnit.latToVon(BigInteger.ONE);
    assertEquals(ZERO_18, von);
    von = PlatOnUnit.latToVon(BigDecimal.ONE);
    assertEquals(ZERO_18, von);
  }

  @Test
  void vonToLat() {
    BigDecimal lat = PlatOnUnit.vonToLat(BigInteger.valueOf(1_000_000_000_000_000_001L));
    assertEquals(new BigDecimal("1.000000000000000001"), lat);
  }

  @Test
  void format() {
    BigDecimal lat = PlatOnUnit.vonToLat(BigInteger.valueOf(1_000_000_000_000_000_001L));
    String formatString = lat.setScale(12, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
    assertEquals("1", formatString);
  }
}
