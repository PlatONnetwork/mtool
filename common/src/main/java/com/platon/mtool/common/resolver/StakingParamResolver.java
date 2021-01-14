package com.platon.mtool.common.resolver;

import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.mtool.common.exception.MtoolException;
import com.platon.rlp.solidity.RlpType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.List;

/** Created by liyf. */
public class StakingParamResolver implements Resolver<StakingParamResolver.StakingParam> {

  @Override
  public StakingParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    StakingParam param = new StakingParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setStakingAmountType(
        RlpResolverHelp.toBigInteger(rlpTypes.get(1)).intValue() == 0
            ? StakingAmountType.FREE_AMOUNT_TYPE
            : StakingAmountType.RESTRICTING_AMOUNT_TYPE);
    param.setBenefitAddress(RlpResolverHelp.toString(rlpTypes.get(2)));
    param.setNodeId(RlpResolverHelp.toString(rlpTypes.get(3)));
    try {
      String externalIdUtf8 = RlpResolverHelp.toString(rlpTypes.get(4));
      param.setExternalId(new String(Hex.decodeHex(externalIdUtf8.substring(2))));
      String nodeNameUtf8 = RlpResolverHelp.toString(rlpTypes.get(5));
      param.setNodeName(new String(Hex.decodeHex(nodeNameUtf8.substring(2))));
      String webSiteUtf8 = RlpResolverHelp.toString(rlpTypes.get(6));
      param.setWebSite(new String(Hex.decodeHex(webSiteUtf8.substring(2))));
      String detailsUtf8 = RlpResolverHelp.toString(rlpTypes.get(7));
      param.setDetails(new String(Hex.decodeHex(detailsUtf8.substring(2))));
    } catch (DecoderException e) {
      throw new MtoolException("rlp parse error", e);
    }
    param.setAmount(RlpResolverHelp.toBigInteger(rlpTypes.get(8)));
    param.setProcessVersion(RlpResolverHelp.toBigInteger(rlpTypes.get(9)));
    return param;
  }

  public static class StakingParam implements PlatonParameterize {

    private Integer funcType;
    private StakingAmountType stakingAmountType;
    private String benefitAddress;
    private String nodeId;
    private String externalId;
    private String nodeName;
    private String webSite;
    private String details;
    private BigInteger amount;
    private BigInteger processVersion;

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

    public BigInteger getAmount() {
      return amount;
    }

    public void setAmount(BigInteger amount) {
      this.amount = amount;
    }

    public StakingAmountType getStakingAmountType() {
      return stakingAmountType;
    }

    public void setStakingAmountType(StakingAmountType stakingAmountType) {
      this.stakingAmountType = stakingAmountType;
    }

    public String getBenefitAddress() {
      return benefitAddress;
    }

    public void setBenefitAddress(String benefitAddress) {
      this.benefitAddress = benefitAddress;
    }

    public String getExternalId() {
      return externalId;
    }

    public void setExternalId(String externalId) {
      this.externalId = externalId;
    }

    public String getNodeName() {
      return nodeName;
    }

    public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
    }

    public String getWebSite() {
      return webSite;
    }

    public void setWebSite(String webSite) {
      this.webSite = webSite;
    }

    public String getDetails() {
      return details;
    }

    public void setDetails(String details) {
      this.details = details;
    }

    public BigInteger getProcessVersion() {
      return processVersion;
    }

    public void setProcessVersion(BigInteger processVersion) {
      this.processVersion = processVersion;
    }
  }
}
