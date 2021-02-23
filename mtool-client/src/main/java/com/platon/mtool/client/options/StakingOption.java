package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.KeystoreConverter;
import com.platon.mtool.client.converter.StakingAmountConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.client.options.validator.AddressValidator;
import com.platon.mtool.client.options.validator.DelegateRewardPercentValidator;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.web3j.Keystore;

import javax.validation.constraints.NotNull;

/** Created by liyf. */
@Parameters(
    commandNames = {AllCommands.STAKING},
    commandDescriptionKey = AllCommands.STAKING)
public class StakingOption extends CommonOption {

  @Parameter(
      names = {AllParams.AMOUNT, AllParams.RESTRICTEDAMOUNT, AllParams.AUTO_AMOUNT},
      description =
          "stake amount, cannot be lower than the stakeThreshold(LAT) of target chain. "
              + "Support for using account balances or account restricted balances.",
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

  @Parameter(
          names = {"--node_name"},
          description = "node name. less than 30 bytes.",
          arity = 1,
          required = true)
  private String nodeName;

  @Parameter(
          names = {"--website"},
          description = "node website URL. less than 140 bytes.",
          arity = 1,
          required = true)
  private String website;

  @Parameter(
          names = {"--details"},
          description = "validator description (in English recommended), less than 280 bytes.",
          arity = 1,
          required = true)
  private String details;

  @Parameter(
          names = {"--external_id"},
          description = "third party ID. such as public key base64 encoded. less than 70 bytes.",
          arity = 1,
          required = true)
  private String externalId;

  @Parameter(
          names = {"--benefit_address"},
          description = "validator's wallet address to receive reward.",
          arity = 1,
          validateWith = AddressValidator.class,
          required = true)
  private String benefitAddress;

  @Parameter(
          names = {"--delegated_reward_rate"},
          description = "Delegated reward ratio, range [0, 10000], unit: ‱, 500 meaning 5%",
          arity = 1,
          validateWith = DelegateRewardPercentValidator.class,
          required = true)
  private int delegateRewardPercent;


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

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public String getBenefitAddress() {
    return benefitAddress;
  }

  public void setBenefitAddress(String benefitAddress) {
    this.benefitAddress = benefitAddress;
  }

  public int getDelegateRewardPercent() {
    return delegateRewardPercent;
  }

  public void setDelegateRewardPercent(int delegateRewardPercent) {
    this.delegateRewardPercent = delegateRewardPercent;
  }
}
