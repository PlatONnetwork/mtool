package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.contracts.ppos.dto.enums.VoteOption;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.common.AllParams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Created by liyf. */
class VoteOptionConverterTest {

  private VoteOptionConverter converter = new VoteOptionConverter(AllParams.OPINION);

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

  @Test
  void convert() {
    VoteOption option = converter.convert("yes");
    assertEquals(VoteOption.YEAS, option);
  }

  @Test
  void convertException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> converter.convert("unknow"));
    assertEquals(
        "\"--opinion\": unknow (no such opinion, support input:yes,no,abstain. )",
        exception.getMessage());
  }
}
