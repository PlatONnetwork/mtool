package com.platon.mtool.client.options.delegate;

import com.beust.jcommander.Parameter;
import com.platon.mtool.client.converter.BigDecimalConverter;
import com.platon.mtool.client.converter.BigIntegerConverter;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.utils.PlatOnUnit;
import com.platon.tx.gas.GasProvider;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.BigInteger;

/** Created by 10103 */
public class GasProviderDelegate implements GasProvider {
  @Parameter(
      names = {AllParams.GAS_LIMIT},
      descriptionKey = AllParams.GAS_LIMIT,
      converter = BigIntegerConverter.class)
  @DecimalMin(value = "0", message = "Incorrect gaslimit")
  private BigInteger gasLimit;

  @Parameter(
      names = {AllParams.GAS_PRICE},
      descriptionKey = AllParams.GAS_PRICE,
      converter = BigDecimalConverter.class)
  @DecimalMin(value = "0", message = "Incorrect gasprice")
  private BigDecimal gasPriceLat;

  @Override
  public BigInteger getGasLimit() {
    return gasLimit;
  }

  @Override
  public BigInteger getGasPrice() {
    return gasPriceLat == null ? null : PlatOnUnit.latToVon(this.gasPriceLat);
  }
}
