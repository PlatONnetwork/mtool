package com.platon.mtool.client.converter;

import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.utils.PlatOnUnit;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 质押金额转换器
 *
 * <p>Created by liyf.
 */
public class StakingAmountConverter extends BaseConverter<StakingAmount> {
  public StakingAmountConverter(String optionName) {
    super(optionName);
  }

  @Override
  public StakingAmount convert(String value) {
    StakingAmount stakingAmount = new StakingAmount();
    if (getOptionName().equals(AllParams.AMOUNT)) {
      stakingAmount.setAmountType(StakingAmountType.FREE_AMOUNT_TYPE);
    } else if (getOptionName().equals(AllParams.RESTRICTEDAMOUNT)) {
      stakingAmount.setAmountType(StakingAmountType.RESTRICTING_AMOUNT_TYPE);
    } else if (getOptionName().equals(AllParams.AUTO_AMOUNT)){
      stakingAmount.setAmountType(StakingAmountType.AUTO_AMOUNT_TYPE);
    }

    if (StringUtils.isNotEmpty(value)) {
      if (value.contains(".") && value.substring(value.indexOf('.') + 1).length() > 8) {
        throw new ParameterException(
            getOptionName().replace("--", "").trim() + " cannot exceed 8 decimal places");
      }
      try {
        BigDecimal lat = new BigDecimal(value);
        stakingAmount.setAmount(PlatOnUnit.latToVon(lat));
      } catch (Exception e) {
        throw new ParameterException("Invalid " + getOptionName().replace("--", "").trim());
      }
    }
    return stakingAmount;
  }
}
