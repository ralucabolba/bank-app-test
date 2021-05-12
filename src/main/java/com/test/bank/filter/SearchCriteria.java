package com.test.bank.filter;

public class SearchCriteria {

	private String name;

	private SearchOperation operation;

	private Object value;

	public SearchCriteria() {

	}

	public SearchCriteria(String name, SearchOperation operation, Object value) {
		this.name = name;
		this.operation = operation;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SearchOperation getOperation() {
		return operation;
	}

	public void setOperation(SearchOperation operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SearchCriteria [name=" + name + ", operation=" + operation + ", value=" + value + "]";
	}

}
