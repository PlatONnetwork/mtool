package com.platon.mtool.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class MtoolExceptionFactoryTest {

  @Test
  void createPlatonException() {
    assertEquals(
        "Insufficient wallet balance(Total payment: 5000000 ATP)",
        MtoolPlatonExceptionCode.E301111.create(BigInteger.valueOf(5000_000)).getMessage());
  }
}
