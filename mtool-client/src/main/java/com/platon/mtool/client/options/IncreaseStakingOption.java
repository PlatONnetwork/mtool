package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.KeystoreConverter;
import com.platon.mtool.client.converter.StakingAmountConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.validate.StakingAmountMin;
import com.platon.mtool.common.web3j.Keystore;
import javax.validation.constraints.NotNull;

/** Created by liyf. */
@Parameters(
    commandNames = {AllCommands.INCREASESTAKING},
    commandDescriptionKey = AllCommands.INCREASESTAKING)
public class IncreaseStakingOption extends CommonOption {

  @Parameter(
      names = {AllParams.AMOUNT, AllParams.RESTRICTEDAMOUNT},
      description =
          "increase stake amount, cannot be lower than the operatingThreshold(ATP) of target chain. support "
              + "for using account balances or restricted balances.",
      arity = 1,
      converter = StakingAmountConverter.class,
      required = true)
  @NotNull
  private StakingAmount amount;

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

  public StakingAmount getAmount() {
    return amount;
  }

  public void setAmount(StakingAmount amount) {
    this.amount = amount;
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
