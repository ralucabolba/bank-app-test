package com.test.bank.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.test.bank.enums.Status;

@Entity
@Table(name = "current_accounts")
public class CurrentAccount implements BaseEntity<Integer> {

	private static final long serialVersionUID = 7699395175788294878L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;

	@Column(name = "account_no", length = 24)
	@NotNull(message = "Account no should not be null")
	private String accountNo;

	@Column(columnDefinition = "enum('OPEN','CLOSED')")
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status should not be null")
	private Status status;

	@Column(length = 3)
	@NotNull(message = "Currency should not be null")
	private String currency;

	@Column
	@NotNull(message = "Balance should not be null")
	private Double balance;

	@Column(name = "date_created")
	@NotNull(message = "Date created should not be null")
	private LocalDateTime dateCreated;

	@Column(name = "date_modified")
	@NotNull(message = "Date modified should not be null")
	private LocalDateTime dateModified;

	@OneToMany(mappedBy = "currentAccount", fetch = FetchType.LAZY)
	private Set<Transaction> transactions;

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

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrentAccount other = (CurrentAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CurrentAccount [id=" + id + ", accountNo=" + accountNo + ", status=" + status + ", currency=" + currency
				+ ", balance=" + balance + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + "]";
	}

}
