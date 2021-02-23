package com.platon.mtool.common.exception;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Created by liyf. */
class MtoolExceptionFactoryTest {

  @Test
  void createPlatonException() {
    assertEquals(
        "Insufficient wallet balance(Total payment: 5000000 LAT)",
        MtoolPlatonExceptionCode.E301111.create(BigInteger.valueOf(5000_000)).getMessage());
  }
}
