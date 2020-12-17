package com.platon.mtool.client;

import com.dtotest.DtoExtension;
import com.dtotest.bind.annotation.DtoPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Created by liyf. */
@ExtendWith(DtoExtension.class)
class OptionDtoTest {

  @Test
  @DtoPackage(value = "com.platon.mtool.client.options")
  void success() {
    Assertions.assertTrue(true);
  }
}
