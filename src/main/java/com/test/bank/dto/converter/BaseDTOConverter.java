package com.test.bank.dto.converter;

import java.util.List;

import com.test.bank.dto.BaseDTO;
import com.test.bank.model.BaseEntity;

public interface BaseDTOConverter<ENTITY extends BaseEntity<?>, DTO extends BaseDTO<?>> {

	DTO convertToDTO(ENTITY entity);

	ENTITY convertFromDTO(DTO dto);

	List<DTO> convertToDTO(List<ENTITY> entity);

	List<ENTITY> convertFromDTO(List<DTO> dto);
	
}
