package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.AddressConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.common.AllCommands.Account;
import com.platon.mtool.common.AllModules;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.ValidatorConfig;

/**
 * 转账选项，模块命令包含子选项
 * Created by liyf */
@Parameters(
    commandNames = {AllModules.ACCOUNT},
    commandDescriptionKey = AllModules.ACCOUNT)
public class AccountOptions extends CommonOption {

  private NewOption newOption;
  private RecoverOption recoverOption;
  private ListOption listOption;
  private BalanceOption balanceOption;

  public AccountOptions() {
    this.newOption = new NewOption();
    this.recoverOption = new RecoverOption();
    this.listOption = new ListOption();
    this.balanceOption = new BalanceOption();
  }

  public NewOption getNewOption() {
    return newOption;
  }

  public RecoverOption getRecoverOption() {
    return recoverOption;
  }

  public ListOption getListOption() {
    return listOption;
  }

  public BalanceOption getBalanceOption() {
    return balanceOption;
  }

  public static class BaseAccountOption extends CommonOption {}

  /**
   * 创建钱包
   */
  @Parameters(
      commandNames = {Account.NEW},
      commandDescriptionKey = AllModules.ACCOUNT + Account.NEW)
  public static class NewOption extends BaseAccountOption {
    @Parameter(description = "accountName")
    private String name;

    public String getName() {
      return name;
    }
  }

  /**
   * 恢复钱包
   */
  @Parameters(
      commandNames = {Account.RECOVER},
      commandDescriptionKey = AllModules.ACCOUNT + Account.RECOVER)
  public static class RecoverOption extends BaseAccountOption {
    @Parameter(
        names = {"-k", "--key"},
        description = "Account recovery with private key")
    private boolean isPrivateKey;

    @Parameter(
        names = {"-m", "--mnemonics"},
        description = "Account recovery with mnemonics")
    private boolean isMnemonics;

    @Parameter(description = "accountName")
    private String name;

    public String getName() {
      return name;
    }

    public boolean isPrivateKey() {
      return isPrivateKey;
    }

    public boolean isMnemonics() {
      return isMnemonics;
    }
  }

  /**
   * 钱包列表
   */
  @Parameters(
      commandNames = {Account.LIST},
      commandDescriptionKey = AllModules.ACCOUNT + Account.LIST)
  public static class ListOption extends BaseAccountOption {}

  /**
   * 钱包余额
   */
  @Parameters(
      commandNames = {Account.BALANCE},
      commandDescriptionKey = AllModules.ACCOUNT + Account.BALANCE)
  public static class BalanceOption extends BaseAccountOption {
    @Parameter(
        names = {"-a", "--address"},
        converter = AddressConverter.class,
        description = "address")
    private String address;

    @Parameter(description = "keystorename")
    private String keystoreName;

    @Parameter(
        names = {AllParams.CONFIG},
        descriptionKey = AllParams.CONFIG,
        required = true,
        arity = 1,
        converter = ValidatorConfigConverter.class)
    private ValidatorConfig config;

    public String getAddress() {
      return address;
    }

    public ValidatorConfig getConfig() {
      return config;
    }

    public String getKeystoreName() {
      return keystoreName;
    }
  }
}
