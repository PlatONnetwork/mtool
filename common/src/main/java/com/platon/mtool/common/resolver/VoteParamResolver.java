package com.platon.mtool.common.resolver;

import com.platon.contracts.ppos.dto.enums.VoteOption;
import com.platon.mtool.common.exception.MtoolException;
import com.platon.rlp.solidity.RlpType;

import java.util.List;

/** Created by liyf. */
public class VoteParamResolver implements Resolver<VoteParamResolver.VoteParam> {

  @Override
  public VoteParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    VoteParam param = new VoteParam();
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setVerifier(RlpResolverHelp.toString(rlpTypes.get(1)));
    param.setProposalId(RlpResolverHelp.toString(rlpTypes.get(2)));
    int optionValue = RlpResolverHelp.toBigInteger(rlpTypes.get(3)).intValue();
    VoteOption voteOption;
    switch (optionValue) {
      case 1:
        voteOption = VoteOption.YEAS;
        break;
      case 2:
        voteOption = VoteOption.NAYS;
        break;
      case 3:
        voteOption = VoteOption.ABSTENTIONS;
        break;
      default:
        throw new MtoolException("invalid option value " + optionValue);
    }

    param.setVoteOption(voteOption);
    return param;
  }

  public static class VoteParam implements PlatonParameterize {

    private Integer funcType;
    private String proposalId;
    private String verifier;
    private VoteOption voteOption;

    public Integer getFuncType() {
      return funcType;
    }

    public void setFuncType(Integer funcType) {
      this.funcType = funcType;
    }

    public String getProposalId() {
      return proposalId;
    }

    public void setProposalId(String proposalId) {
      this.proposalId = proposalId;
    }

    public String getVerifier() {
      return verifier;
    }

    public void setVerifier(String verifier) {
      this.verifier = verifier;
    }

    public VoteOption getVoteOption() {
      return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
      this.voteOption = voteOption;
    }
  }
}
