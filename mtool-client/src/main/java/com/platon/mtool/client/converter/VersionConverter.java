package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.mtool.common.utils.VerUtil;

import java.math.BigInteger;

/**
 * 版本号转换器
 *
 * <p>Created by liyf.
 */
public class VersionConverter extends BaseConverter<BigInteger> {

  public VersionConverter(String optionName) {
    super(optionName);
  }

  @Override
  public BigInteger convert(String value) {
    try {
      return VerUtil.toInteger(value);
    } catch (Exception e) {
      throw new ParameterException(getErrorString(value, " version is invalid"));
    }
  }

  @Override
  protected String getErrorString(String value, String to) {
    return "\"" + getOptionName() + "\": " + value + " (" + to + " )";
  }
}
