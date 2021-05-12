package com.test.bank.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface BaseEntity<ID extends Serializable> extends Serializable {

	ID getId();

	void setId(ID id);

}