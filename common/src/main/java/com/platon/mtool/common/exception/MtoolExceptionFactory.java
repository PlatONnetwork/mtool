package com.platon.mtool.common.exception;

/**
 * mtool异常工厂
 *
 * <p>Created by liyf.
 */
public interface MtoolExceptionFactory<T extends MtoolException> {

  T create();

  T create(Object... args);
}
