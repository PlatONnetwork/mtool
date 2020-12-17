package com.platon.mtool.common.validate;

import com.platon.mtool.common.entity.StakingAmount;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;

/**
 * hibernate validator implements
 *
 * <p>Created by liyf.
 */
public class StakingAmountValidator
    implements ConstraintValidator<StakingAmountMin, StakingAmount> {

  private static final org.hibernate.validator.internal.util.logging.Log LOG =
      LoggerFactory.make(MethodHandles.lookup());
  private BigInteger minValue;
  private boolean inclusive;

  @Override
  public void initialize(StakingAmountMin constraintAnnotation) {
    try {
      this.minValue = new BigInteger(constraintAnnotation.value());
    } catch (NumberFormatException e) {
      throw LOG.getInvalidBigDecimalFormatException(constraintAnnotation.value(), e);
    }
    this.inclusive = constraintAnnotation.inclusive();
  }

  @Override
  public boolean isValid(
      StakingAmount stakingAmount, ConstraintValidatorContext constraintValidatorContext) {
    if (stakingAmount == null || stakingAmount.getAmount() == null) {
      return true;
    } else {
      try {
        int comparisonResult = stakingAmount.getAmount().compareTo(this.minValue);
        return this.inclusive ? comparisonResult >= 0 : comparisonResult > 0;
      } catch (NumberFormatException e) {
        return false;
      }
    }
  }
}
