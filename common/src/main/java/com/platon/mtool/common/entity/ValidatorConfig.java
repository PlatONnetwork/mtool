package com.platon.mtool.common.entity;

import com.platon.mtool.common.validate.UpdateValidatorChecks;
import org.hibernate.validator.constraints.URL;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;

/**
 * 验证人配置文件
 *
 * <p>节点公钥、节点IP、端口及RPC端口 验证人名称、身份认证ID、官网、验证人奖励接收钱包地址、简介.
 *
 * <p>Created by liyf.
 */
@GroupSequence({UpdateValidatorChecks.class, ValidatorConfig.class})
public class ValidatorConfig {
  @NotEmpty
  @Size(min = 128, max = 128, message = "size must be equal to 128")
  private String nodePublicKey;

  @NotEmpty @URL private String nodeAddress;

  @NotNull
  @Min(1)
  @Max(99_999_999)
  private Integer nodePort;

  @NotNull
  @Max(99_999_999)
  private Integer nodeRpcPort;

  @NotEmpty
  @Size(min = 192, max = 192, message = "size must be equal to 192")
  private String blsPubKey;

  private String certificate;

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


  public String getBlsPubKey() {
    return blsPubKey;
  }

  public void setBlsPubKey(String blsPubKey) {
    this.blsPubKey = blsPubKey;
  }

  public String getCertificate() {
    return certificate;
  }

  public void setCertificate(String certificate) {
    this.certificate = certificate;
  }
}
