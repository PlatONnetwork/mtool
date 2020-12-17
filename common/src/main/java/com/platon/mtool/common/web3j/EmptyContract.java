package com.platon.mtool.common.web3j;

import com.alaya.contracts.ppos.BaseContract;
import com.alaya.protocol.Web3j;

/**
 * 自定义空合约， 为了使用部分合约方法
 *
 * <p>Created by liyf.
 */
public class EmptyContract extends BaseContract {

  private EmptyContract(String contractAddress, Web3j web3j) {
    super(contractAddress, web3j);
  }

  public static EmptyContract load(Web3j web3j) {
    return new EmptyContract(null, web3j);
  }
}
