package com.platon.mtool.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Created by liyf */
class MtoolClientExceptionCodeTest {

  // 错误码不能重复
  @Test
  void unique() {
    Set<Integer> uniqueSet = new HashSet<>();
    for (MtoolClientExceptionCode exceptionCode : MtoolClientExceptionCode.values()) {
      uniqueSet.add(exceptionCode.getCode());
    }
    assertEquals(MtoolClientExceptionCode.values().length, uniqueSet.size());
  }
}
