package com.platon.mtool.client.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.beust.jcommander.ParameterException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class BigDecimalConverterTest {

  private BigDecimalConverter converter = new BigDecimalConverter("--optionName");

  @Test
  void convert() {
    BigDecimal decimal = converter.convert("1");
    assertEquals(BigDecimal.ONE, decimal);
  }

  @Test
  void convertException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> converter.convert("1a"));
    assertEquals("Invalid optionName", exception.getMessage());
  }
}
