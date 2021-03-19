package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.tools.CliConfigUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Created by liyf. */
class PathConverterTest {

  private PathConverter converter = new PathConverter("--optionName");

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

  @Test
  void convert() throws URISyntaxException {
    Path originPath = Paths.get(ClassLoader.getSystemResource("validator_config.json").toURI());
    String filepath = originPath.toAbsolutePath().toString();

    Path path = converter.convert(filepath);
    assertEquals(originPath, path);
  }

  @Test
  void convertException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> converter.convert("/path/nofound"));
    assertEquals("\"--optionName\": /path/nofound (No such file )", exception.getMessage());
  }
}
