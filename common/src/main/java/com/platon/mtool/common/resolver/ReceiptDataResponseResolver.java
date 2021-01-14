package com.platon.mtool.common.resolver;

import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** Created by liyf. */
public class ReceiptDataResponseResolver {

  private static final Logger logger = LoggerFactory.getLogger(ReceiptDataResponseResolver.class);

  public ReceiptDataResponse resolv(String input) {
    if (input.startsWith("0x")) {
      input = input.substring(2);
    }
    ReceiptDataResponse response = new ReceiptDataResponse();
    try {
      RlpList b = RlpDecoder.decode(Hex.decodeHex(input));
      RlpList group = (RlpList) b.getValues().get(0);
      List<RlpType> rlpList = group.getValues();
      String code = new String(((RlpString) rlpList.get(0)).getBytes());
      response.setData(code);
      response.setStatus("0".equals(code));
    } catch (Exception e) {
      logger.error("resolv receip error", e);
    }
    return response;
  }

  public static class ReceiptDataResponse {

    private Boolean status;
    private String errMsg;
    private String data;

    public Boolean getStatus() {
      return status;
    }

    public void setStatus(Boolean status) {
      this.status = status;
    }

    public String getErrMsg() {
      return errMsg;
    }

    public void setErrMsg(String errMsg) {
      this.errMsg = errMsg;
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }
  }
}
