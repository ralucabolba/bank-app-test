package com.test.bank.dto;

import java.io.Serializable;

public interface BaseDTO<ID extends Serializable> extends Serializable {

	ID getId();

	void setId(ID id);

}
