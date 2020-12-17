package com.platon.mtool.common.resolver;

import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.rlp.solidity.RlpType;

import java.math.BigInteger;
import java.util.List;


/** Created by liyf. */
public class AddStakingParamResolver implements Resolver<AddStakingParamResolver.AddStakingParam> {

  @Override
  public AddStakingParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    AddStakingParam param = new AddStakingParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setNodeId(RlpResolverHelp.toString(rlpTypes.get(1)));

    param.setStakingAmountType(
        RlpResolverHelp.toBigInteger(rlpTypes.get(2)).intValue() == 0
            ? StakingAmountType.FREE_AMOUNT_TYPE
            : StakingAmountType.RESTRICTING_AMOUNT_TYPE);
    param.setAmount(RlpResolverHelp.toBigInteger(rlpTypes.get(3)));
    return param;
  }

  public static class AddStakingParam implements PlatonParameterize {

    private Integer funcType;
    private String nodeId;
    private StakingAmountType stakingAmountType;
    private BigInteger amount;

    public Integer getFuncType() {
      return funcType;
    }

    public void setFuncType(Integer funcType) {
      this.funcType = funcType;
    }

    public String getNodeId() {
      return nodeId;
    }

    public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
    }

    public StakingAmountType getStakingAmountType() {
      return stakingAmountType;
    }

    public void setStakingAmountType(StakingAmountType stakingAmountType) {
      this.stakingAmountType = stakingAmountType;
    }

    public BigInteger getAmount() {
      return amount;
    }

    public void setAmount(BigInteger amount) {
      this.amount = amount;
    }
  }
}
