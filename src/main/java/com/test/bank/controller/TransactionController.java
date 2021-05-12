package com.test.bank.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.bank.dto.PaginationDTO;
import com.test.bank.dto.TransactionDTO;
import com.test.bank.enums.TimeUnit;
import com.test.bank.filter.TransactionSpecification;
import com.test.bank.model.Transaction;
import com.test.bank.service.TransactionService;
import com.test.bank.util.SearchUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Transaction management services")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/transactions")
public class TransactionController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@ApiOperation(notes = "<p>Service that returns: </p> <ul><li>transactions, the list of TransactionDTO objects</li> "
			+ "<li>pagination, an object containing : </li><li>hasNext</li><li>hasPrev</li>" + "<li>totalRows</li>"
			+ "<li>totalPages</li></ul>", code = 201, value = "")
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> findAll(
			@ApiParam(required = false, value = "Query string containing the search criteria for filtering. "
					+ "The expected format is at follows: search=<i>nameField1</i><b>operator1</b><i>valueField1</i>;"
					+ "<i>nameField2</i><b>operator2</b><i>valueField2</i> </br> Search filtering is possible after "
					+ "multiple criteria, delimited by <b>;</b>. Possible operators are : <ul><li><b>:</b> - equals</li>"
					+ "<li><b>!</b> - not equals</li><li><b><</b> - less than</li><li><b>></b> - greater than</li></ul> </br>") @RequestParam(required = false) String search,
			@ApiParam(required = false, defaultValue = "0", value = "Page number") @RequestParam(required = false, defaultValue = "0") String page,
			@ApiParam(required = false, defaultValue = "12", value = "Size of page") @RequestParam(required = false, defaultValue = "12") String size,
			@ApiParam(required = false, value = "To be used together with <b>timeUnit</b> in order to search transactions made in the <b>lastUnits</b> of time. "
					+ "</br>For example ?lastUnits=5&timeUnit=MINUTES - returns the transactions made in the last 5 minutes ") @RequestParam(required = false) Long lastUnits,
			@ApiParam(required = false, value = "Time unit", allowableValues = "MINUTES, HOURS, DAYS, MONTHS, YEARS") @RequestParam(required = false) TimeUnit timeUnit) {
		logger.info(
				"Find transactions. Received request parameters: search = {}, page = {}, size = {}, lastUnits = {}, timeUnit = {}",
				search, page, size, lastUnits, timeUnit);

		search = fillLastTimeUnitsSearchCriterias(search, lastUnits, timeUnit);
		TransactionSpecification specification = new TransactionSpecification(
				SearchUtils.queryToSearchCriterias(search, Transaction.class));

		Page<TransactionDTO> transactions = transactionService.findAllBySearchCriteria(Integer.valueOf(page),
				Integer.valueOf(size), specification);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("transactions", transactions.getContent());
		resultMap.put("pagination", new PaginationDTO(transactions.hasPrevious(), transactions.hasNext(),
				transactions.getTotalElements(), transactions.getTotalPages()));
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@ApiOperation(notes = "Service that returns the transaction idenfified by id", code = 201, value = "")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionDTO> findOne(@ApiParam(required = true) @PathVariable("id") Integer id) {
		logger.info("Find transaction. Received id : {}", id);
		return new ResponseEntity<TransactionDTO>(transactionService.findOne(id), HttpStatus.OK);
	}

	@ApiOperation(notes = "Creates a new transaction", code = 201, value = "")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionDTO> add(
			@Valid @ApiParam(required = true) @RequestBody(required = true) TransactionDTO dto) {
		logger.info("Create new transaction. Received request body : {}", dto.toString());
		return new ResponseEntity<TransactionDTO>(transactionService.add(dto), HttpStatus.CREATED);
	}

	/**
	 * Fills the search string with the corresponding criteria for filtering
	 * dateCreated made in the <b>lastUnits</b> of <b>timeUnit</b>
	 * 
	 * @param search
	 * @param lastUnits
	 * @param timeUnit
	 * @return the updated search string
	 */
	private String fillLastTimeUnitsSearchCriterias(String search, Long lastUnits, TimeUnit timeUnit) {
		if (lastUnits == null || timeUnit == null) {
			return search;
		}
		LocalDateTime now = LocalDateTime.now().minus(lastUnits, ChronoUnit.valueOf(timeUnit.name()));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

		String inLastTimeUnitSearchCriteria = "dateCreated" + ">" + now.format(formatter);
		if (search == null) {
			return inLastTimeUnitSearchCriteria;
		}
		return search.concat(";" + inLastTimeUnitSearchCriteria);
	}
}
