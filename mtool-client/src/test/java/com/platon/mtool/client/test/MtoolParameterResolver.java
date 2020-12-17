package com.platon.mtool.client.test;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.entity.RewardConfigTotal;
import com.platon.mtool.common.entity.ValidatorConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import com.alaya.crypto.CipherException;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;

/** Created by liyf. */
public class MtoolParameterResolver implements ParameterResolver {

  private Map<Class, Object> paramMap = new HashMap<>();

  public MtoolParameterResolver() {
    ValidatorConfig validatorConfig = null;
    Credentials credentials = null;
    RewardConfigTotal rewardConfig = null;
    // language=JSON

    try {
      validatorConfig =
          JSON.parseObject(
              Objects.requireNonNull(
                  ClassLoader.getSystemResourceAsStream("validator_config.json")),
              ValidatorConfig.class);
      credentials =
          WalletUtils.loadCredentials(
              "123456", ClassLoader.getSystemResource("staking.keystore").getPath());
      rewardConfig =
          JSON.parseObject(
              Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("reward_config.json")),
              RewardConfigTotal.class);
    } catch (IOException | CipherException e) {
      e.printStackTrace();
    }
    paramMap.put(ValidatorConfig.class, validatorConfig);
    paramMap.put(Credentials.class, credentials);
    paramMap.put(RewardConfigTotal.class, rewardConfig);
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return paramMap.containsKey(parameterContext.getParameter().getType());
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return paramMap.get(parameterContext.getParameter().getType());
  }
}
