package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.common.AllParams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Created by liyf. */
class VersionConverterTest {

  private VersionConverter converter = new VersionConverter(AllParams.NEWVERSION);

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

  @Test
  void convert() {
    BigInteger version = converter.convert("1.0.1");
    assertEquals(new BigInteger("65537"), version);
  }

  @Test
  void convertException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> converter.convert("1.0.1a"));
    assertEquals("\"--newversion\": 1.0.1a ( version is invalid )", exception.getMessage());
  }
}
