package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 大数BigDecimal转换器
 *
 * <p>Created by liyf.
 */
public class BigDecimalConverter extends BaseConverter<BigDecimal> {

  public BigDecimalConverter(String optionName) {
    super(optionName);
  }

  @Override
  public BigDecimal convert(String value) {
    if (StringUtils.isNotEmpty(value)) {
      try {
        return new BigDecimal(value);
      } catch (Exception e) {
        throw new ParameterException("Invalid " + getOptionName().replace("--", "").trim());
      }
    } else {
      return null;
    }
  }
}
