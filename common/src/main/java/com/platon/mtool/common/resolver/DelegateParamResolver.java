package com.platon.mtool.common.resolver;

import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.rlp.solidity.RlpType;

import java.math.BigInteger;
import java.util.List;

/** Created by liyf. */
public class DelegateParamResolver implements Resolver<DelegateParamResolver.DelegateParam> {

  @Override
  public DelegateParamResolver.DelegateParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    DelegateParam param = new DelegateParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setStakingAmountType(
        RlpResolverHelp.toBigInteger(rlpTypes.get(1)).intValue() == 0
            ? StakingAmountType.FREE_AMOUNT_TYPE
            : StakingAmountType.RESTRICTING_AMOUNT_TYPE);
    param.setNodeId(RlpResolverHelp.toString(rlpTypes.get(2)));
    param.setAmount(RlpResolverHelp.toBigInteger(rlpTypes.get(3)));
    return param;
  }

  public static class DelegateParam implements PlatonParameterize {

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
