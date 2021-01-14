package com.platon.mtool.common.resolver;

import com.platon.mtool.common.exception.MtoolException;
import com.platon.rlp.solidity.RlpType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.List;

/** Created by liyf. */
public class UpdateStakingInfoParamResolver
    implements Resolver<UpdateStakingInfoParamResolver.UpdateStakingInfoParam> {

  @Override
  public UpdateStakingInfoParam resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }

    List<RlpType> rlpTypes = RlpResolverHelp.toRlpTypeList(input);
    UpdateStakingInfoParam param = new UpdateStakingInfoParam();
    param.setFuncType(RlpResolverHelp.toBigInteger(rlpTypes.get(0)).intValue());
    param.setBenefitAddress(RlpResolverHelp.toString(rlpTypes.get(1)));
    param.setNodeId(RlpResolverHelp.toString(rlpTypes.get(2)));
    try {
      String externalIdUtf8 = RlpResolverHelp.toString(rlpTypes.get(3));
      param.setExternalId(new String(Hex.decodeHex(externalIdUtf8.substring(2))));
      String webSiteUtf8 = RlpResolverHelp.toString(rlpTypes.get(4));
      param.setWebSite(new String(Hex.decodeHex(webSiteUtf8.substring(2))));
      String detailsUtf8 = RlpResolverHelp.toString(rlpTypes.get(5));
      param.setDetails(new String(Hex.decodeHex(detailsUtf8.substring(2))));
    } catch (DecoderException e) {
      throw new MtoolException("rlp parse error", e);
    }
    return param;
  }

  public static class UpdateStakingInfoParam implements PlatonParameterize {

    private Integer funcType;
    private String nodeId;
    private String benefitAddress;
    private String externalId;
    private String nodeName;
    private String webSite;
    private String details;

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
  }
}
