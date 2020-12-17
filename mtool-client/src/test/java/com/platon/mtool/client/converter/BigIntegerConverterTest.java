package com.platon.mtool.client.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.beust.jcommander.ParameterException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class BigIntegerConverterTest {

  private BigIntegerConverter converter = new BigIntegerConverter("--optionName");

  @Test
  void convert() {
    BigInteger decimal = converter.convert("1");
    assertEquals(BigInteger.ONE, decimal);
  }

  @Test
  void convertException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> converter.convert("1a"));
    assertEquals("Invalid optionName", exception.getMessage());
  }
}
