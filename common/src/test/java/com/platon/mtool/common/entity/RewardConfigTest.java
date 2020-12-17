package com.platon.mtool.common.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.enums.FeePayerEnum;
import com.platon.mtool.common.enums.RewardMethod;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class RewardConfigTest {

  private static RewardConfigTotal rewardConfig;

  @BeforeAll
  static void before() {
    rewardConfig = getConfig();
  }

  private static RewardConfigTotal getConfig() {
    RewardConfigTotal rewardConfig = new RewardConfigTotal();
    RewardConfigDetail staking = new RewardConfigDetail();
    RewardConfigDetail block = new RewardConfigDetail();
    RewardConfigDetail trade = new RewardConfigDetail();
    rewardConfig.setStaking(staking);
    rewardConfig.setBlock(block);
    rewardConfig.setTrade(trade);
    rewardConfig.setFeePayer(FeePayerEnum.DELEGATOR);
    staking.setRewardMethod(RewardMethod.AVERAGE);
    block.setRewardMethod(RewardMethod.AVERAGE);
    trade.setRewardMethod(RewardMethod.AVERAGE);
    staking.setCommissionRatio(0.1F);
    block.setCommissionRatio(0.2F);
    trade.setCommissionRatio(0.3F);
    return rewardConfig;
  }

  @Test
  void fromJson() throws IOException {
    RewardConfigTotal rewardConfig =
        JSON.parseObject(
            Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("reward_config.json")),
            RewardConfigTotal.class);
    assertTrue(
        EqualsBuilder.reflectionEquals(rewardConfig.getStaking(), rewardConfig.getStaking()));
    assertTrue(EqualsBuilder.reflectionEquals(rewardConfig.getBlock(), rewardConfig.getBlock()));
    assertTrue(EqualsBuilder.reflectionEquals(rewardConfig.getTrade(), rewardConfig.getTrade()));
    assertTrue(
        EqualsBuilder.reflectionEquals(rewardConfig.getFeePayer(), rewardConfig.getFeePayer()));
  }

  @Test
  void toJson() throws IOException {
    String configStr = JSON.toJSONString(rewardConfig);
    String expect =
        JSON.toJSONString(
            JSON.parseObject(
                Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("reward_config.json")),
                RewardConfigTotal.class));
    assertEquals(expect, configStr);
  }
}
