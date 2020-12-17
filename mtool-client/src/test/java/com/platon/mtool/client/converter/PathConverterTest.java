package com.platon.mtool.client.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.beust.jcommander.ParameterException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class PathConverterTest {

  private PathConverter converter = new PathConverter("--optionName");

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
