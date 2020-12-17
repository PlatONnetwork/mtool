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

/** Created by liyf */
@Parameters(
    commandNames = {AllCommands.SUBMIT_PARAM_PROPOSAL},
    commandDescriptionKey = AllCommands.SUBMIT_PARAM_PROPOSAL)
public class SubmitParamProposalOption extends CommonOption {

  @Parameter(
      names = {AllParams.PID_ID},
      descriptionKey = AllParams.PID_ID,
      required = true,
      arity = 1)
  private String pidId;

  @Parameter(
      names = {AllParams.MODULE},
      descriptionKey = AllParams.MODULE,
      required = true,
      arity = 1)
  private String module;

  @Parameter(
      names = {AllParams.PARAM_NAME},
      descriptionKey = AllParams.PARAM_NAME,
      required = true,
      arity = 1)
  private String paramName;

  @Parameter(
      names = {AllParams.PARAM_VALUE},
      descriptionKey = AllParams.PARAM_VALUE,
      required = true,
      arity = 1)
  private String paramValue;

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

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getParamName() {
    return paramName;
  }

  public void setParamName(String paramName) {
    this.paramName = paramName;
  }

  public String getParamValue() {
    return paramValue;
  }

  public void setParamValue(String paramValue) {
    this.paramValue = paramValue;
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
