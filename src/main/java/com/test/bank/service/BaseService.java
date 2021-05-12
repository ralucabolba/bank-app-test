package com.test.bank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.test.bank.dto.BaseDTO;
import com.test.bank.model.BaseEntity;

public interface BaseService<ENTITY extends BaseEntity<?>, DTO extends BaseDTO<?>> {

	DTO findOne(Integer id);

	Page<DTO> findAllBySearchCriteria(Integer page, Integer size, Specification<ENTITY> specification);

	DTO add(DTO dto);

	DTO update(DTO dto);
	
	void delete(Integer id);

}
