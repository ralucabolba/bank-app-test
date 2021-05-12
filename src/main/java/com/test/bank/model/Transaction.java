package com.test.bank.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;

@Where(clause = "deleted=0")
@Entity
@Table(name = "transactions")
public class Transaction implements BaseEntity<Integer> {

	private static final long serialVersionUID = 2224652233434807521L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;

	@Column
	private String description;

	@Column
	private Double debit;

	@Column
	private Double credit;

	@Column
	private Double balance;

	@Column(name = "date_created", nullable = false)
	@NotNull(message = "Date created should not be null")
	private LocalDateTime dateCreated;

	@Column
	private boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_current_account", nullable = false)
	@NotNull(message = "Current account should not be null")
	private CurrentAccount currentAccount;

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

	public CurrentAccount getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(CurrentAccount currentAccount) {
		this.currentAccount = currentAccount;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", description=" + description + ", debit=" + debit + ", credit=" + credit
				+ ", balance=" + balance + ", dateCreated=" + dateCreated + ", deleted=" + deleted + ", currentAccount="
				+ currentAccount + "]";
	}

}
