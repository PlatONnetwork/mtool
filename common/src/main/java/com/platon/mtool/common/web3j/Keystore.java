package com.platon.mtool.common.web3j;

import com.alaya.crypto.Address;
import com.alaya.crypto.Credentials;

/**
 * 钱包model
 *
 * <p>Created by liyf.
 */
public class Keystore {

  private Address address;
  private Credentials credentials;
  private Type type;
  private String filepath;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Credentials getCredentials() {
    return credentials;
  }

  public void setCredentials(Credentials credentials) {
    this.credentials = credentials;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  public enum Type {
    // 普通钱包， 观察钱包
    NORMAL,
    OBSERVE
  }
}
