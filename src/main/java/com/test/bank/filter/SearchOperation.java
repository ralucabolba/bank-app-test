package com.test.bank.filter;

public enum SearchOperation {

	EQUALS(":"), NOT_EQUALS("!"), LESS_THAN("<"), GREATER_THAN(">");

	private String operation;

	private SearchOperation(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public static SearchOperation fromString(String operation) {
		for (SearchOperation searchOperation : SearchOperation.values()) {
			if (searchOperation.getOperation().equals(operation)) {
				return searchOperation;
			}
		}
		return null;
	}

}
