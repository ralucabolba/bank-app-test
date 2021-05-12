package com.test.bank.controller;

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

import com.test.bank.dto.CurrentAccountDTO;
import com.test.bank.dto.PaginationDTO;
import com.test.bank.filter.CurrentAccountSpecification;
import com.test.bank.model.CurrentAccount;
import com.test.bank.service.CurrentAccountService;
import com.test.bank.util.SearchUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Current account management services")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/currentAccounts")
public class CurrentAccountController {

	private static final Logger logger = LoggerFactory.getLogger(CurrentAccountController.class);

	@Autowired
	private CurrentAccountService currentAccountService;

	@ApiOperation(notes = "<p>Service that returns: </p> <ul><li>currentAccounts, the list of CurrentAccountDTO objects</li> "
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
			@ApiParam(required = false, defaultValue = "12", value = "Size of page") @RequestParam(required = false, defaultValue = "12") String size) {
		logger.info("Find current accounts. Received request parameters: search = {}, page = {}, size = {}", search,
				page, size);

		CurrentAccountSpecification specification = new CurrentAccountSpecification(
				SearchUtils.queryToSearchCriterias(search, CurrentAccount.class));

		Page<CurrentAccountDTO> currentAccounts = currentAccountService.findAllBySearchCriteria(Integer.valueOf(page),
				Integer.valueOf(size), specification);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("currentAccounts", currentAccounts.getContent());
		resultMap.put("pagination", new PaginationDTO(currentAccounts.hasPrevious(), currentAccounts.hasNext(),
				currentAccounts.getTotalElements(), currentAccounts.getTotalPages()));
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@ApiOperation(notes = "Service that returns the current account idenfified by id", code = 201, value = "")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrentAccountDTO> findOne(@ApiParam(required = true) @PathVariable("id") Integer id) {
		logger.info("Find current account. Received id : {}", id);
		return new ResponseEntity<CurrentAccountDTO>(currentAccountService.findOne(id), HttpStatus.OK);
	}

	@ApiOperation(notes = "Creates a new current account", code = 201, value = "")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrentAccountDTO> add(
			@Valid @ApiParam(required = true) @RequestBody(required = true) CurrentAccountDTO dto) {
		logger.info("Create new current account. Received request body : {}", dto.toString());
		return new ResponseEntity<CurrentAccountDTO>(currentAccountService.add(dto), HttpStatus.CREATED);
	}

	@ApiOperation(notes = "Updates an existing current account", code = 201, value = "")
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrentAccountDTO> update(@ApiParam(required = true) @PathVariable("id") Integer id,
			@Valid @ApiParam(required = true) @RequestBody(required = true) CurrentAccountDTO dto) {
		logger.info("Update current account. Received id : {} and request body : {}", id, dto.toString());
		dto.setId(id);
		return new ResponseEntity<CurrentAccountDTO>(currentAccountService.update(dto), HttpStatus.OK);
	}

}
