package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.KeystoreConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.web3j.Keystore;

/** Created by liyf. */
@Parameters(
    commandNames = {AllCommands.SUBMIT_TEXT_PROPOSAL},
    commandDescriptionKey = AllCommands.SUBMIT_TEXT_PROPOSAL)
public class SubmitTextProposalOption extends CommonOption {

  @Parameter(
      names = {AllParams.PID_ID},
      descriptionKey = AllParams.PID_ID,
      required = true,
      arity = 1)
  private String pidId;

  @Parameter(
      names = {AllParams.KEYSTORE, AllParams.ADDRESS},
      descriptionKey = AllParams.KEYSTORE + AllParams.ADDRESS,
      required = true,
      arity = 1,
      converter = KeystoreConverter.class)
  private Keystore keystore;

  @Parameter(
      names = {AllParams.CONFIG},
      descriptionKey = AllParams.CONFIG,
      required = true,
      arity = 1,
      converter = ValidatorConfigConverter.class)
  private ValidatorConfig config;

  public String getPidId() {
    return pidId;
  }

  public void setPidId(String pidId) {
    this.pidId = pidId;
  }

  public Keystore getKeystore() {
    return keystore;
  }

  public void setKeystore(Keystore keystore) {
    this.keystore = keystore;
  }

  public ValidatorConfig getConfig() {
    return config;
  }

  public void setConfig(ValidatorConfig config) {
    this.config = config;
  }
}
