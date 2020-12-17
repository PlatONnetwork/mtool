package com.platon.mtool.common.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson.JSON;
import com.alaya.contracts.ppos.dto.enums.VoteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class ResolvTest {

  private static Path resourceDirectory = Paths.get("src", "test", "resources");

  private String getInput(String filename) throws IOException {
    Path filepath = resourceDirectory.resolve(filename);
    return Files.newBufferedReader(filepath).readLine();
  }

  @Test
  void baseResolv() throws IOException {
    String s = getInput("platondata/input/staking.txt");
    PlatonBaseResolver baseResolver = new PlatonBaseResolver();
    PlatonParameterize param = baseResolver.resolv(s);
    assertEquals(1000, param.getFuncType());
  }

  @Test
  void resolvStaking() throws IOException {
    String s = getInput("platondata/input/staking.txt");
    StakingParamResolver resolver = new StakingParamResolver();
    StakingParamResolver.StakingParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertNotNull(param.getStakingAmountType());
    assertEquals(1000, param.getFuncType());
  }

  @Test
  void resolvReceipt() {
    String s = "0xc130";
    ReceiptDataResponseResolver resolver = new ReceiptDataResponseResolver();
    ReceiptDataResponseResolver.ReceiptDataResponse response = resolver.resolv(s);
    System.out.println(JSON.toJSONString(response));
    assertTrue(response.getStatus());

    s = "0xc786333031303031";
    response = resolver.resolv(s);
    System.out.println(JSON.toJSONString(response));
    assertFalse(response.getStatus());
    assertEquals("301001", response.getData());
  }

  @Test
  void resolvDelegate() throws IOException {
    String s = getInput("platondata/input/delegate.txt");
    DelegateParamResolver resolver = new DelegateParamResolver();
    DelegateParamResolver.DelegateParam param = resolver.resolv(s);
    assertNotNull(param.getStakingAmountType());
    assertEquals(1004, param.getFuncType());
  }

  @Test
  void resolvAddStaking() throws IOException {
    String s = getInput("platondata/input/addStaking.txt");
    AddStakingParamResolver resolver = new AddStakingParamResolver();
    AddStakingParamResolver.AddStakingParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertNotNull(param.getStakingAmountType());
    assertEquals(1002, param.getFuncType());
  }

  @Test
  void resolvCreateRestrictingPlan() throws IOException {
    String s = getInput("platondata/input/create_restricting_plan.txt");
    CreateRestrictingPlanResolver resolver = new CreateRestrictingPlanResolver();
    CreateRestrictingPlanResolver.CreateRestrictingPlanParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertNotNull(param.getAccount());
    assertEquals(4000, param.getFuncType());
  }

  @Test
  void resolvUpdateStakingInfo() throws IOException {
    String s = getInput("platondata/input/update_staking_info.txt");
    UpdateStakingInfoParamResolver resolver = new UpdateStakingInfoParamResolver();
    UpdateStakingInfoParamResolver.UpdateStakingInfoParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertEquals(1001, param.getFuncType());
  }

  @Test
  void resolvUnDelegate() throws IOException {
    String s = getInput("platondata/input/undelegate.txt");
    UnDelegateParamResolver resolver = new UnDelegateParamResolver();
    UnDelegateParamResolver.UnDelegateParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertEquals(1005, param.getFuncType());
  }

  @Test
  void resolvUnStaking() throws IOException {
    String s = getInput("platondata/input/unstaking.txt");
    UnStakingParamResolver resolver = new UnStakingParamResolver();
    UnStakingParamResolver.UnStakingParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertEquals(1003, param.getFuncType());
  }

  @Test
  void resolvVote() throws IOException {
    String s = getInput("platondata/input/vote.txt");
    VoteParamResolver resolver = new VoteParamResolver();
    VoteParamResolver.VoteParam param = resolver.resolv(s);
    System.out.println(JSON.toJSONString(param));
    assertEquals(VoteOption.YEAS, param.getVoteOption());
    assertEquals(2003, param.getFuncType());
  }
}
