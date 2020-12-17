package com.platon.mtool.common.entity;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * qrcode data工具类
 *
 * <p>Created by liyf
 */
public class QrCodeData {
  List<QrCodeTransaction> qrCodeTransactionList;

  long timestamp;

  public List<QrCodeTransaction> getQrCodeTransactionList() {
    return qrCodeTransactionList;
  }

  public void setQrCodeTransactionList(List<QrCodeTransaction> qrCodeTransactionList) {
    this.qrCodeTransactionList = qrCodeTransactionList;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  /** 逻辑从aton拷贝 */
  public String toString() {
    String str = JSON.toJSONString(this);
    // 创建一个新的输出流
    ByteArrayOutputStream out = null;
    // 使用默认缓冲区大小创建新的输出流
    GZIPOutputStream gzip = null;
    try {
      out = new ByteArrayOutputStream();
      gzip = new GZIPOutputStream(out);
      // 将字节写入此输出流
      gzip.write(str.getBytes()); // 因为后台默认字符集有可能是GBK字符集，所以此处需指定一个字符集
      gzip.close();
      // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
      return Base64.encodeBase64String(out.toByteArray());
    } catch (IOException ignored) {
    }
    return "";
  }

  public static class QrCodeTransaction {
    Integer functionType;
    BigInteger amount;
    Long chainId;
    String from;
    String to;
    BigInteger gasPrice;
    BigInteger gasLimit;
    BigInteger nonce;
    String nodeId;

    public Integer getFunctionType() {
      return functionType;
    }

    public void setFunctionType(Integer functionType) {
      this.functionType = functionType;
    }

    public BigInteger getAmount() {
      return amount;
    }

    public void setAmount(BigInteger amount) {
      this.amount = amount;
    }

    public Long getChainId() {
      return chainId;
    }

    public void setChainId(Long chainId) {
      this.chainId = chainId;
    }

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public String getTo() {
      return to;
    }

    public void setTo(String to) {
      this.to = to;
    }

    public BigInteger getGasPrice() {
      return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
      this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
      return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
      this.gasLimit = gasLimit;
    }

    public BigInteger getNonce() {
      return nonce;
    }

    public void setNonce(BigInteger nonce) {
      this.nonce = nonce;
    }

    public String getNodeId() {
      return nodeId;
    }

    public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
    }
  }
}
