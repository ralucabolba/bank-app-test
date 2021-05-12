package com.test.bank.dto.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.bank.dto.TransactionDTO;
import com.test.bank.model.CurrentAccount;
import com.test.bank.model.Transaction;
import com.test.bank.repository.CurrentAccountRepository;

@Component
public class TransactionConverter implements BaseDTOConverter<Transaction, TransactionDTO> {

	@Autowired
	private CurrentAccountConverter currentAccountConverter;

	@Autowired
	private CurrentAccountRepository currentAccountRepository;

	@Override
	public TransactionDTO convertToDTO(Transaction entity) {
		TransactionDTO dto = new TransactionDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setDebit(entity.getDebit());
		dto.setCredit(entity.getCredit());
		dto.setBalance(entity.getBalance());
		dto.setDateCreated(entity.getDateCreated());
		dto.setCurrentAccount(currentAccountConverter.convertToDTO(entity.getCurrentAccount()));
		dto.setCurrentAccountId(entity.getCurrentAccount().getId());
		return dto;
	}

	@Override
	public Transaction convertFromDTO(TransactionDTO dto) {
		Transaction entity = new Transaction();
		entity.setId(dto.getId());
		entity.setDescription(dto.getDescription());
		entity.setDebit(dto.getDebit());
		entity.setCredit(dto.getCredit());
		if (dto.getCurrentAccountId() != null) {
			CurrentAccount currentAccount = currentAccountRepository.getOne(dto.getCurrentAccountId());
			entity.setCurrentAccount(currentAccount);
		}
		return entity;
	}

	@Override
	public List<TransactionDTO> convertToDTO(List<Transaction> entityList) {
		return entityList.stream().map(e -> convertToDTO(e)).collect(Collectors.toList());
	}

	@Override
	public List<Transaction> convertFromDTO(List<TransactionDTO> dtoList) {
		return dtoList.stream().map(d -> convertFromDTO(d)).collect(Collectors.toList());
	}

}
