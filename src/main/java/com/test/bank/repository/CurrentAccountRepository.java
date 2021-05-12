package com.test.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.test.bank.model.CurrentAccount;

@Repository
public interface CurrentAccountRepository
		extends JpaRepository<CurrentAccount, Integer>, JpaSpecificationExecutor<CurrentAccount> {

	boolean existsByAccountNo(String accountNo);
}
