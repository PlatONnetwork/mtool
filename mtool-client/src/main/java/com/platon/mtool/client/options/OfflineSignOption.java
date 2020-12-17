package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.PathConverter;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllParams;
import java.nio.file.Path;

/** Created by liyf. */
@Parameters(
    commandNames = {AllCommands.OFFLINESIGN},
    commandDescriptionKey = AllCommands.OFFLINESIGN)
public class OfflineSignOption extends CommonOption {

  @Parameter(
      names = {AllParams.FILELOCATION},
      descriptionKey = AllParams.FILELOCATION,
      required = true,
      arity = 1,
      converter = PathConverter.class)
  private Path filelocation;

  public Path getFilelocation() {
    return filelocation;
  }

  public void setFilelocation(Path filelocation) {
    this.filelocation = filelocation;
  }
}
