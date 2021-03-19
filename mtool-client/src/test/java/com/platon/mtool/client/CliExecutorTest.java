package com.platon.mtool.client;

import com.platon.mtool.client.execute.IncreaseStakingExecutor;
import com.platon.mtool.client.execute.StakingExecutor;
import com.platon.mtool.client.options.IncreaseStakingOption;
import com.platon.mtool.client.options.StakingOption;
import com.platon.mtool.client.parser.BaseOptionParser;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.utils.PlatOnUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Created by liyf. */
class CliExecutorTest {

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

  @Test
  void showHelp() {
    BaseOptionParser parser = new BaseOptionParser("mtool-test");
    CliExecutor<StakingOption> executor = new StakingExecutor(parser.getjCommander(), null);
    executor.help();
    assertTrue(true);
  }

  @Test
  void isStakingOptionValid() {
    StakingAmount stakingAmount = new StakingAmount();
    stakingAmount.setAmount(PlatOnUnit.latToVon(new BigInteger("1000000")));

    StakingOption option = new StakingOption();
    option.setAmount(stakingAmount);
    CliExecutor<StakingOption> executor = new StakingExecutor(null, option);
    assertSame(true, executor.isOptionValid(option));
  }

  @Test
  void increaseStakingOptionValid() {
    StakingAmount stakingAmount = new StakingAmount();
    stakingAmount.setAmount(PlatOnUnit.latToVon(new BigInteger("10")));

    IncreaseStakingOption option = new IncreaseStakingOption();
    option.setAmount(stakingAmount);
    CliExecutor<IncreaseStakingOption> executor = new IncreaseStakingExecutor(null, option);
    assertSame(true, executor.isOptionValid(option));
  }

}
