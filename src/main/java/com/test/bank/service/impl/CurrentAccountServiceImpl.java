package com.test.bank.service.impl;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.bank.dto.CurrentAccountDTO;
import com.test.bank.dto.converter.CurrentAccountConverter;
import com.test.bank.enums.Status;
import com.test.bank.model.CurrentAccount;
import com.test.bank.repository.CurrentAccountRepository;
import com.test.bank.service.CurrentAccountService;

@Service
public class CurrentAccountServiceImpl implements CurrentAccountService {

	private static final String ACCOUNT_NO_PREFIX = "RO";
	private static final int ACCOUNT_NO_LENGHT = 22;
	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

	@Autowired
	private CurrentAccountRepository currentAccountRepository;

	@Autowired
	private CurrentAccountConverter currentAccountConverter;

	@Transactional(readOnly = true)
	@Override
	public CurrentAccountDTO findOne(Integer id) {
		return currentAccountConverter.convertToDTO(currentAccountRepository.getOne(id));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CurrentAccountDTO> findAllBySearchCriteria(Integer page, Integer size,
			Specification<CurrentAccount> specification) {
		return currentAccountRepository.findAll(specification, PageRequest.of(page, size))
				.map(entity -> currentAccountConverter.convertToDTO(entity));
	}

	@Transactional
	@Override
	public CurrentAccountDTO add(CurrentAccountDTO dto) {
		CurrentAccount currentAccount = currentAccountConverter.convertFromDTO(dto);
		currentAccount.setId(null);
		currentAccount.setDateCreated(LocalDateTime.now());
		currentAccount.setDateModified(LocalDateTime.now());
		currentAccount.setStatus(Status.OPEN);
		currentAccount.setAccountNo(generateAccountNo());
		return currentAccountConverter.convertToDTO(currentAccountRepository.save(currentAccount));
	}

	@Transactional
	@Override
	public CurrentAccountDTO update(CurrentAccountDTO dto) {
		CurrentAccount currentAccountExisting = currentAccountRepository.getOne(dto.getId());
		if (currentAccountExisting != null) {
			CurrentAccount currentAccountFromDto = currentAccountConverter.convertFromDTO(dto);
			currentAccountExisting.setBalance(currentAccountFromDto.getBalance());
			currentAccountExisting.setCurrency(currentAccountFromDto.getCurrency());
			currentAccountExisting.setStatus(currentAccountFromDto.getStatus());
			currentAccountExisting.setDateModified(LocalDateTime.now());
			return currentAccountConverter.convertToDTO(currentAccountRepository.save(currentAccountExisting));
		}
		throw new EntityNotFoundException("Current account with id " + dto.getId() + " not found");
	}

	@Override
	public void delete(Integer id) {
		throw new UnsupportedOperationException(); // suppose a current account cannot be deleted, just marked as closed
	}

	/**
	 * Generates a new account no. To ensure uniqueness, the method checks if the
	 * generated value does not already exist in database
	 * 
	 * @return generated account no
	 */
	private String generateAccountNo() {
		String accountNo = null;
		boolean exists = true;
		while (exists) {
			accountNo = ACCOUNT_NO_PREFIX + RandomStringUtils.random(ACCOUNT_NO_LENGHT, ALPHANUMERIC_CHARACTERS);
			if (!currentAccountRepository.existsByAccountNo(accountNo)) {
				exists = false;
			}
		}
		return accountNo;
	}

}
