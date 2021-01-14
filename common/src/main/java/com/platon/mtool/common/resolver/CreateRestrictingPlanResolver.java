package com.platon.mtool.common.resolver;

import com.platon.contracts.ppos.dto.RestrictingPlan;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;

import java.util.ArrayList;
import java.util.List;

/** Created by liyf. */
public class CreateRestrictingPlanResolver
    implements Resolver<CreateRestrictingPlanResolver.CreateRestrictingPlanParam> {

  @Override
  public CreateRestrictingPlanParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    CreateRestrictingPlanParam param = new CreateRestrictingPlanParam();
    List<RestrictingPlan> restrictingPlanList = new ArrayList<>();
    param.setRestrictingPlanList(restrictingPlanList);
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setAccount(RlpResolverHelp.toString(rlpTypes.get(1)));
    ((RlpList) RlpDecoder.decode(((RlpString) rlpTypes.get(2)).getBytes()).getValues().get(0))
        .getValues()
        .forEach(
            rl -> {
              RlpList rlpL = (RlpList) rl;
              RestrictingPlan plan =
                  new RestrictingPlan(
                      ((RlpString) rlpL.getValues().get(0)).asPositiveBigInteger(),
                      ((RlpString) rlpL.getValues().get(1)).asPositiveBigInteger());
              restrictingPlanList.add(plan);
            });
    return param;
  }

  public static class CreateRestrictingPlanParam implements PlatonParameterize {

    private Integer funcType;
    private String account;
    private List<RestrictingPlan> restrictingPlanList;

    @Override
    public Integer getFuncType() {
      return funcType;
    }

    public void setFuncType(Integer funcType) {
      this.funcType = funcType;
    }

    public String getAccount() {
      return account;
    }

    public void setAccount(String account) {
      this.account = account;
    }

    public List<RestrictingPlan> getRestrictingPlanList() {
      return restrictingPlanList;
    }

    public void setRestrictingPlanList(List<RestrictingPlan> restrictingPlanList) {
      this.restrictingPlanList = restrictingPlanList;
    }
  }
}
