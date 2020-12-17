package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 路径转换器
 *
 * <p>Created by liyf.
 */
public class PathConverter extends BaseConverter<Path> {

  public PathConverter(String optionName) {
    super(optionName);
  }

  @Override
  public Path convert(String value) {
    Path path = Paths.get(value);
    if (!path.toFile().exists()) {
      throw new ParameterException(getErrorString(value, "No such file"));
    }
    return path;
  }

  @Override
  protected String getErrorString(String value, String to) {
    return "\"" + getOptionName() + "\": " + value + " (" + to + " )";
  }
}
