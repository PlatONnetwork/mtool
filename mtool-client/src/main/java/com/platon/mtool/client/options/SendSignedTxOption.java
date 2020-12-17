package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.PathConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.ValidatorConfig;
import java.nio.file.Path;

/** Created by liyf. */
@Parameters(
    commandNames = {AllCommands.SEND_SIGNEDTX},
    commandDescriptionKey = AllCommands.SEND_SIGNEDTX)
public class SendSignedTxOption extends CommonOption {

  @Parameter(
      names = {AllParams.FILELOCATION},
      descriptionKey = AllParams.FILELOCATION,
      required = true,
      arity = 1,
      converter = PathConverter.class)
  private Path filelocation;

  @Parameter(
      names = {AllParams.CONFIG},
      descriptionKey = AllParams.CONFIG,
      required = true,
      arity = 1,
      converter = ValidatorConfigConverter.class)
  private ValidatorConfig config;

  public Path getFilelocation() {
    return filelocation;
  }

  public void setFilelocation(Path filelocation) {
    this.filelocation = filelocation;
  }

  public ValidatorConfig getConfig() {
    return config;
  }

  public void setConfig(ValidatorConfig config) {
    this.config = config;
  }
}
