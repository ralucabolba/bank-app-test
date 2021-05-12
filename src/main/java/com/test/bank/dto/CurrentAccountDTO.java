package com.test.bank.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.bank.enums.Status;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

@ApiModel
public class CurrentAccountDTO implements BaseDTO<Integer> {

	private static final long serialVersionUID = 304260621989421032L;

	@ApiModelProperty
	private Integer id;

	@ApiModelProperty
	private String accountNo;

	@ApiModelProperty(accessMode = AccessMode.READ_ONLY)
	private Status status;

	@Size(min = 3, max = 3)
	@ApiModelProperty(required = true)
	private String currency;

	@ApiModelProperty(required = true)
	@NotNull(message = "Balance should not be null")
	private Double balance;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY)
	private LocalDateTime dateCreated;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY)
	private LocalDateTime dateModified;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getDateModified() {
		return dateModified;
	}

	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public String toString() {
		return "CurrentAccountDTO [id=" + id + ", accountNo=" + accountNo + ", status=" + status + ", currency="
				+ currency + ", balance=" + balance + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified
				+ "]";
	}

}
