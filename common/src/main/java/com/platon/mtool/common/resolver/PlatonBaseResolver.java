package com.platon.mtool.common.resolver;

import java.util.List;
import com.alaya.rlp.solidity.RlpType;

/** Created by liyf. */
public class PlatonBaseResolver implements Resolver<PlatonBaseResolver.PlatonBaseParam> {

  @Override
  public PlatonBaseParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    PlatonBaseParam param = new PlatonBaseParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    return param;
  }

  public static class PlatonBaseParam implements PlatonParameterize {

    private Integer funcType;

    @Override
    public Integer getFuncType() {
      return funcType;
    }

    public void setFuncType(Integer funcType) {
      this.funcType = funcType;
    }
  }
}
