package com.test.bank.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PaginationDTO {

	@ApiModelProperty(notes = "Indicates if there exists a previous page")
	private boolean hasPrev;

	@ApiModelProperty(notes = "Indicates if there exists a next page")
	private boolean hasNext;

	@ApiModelProperty(notes = "Indicates the total number of records(rows) the search returns")
	private long totalRows;

	@ApiModelProperty(notes = "Indicates the total number of pages the search returns")
	private int totalPages;

	public PaginationDTO(boolean hasPrevios, boolean hasNext, long totalRows, int totalPages) {
		this.hasPrev = hasPrevios;
		this.hasNext = hasNext;
		this.totalRows = totalRows;
		this.totalPages = totalPages;
	}

	public boolean getHasPrev() {
		return hasPrev;
	}

	public void setHasPrev(boolean hasPrev) {
		this.hasPrev = hasPrev;
	}

	public boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public String toString() {
		return "Pagination [hasPrev=" + hasPrev + ", hasNext=" + hasNext + ", totalRows=" + totalRows + ", totalPages="
				+ totalPages + "]";
	}

}
