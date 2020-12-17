package com.platon.mtool.common.entity;

import com.dtotest.DtoExtension;
import com.dtotest.bind.annotation.DtoPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Created by liyf. */
@ExtendWith(DtoExtension.class)
class DtoTest {

  @Test
  @DtoPackage(
      value = "com.platon.mtool.common.entity",
      ignoreClasses = {RewardConfigTest.class, DtoTest.class})
  void success() {
    Assertions.assertTrue(true);
  }
}
