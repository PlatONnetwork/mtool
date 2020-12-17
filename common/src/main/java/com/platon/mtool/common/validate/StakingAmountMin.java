package com.platon.mtool.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * hibernate validator注解
 *
 * <p>Created by liyf.
 */
@Target({
  ElementType.METHOD,
  ElementType.FIELD,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.PARAMETER,
  ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StakingAmountMin.List.class)
@Documented
@Constraint(validatedBy = {StakingAmountValidator.class})
public @interface StakingAmountMin {

  String message() default "{javax.validation.constraints.DecimalMin.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String value();

  boolean inclusive() default true;

  @Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
  })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {

    StakingAmountMin[] value();
  }
}
