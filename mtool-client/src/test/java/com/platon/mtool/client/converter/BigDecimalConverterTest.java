package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.tools.CliConfigUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Created by liyf. */
class BigDecimalConverterTest {

  private BigDecimalConverter converter = new BigDecimalConverter("--optionName");

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

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
