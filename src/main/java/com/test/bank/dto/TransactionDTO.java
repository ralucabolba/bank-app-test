package com.test.bank.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

@ApiModel
public class TransactionDTO implements BaseDTO<Integer> {

	private static final long serialVersionUID = -3538833191802127035L;

	@ApiModelProperty
	private Integer id;

	@ApiModelProperty
	private String description;

	@ApiModelProperty
	private Double debit;

	@ApiModelProperty
	private Double credit;

	@ApiModelProperty(accessMode = AccessMode.READ_ONLY)
	private Double balance;

	@ApiModelProperty(accessMode = AccessMode.READ_ONLY)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime dateCreated;

	@ApiModelProperty(required = true)
	@NotNull(message = "Current account id should not be null")
	private Integer currentAccountId;

	@ApiModelProperty
	private CurrentAccountDTO currentAccount;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
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

	public Integer getCurrentAccountId() {
		return currentAccountId;
	}

	public void setCurrentAccountId(Integer currentAccountId) {
		this.currentAccountId = currentAccountId;
	}

	public CurrentAccountDTO getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(CurrentAccountDTO currentAccount) {
		this.currentAccount = currentAccount;
	}

	@Override
	public String toString() {
		return "TransactionDTO [id=" + id + ", description=" + description + ", debit=" + debit + ", credit=" + credit
				+ ", balance=" + balance + ", dateCreated=" + dateCreated + ", currentAccountId=" + currentAccountId
				+ ", currentAccount=" + currentAccount + "]";
	}

}
