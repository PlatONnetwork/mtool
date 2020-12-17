package com.platon.mtool.common.entity;

/** Created by liyf. */
public class AdditionalInfo {

  private String chainId;
  private String nodeName;
  private String nodePublicKey;
  private String nodeAddress;
  private Integer nodePort;
  private Integer nodeRpcPort;
  private String webSite;
  // 参数治理提案新增
  private String module;
  private String paramName;
  private String paramValue;

  public String getChainId() {
    return chainId;
  }

  public void setChainId(String chainId) {
    this.chainId = chainId;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getNodePublicKey() {
    return nodePublicKey;
  }

  public void setNodePublicKey(String nodePublicKey) {
    this.nodePublicKey = nodePublicKey;
  }

  public String getNodeAddress() {
    return nodeAddress;
  }

  public void setNodeAddress(String nodeAddress) {
    this.nodeAddress = nodeAddress;
  }

  public Integer getNodePort() {
    return nodePort;
  }

  public void setNodePort(Integer nodePort) {
    this.nodePort = nodePort;
  }

  public Integer getNodeRpcPort() {
    return nodeRpcPort;
  }

  public void setNodeRpcPort(Integer nodeRpcPort) {
    this.nodeRpcPort = nodeRpcPort;
  }

  public String getWebSite() {
    return webSite;
  }

  public void setWebSite(String webSite) {
    this.webSite = webSite;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getParamName() {
    return paramName;
  }

  public void setParamName(String paramName) {
    this.paramName = paramName;
  }

  public String getParamValue() {
    return paramValue;
  }

  public void setParamValue(String paramValue) {
    this.paramValue = paramValue;
  }
}
