package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.*;
import com.platon.mtool.client.options.delegate.GasProviderDelegate;
import com.platon.mtool.common.AllCommands.Tx;
import com.platon.mtool.common.AllModules;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.validate.StakingAmountMin;
import com.platon.mtool.common.web3j.Keystore;
import com.platon.tx.gas.GasProvider;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Created by liyf */
@Parameters(
    commandNames = {AllModules.TX},
    commandDescriptionKey = AllModules.TX)
public class TxOptions extends CommonOption {
  private TransferOption transferOption;
  private DelegateOption delegateOption;

  public TxOptions() {
    this.transferOption = new TransferOption();
    this.delegateOption = new DelegateOption();
  }

  public static class BaseTxOption extends CommonOption {}

  @Parameters(
      commandNames = {Tx.DELEGATE},
      commandDescriptionKey = AllModules.TX + Tx.DELEGATE)
  public static class DelegateOption extends BaseTxOption {
    @Parameter(
        names = {AllParams.AMOUNT, AllParams.RESTRICTEDAMOUNT, AllParams.AUTO_AMOUNT},
        description = "Send amount",
        arity = 1,
        converter = StakingAmountConverter.class,
        required = true)
    @StakingAmountMin(
        value = "10000000000000000000",
        message = "The delegate amount cannot be lower than 10LAT")
    @NotNull
    private StakingAmount amount;

    @Parameter(
        names = {AllParams.KEYSTORE, AllParams.ADDRESS},
        description = "Send account keystore",
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
        names = {"--node_id"},
        description = "Nodeid,hex 0x begins",
        required = true,
        arity = 1)
    private String nodeId;

    public StakingAmount getAmount() {
      return amount;
    }

    public Keystore getKeystore() {
      return keystore;
    }

    public ValidatorConfig getConfig() {
      return config;
    }

    public String getNodeId() {
      return nodeId;
    }

    public void setAmount(StakingAmount amount) {
      this.amount = amount;
    }

    public void setKeystore(Keystore keystore) {
      this.keystore = keystore;
    }

    public void setConfig(ValidatorConfig config) {
      this.config = config;
    }

    public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
    }
  }

  @Parameters(
      commandNames = {Tx.TRANSFER},
      commandDescriptionKey = AllModules.TX + Tx.TRANSFER)
  public static class TransferOption extends BaseTxOption {
    @Parameter(
        names = {"--recipient"},
        description = "Receiving account address",
        arity = 1,
        converter = AddressConverter.class,
        required = true)
    private String to;

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
        names = {AllParams.AMOUNT},
        description = "Send amount",
        arity = 1,
        converter = BigDecimalConverter.class,
        required = true)
    @NotNull
    @DecimalMin("0.000000000000000001")
    private BigDecimal lat;

    @ParametersDelegate @Valid private GasProvider gasProvider = new GasProviderDelegate();

    public String getTo() {
      return to;
    }

    public Keystore getKeystore() {
      return keystore;
    }

    public ValidatorConfig getConfig() {
      return config;
    }

    public BigDecimal getLat() {
      return lat;
    }

    public GasProvider getGasProvider() {
      return gasProvider;
    }

    public void setKeystore(Keystore keystore) {
      this.keystore = keystore;
    }

    public void setConfig(ValidatorConfig config) {
      this.config = config;
    }

    public void setLat(BigDecimal lat) {
      this.lat = lat;
    }

    public void setGasProvider(GasProvider gasProvider) {
      this.gasProvider = gasProvider;
    }

    public void setTo(String to) {
      this.to = to;
    }
  }

  public TransferOption getTransferOption() {
    return transferOption;
  }

  public DelegateOption getDelegateOption() {
    return delegateOption;
  }
}
