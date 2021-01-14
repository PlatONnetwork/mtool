package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.contracts.ppos.dto.enums.VoteOption;

/**
 * 投票选项转换器
 *
 * <p>Created by liyf.
 */
public class VoteOptionConverter extends BaseConverter<VoteOption> {

  private static final String[] options = new String[] {"yes", "no", "abstain"};
  private static final VoteOption[] voteOptions =
      new VoteOption[] {VoteOption.YEAS, VoteOption.NAYS, VoteOption.ABSTENTIONS};

  public VoteOptionConverter(String optionName) {
    super(optionName);
  }

  @Override
  public VoteOption convert(String value) {
    for (int i = 0; i < options.length; i++) {
      if (options[i].equals(value)) {
        return voteOptions[i];
      }
    }
    throw new ParameterException(
        getErrorString(value, "no such opinion, support input:yes,no,abstain."));
  }

  @Override
  protected String getErrorString(String value, String to) {
    return "\"" + getOptionName() + "\": " + value + " (" + to + " )";
  }
}
