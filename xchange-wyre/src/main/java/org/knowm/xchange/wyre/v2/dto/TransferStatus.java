package org.knowm.xchange.wyre.v2.dto;

import java.math.BigDecimal;
import java.util.Map;

public class TransferStatus {
  private String id;
  private String status;
  private String failureReason;
  private String language;
  private Long createdAt;
  private Long completedAt;
  private Long depositInitiatedAt;
  private Long invalidatedAt;
  private Long expiresAt;
  private Long estimatedArrival;
  private String owner;
  private String source;
  private String dest;
  private String sourceCurrency;
  private BigDecimal sourceAmount;
  private String destCurrency;
  private BigDecimal destAmount;
  private BigDecimal exchangeRate;
  private String desc;
  private BigDecimal totalFees;
  private Map<String, BigDecimal> equivalencies;
  private Map<String, BigDecimal> feeEquivalencies;
  private Map<String, BigDecimal> fees;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public Long getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(Long completedAt) {
    this.completedAt = completedAt;
  }

  public Long getDepositInitiatedAt() {
    return depositInitiatedAt;
  }

  public void setDepositInitiatedAt(Long depositInitiatedAt) {
    this.depositInitiatedAt = depositInitiatedAt;
  }

  public Long getInvalidatedAt() {
    return invalidatedAt;
  }

  public void setInvalidatedAt(Long invalidatedAt) {
    this.invalidatedAt = invalidatedAt;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Long expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Long getEstimatedArrival() {
    return estimatedArrival;
  }

  public void setEstimatedArrival(Long estimatedArrival) {
    this.estimatedArrival = estimatedArrival;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDest() {
    return dest;
  }

  public void setDest(String dest) {
    this.dest = dest;
  }

  public String getSourceCurrency() {
    return sourceCurrency;
  }

  public void setSourceCurrency(String sourceCurrency) {
    this.sourceCurrency = sourceCurrency;
  }

  public BigDecimal getSourceAmount() {
    return sourceAmount;
  }

  public void setSourceAmount(BigDecimal sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  public String getDestCurrency() {
    return destCurrency;
  }

  public void setDestCurrency(String destCurrency) {
    this.destCurrency = destCurrency;
  }

  public BigDecimal getDestAmount() {
    return destAmount;
  }

  public void setDestAmount(BigDecimal destAmount) {
    this.destAmount = destAmount;
  }

  public BigDecimal getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public BigDecimal getTotalFees() {
    return totalFees;
  }

  public void setTotalFees(BigDecimal totalFees) {
    this.totalFees = totalFees;
  }

  public Map<String, BigDecimal> getEquivalencies() {
    return equivalencies;
  }

  public void setEquivalencies(Map<String, BigDecimal> equivalencies) {
    this.equivalencies = equivalencies;
  }

  public Map<String, BigDecimal> getFeeEquivalencies() {
    return feeEquivalencies;
  }

  public void setFeeEquivalencies(Map<String, BigDecimal> feeEquivalencies) {
    this.feeEquivalencies = feeEquivalencies;
  }

  public Map<String, BigDecimal> getFees() {
    return fees;
  }

  public void setFees(Map<String, BigDecimal> fees) {
    this.fees = fees;
  }
}
