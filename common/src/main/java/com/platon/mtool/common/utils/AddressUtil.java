package com.platon.mtool.common.utils;

import com.platon.bech32.Bech32;
import com.platon.crypto.Keys;
import com.platon.crypto.WalletUtils;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.parameters.NetworkParameters;

/**
 * 账户地址格式化工具
 *
 * <p>Created by liyf.
 */
public class AddressUtil {

  private AddressUtil() {}

  public static boolean isValid(String address) {
    return address != null && address.startsWith("0x");
  }

  /**
   * 格式化标准地址0x开头.
   *
   * @param address 地址
   * @return 格式化后地址
   */
  public static String format(String address) {
    if (address == null || address.length() == 0) {
      return address;
    }

    if (address.startsWith("0x")) {
      return address.toLowerCase();
    } else {
      return "0x" + address.toLowerCase();
    }
  }

  public static String getAddress(String nodeId) {
    return "0x" + Keys.getAddress(nodeId);
  }

  /**
   * 根据任意的bech32地址获得目标链的账户的bech32地址
   * @param chainId
   * @param bech32Address
   * @return
   */
  public static String getTargetChainAccountAddress(Long chainId,String bech32Address) {
    if(WalletUtils.isValidAddress(bech32Address)){
        String hexAddress = Bech32.addressDecodeHex(bech32Address);
        // 默认取主链hrp
        String hrp = NetworkParameters.MainNetParams.getHrp();
        if(chainId!=NetworkParameters.MainNetParams.getChainId()){
          // 若链ID不是主链，则取测试链hrp
          hrp = NetworkParameters.TestNetParams.getHrp();
        }
        String targetChainBech32Address = Bech32.addressEncode(hrp,hexAddress);
        return targetChainBech32Address;
    }else{
      throw new MtoolClientException("address ["+bech32Address+"] is not legal bech32 format!");
    }
  }

  /**
   * 判断bech32Address是否是目标链上的合法地址
   * @param chainId
   * @param bech32Address
   * @return
   */
  public static boolean isValidTargetChainAccountAddress(Long chainId,String bech32Address){
    if(!WalletUtils.isValidAddress(bech32Address)){
      return false;
    }
    try {
      String targetChainBech32Address = getTargetChainAccountAddress(chainId,bech32Address);
      if(!bech32Address.equals(targetChainBech32Address)) return false;
    }catch (Exception e){
      return false;
    }
    return true;
  }
}
