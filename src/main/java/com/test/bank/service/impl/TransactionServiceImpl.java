package com.test.bank.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.bank.dto.CurrentAccountDTO;
import com.test.bank.dto.TransactionDTO;
import com.test.bank.dto.converter.CurrentAccountConverter;
import com.test.bank.dto.converter.TransactionConverter;
import com.test.bank.model.Transaction;
import com.test.bank.repository.TransactionRepository;
import com.test.bank.service.CurrentAccountService;
import com.test.bank.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionConverter transactionConverter;
	
	@Autowired
	private CurrentAccountConverter currentAccountConverter;
	
	@Autowired
	private CurrentAccountService currentAccountService;

	@Transactional(readOnly = true)
	@Override
	public TransactionDTO findOne(Integer id) {
		return transactionConverter.convertToDTO(transactionRepository.getOne(id));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<TransactionDTO> findAllBySearchCriteria(Integer page, Integer size,
			Specification<Transaction> specification) {
		return transactionRepository.findAll(specification, PageRequest.of(page, size))
				.map(entity -> transactionConverter.convertToDTO(entity));
	}

	@Transactional
	@Override
	public TransactionDTO add(TransactionDTO dto) {
		Transaction transaction = transactionConverter.convertFromDTO(dto);
		CurrentAccountDTO currentAccountDTO = currentAccountConverter.convertToDTO(transaction.getCurrentAccount());
		
		Double balance = transaction.getCurrentAccount().getBalance();
		if (transaction.getDebit() != null) {
			// transfer from current account, withdrawal
			// suppose transaction debit values are negative values
			if (Double.valueOf(balance + transaction.getDebit()).compareTo(0d) < 0) {
				// not sufficient funds
				throw new ValidationException("Transaction could not be performed : insufficient funds");
			}
			balance += transaction.getDebit();
		} else if (transaction.getCredit() != null) {
			// transfer to current account, deposit
			balance += transaction.getCredit();
		}
		
		currentAccountDTO.setBalance(balance);
		currentAccountService.update(currentAccountDTO);
		
		transaction.setId(null);
		transaction.setBalance(balance);
		transaction.setDateCreated(LocalDateTime.now());
		transaction.setDeleted(false);
		return transactionConverter.convertToDTO(transactionRepository.save(transaction));
	}

	@Override
	public TransactionDTO update(TransactionDTO dto) {
		throw new UnsupportedOperationException(); // suppose a transaction cannot be modified once created
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		transactionRepository.deleteById(id);
	}

	/**
	 * Scheduled service that runs once a day, every day, and checks and deletes the
	 * transactions which are older than 6 months
	 */
	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void deleteTransactionsOlderThan6MonthsScheduler() {
		logger.info("Delete transactions older than 6 months");
		List<Transaction> transactions = transactionRepository
				.findByDateCreatedLessThan(LocalDateTime.now().minusMonths(6));
		logger.info("Number of transactions to delete: {}", transactions.size());
		transactions.forEach(t -> delete(t.getId()));
	}
}
