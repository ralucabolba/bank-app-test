package com.test.bank.dto.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.test.bank.dto.CurrentAccountDTO;
import com.test.bank.model.CurrentAccount;

@Component
public class CurrentAccountConverter implements BaseDTOConverter<CurrentAccount, CurrentAccountDTO> {

	@Override
	public CurrentAccountDTO convertToDTO(CurrentAccount entity) {
		CurrentAccountDTO dto = new CurrentAccountDTO();
		dto.setId(entity.getId());
		dto.setAccountNo(entity.getAccountNo());
		dto.setStatus(entity.getStatus());
		dto.setBalance(entity.getBalance());
		dto.setCurrency(entity.getCurrency());
		dto.setDateCreated(entity.getDateCreated());
		dto.setDateModified(entity.getDateModified());
		return dto;
	}

	@Override
	public CurrentAccount convertFromDTO(CurrentAccountDTO dto) {
		CurrentAccount entity = new CurrentAccount();
		entity.setId(dto.getId());
		entity.setStatus(dto.getStatus());
		entity.setBalance(dto.getBalance());
		entity.setCurrency(dto.getCurrency());
		return entity;
	}

	@Override
	public List<CurrentAccountDTO> convertToDTO(List<CurrentAccount> entityList) {
		return entityList.stream().map(e -> convertToDTO(e)).collect(Collectors.toList());
	}

	@Override
	public List<CurrentAccount> convertFromDTO(List<CurrentAccountDTO> dtoList) {
		return dtoList.stream().map(d -> convertFromDTO(d)).collect(Collectors.toList());
	}

}
