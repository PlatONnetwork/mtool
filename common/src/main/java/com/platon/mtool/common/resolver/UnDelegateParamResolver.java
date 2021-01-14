package com.platon.mtool.common.resolver;

import com.platon.rlp.solidity.RlpType;

import java.math.BigInteger;
import java.util.List;

/** Created by liyf. */
public class UnDelegateParamResolver implements Resolver<UnDelegateParamResolver.UnDelegateParam> {

  @Override
  public UnDelegateParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    UnDelegateParamResolver.UnDelegateParam param = new UnDelegateParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setStakingBlockNum(RlpResolverHelp.toBigInteger(rlpTypes.get(1)));
    param.setNodeId(RlpResolverHelp.toString(rlpTypes.get(2)));
    param.setAmount(RlpResolverHelp.toBigInteger(rlpTypes.get(3)));
    return param;
  }

  public static class UnDelegateParam implements PlatonParameterize {

    private Integer funcType;
    private String nodeId;
    private BigInteger stakingBlockNum;
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

    public BigInteger getStakingBlockNum() {
      return stakingBlockNum;
    }

    public void setStakingBlockNum(BigInteger stakingBlockNum) {
      this.stakingBlockNum = stakingBlockNum;
    }

    public BigInteger getAmount() {
      return amount;
    }

    public void setAmount(BigInteger amount) {
      this.amount = amount;
    }
  }
}
