package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

/**
 * 大数BigInteger转换器
 *
 * <p>Created by liyf.
 */
public class BigIntegerConverter extends BaseConverter<BigInteger> {

  public BigIntegerConverter(String optionName) {
    super(optionName);
  }

  @Override
  public BigInteger convert(String value) {
    if (StringUtils.isNotEmpty(value)) {
      try {
        return new BigInteger(value);
      } catch (Exception e) {
        // amount校验提示：Invalid amount【前】
        throw new ParameterException("Invalid " + getOptionName().replace("--", "").trim());
      }
    } else {
      return null;
    }
  }
}
