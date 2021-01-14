package com.platon.mtool.common.resolver;

import com.platon.rlp.solidity.RlpType;

import java.util.List;

/** Created by liyf. */
public class UnStakingParamResolver implements Resolver<UnStakingParamResolver.UnStakingParam> {

  @Override
  public UnStakingParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    UnStakingParam param = new UnStakingParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setNodeId(RlpResolverHelp.toString(rlpTypes.get(1)));
    return param;
  }

  public static class UnStakingParam implements PlatonParameterize {

    private Integer funcType;
    private String nodeId;

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
  }
}
