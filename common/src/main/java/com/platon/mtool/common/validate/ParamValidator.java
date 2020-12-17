package com.platon.mtool.common.validate;

import com.google.common.collect.ImmutableMap;
import com.platon.mtool.common.entity.ValidatorConfig;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 验证人配置文件校验工具
 *
 * <p>Created by liyf.
 */
public class ParamValidator {
  private static Map<String, String> aliasMap = ImmutableMap.of("rewardPer", "delegatedRewardRate");

  private ParamValidator() {}

  public static Map<String /*name*/, String /*message*/> validConfig(
      ValidatorConfig validatorConfig, Class<?>... groups) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<ValidatorConfig>> constraintSet =
        validator.validate(validatorConfig, groups);
    Map<String, String> invalidParamMap = new HashMap<>();
    if (!constraintSet.isEmpty()) {
      for (ConstraintViolation<ValidatorConfig> constraintViolation : constraintSet) {
        String name = ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName();
        if (aliasMap.containsKey(name)) {
          name = aliasMap.get(name);
        }
        invalidParamMap.put(name, constraintViolation.getMessage());
      }
    }
    return invalidParamMap;
  }
}
