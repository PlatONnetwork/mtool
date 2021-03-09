package com.platon.mtool.client;

import com.beust.jcommander.JCommander;
import com.platon.mtool.client.tools.PrintUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 基础命令执行器
 *
 * <p>Created by liyf.
 */
public abstract class CliExecutor<T extends CommonOption> {

  private JCommander commander;
  private T commonOption;

  public CliExecutor(JCommander commander, T commonOption) {
    this.commander = commander;
    this.commonOption = commonOption;
  }

  public void execute() {
    if (commonOption.isHelp()) {
      help();
    } else {
      if (!isOptionValid(commonOption)) {
        return;
      }
      if (PrintUtils.isJunitTest()) {
        // 单元测试时该入口不往下执行， 要使用execute(T option)
         return;
      }
      try {
        execute(commonOption);
      } catch (Exception e) {
        handleException(e);
      }
    }
  }

  public abstract void execute(T option) throws Exception;

  protected abstract void handleException(Exception e);

  public void help() {
    StringBuilder sb = new StringBuilder(128);
    commander.usage(sb);
    PrintUtils.echo(sb.toString().replaceAll("Possible Values: .*", ""));
  }

  protected boolean isOptionValid(T commonOption) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<T>> constraintSet = validator.validate(commonOption);
    if (!constraintSet.isEmpty()) {
      for (ConstraintViolation<T> constraintViolation : constraintSet) {
        PrintUtils.echo(
            "%s: %s",
            ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName(),
            constraintViolation.getMessage());
      }
      return false;
    }
    return true;
  }

  public JCommander getCommander() {
    return commander;
  }

  public T getCommonOption() {
    return commonOption;
  }
}
