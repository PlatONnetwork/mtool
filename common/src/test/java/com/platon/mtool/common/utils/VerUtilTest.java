package com.platon.mtool.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.platon.mtool.common.exception.MtoolException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class VerUtilTest {

  @Test
  void shouldEquals() {
    BigInteger version = VerUtil.toInteger("1.1.0");
    assertEquals(BigInteger.valueOf(65792), version);
  }

  @Test
  void shouldVersionEquests() {
    String version = VerUtil.toVersion(BigInteger.valueOf(65792));
    assertEquals("1.1.0", version);
  }

  @Test
  void parseError() {
    MtoolException exception = assertThrows(MtoolException.class, () -> VerUtil.toInteger("1.a.0"));
    assertEquals("version is invalid", exception.getMessage());
  }
}
