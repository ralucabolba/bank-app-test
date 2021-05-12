package com.test.bank;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.test.bank.dto.CurrentAccountDTO;
import com.test.bank.dto.converter.CurrentAccountConverter;
import com.test.bank.enums.Status;
import com.test.bank.model.CurrentAccount;
import com.test.bank.repository.CurrentAccountRepository;
import com.test.bank.service.impl.CurrentAccountServiceImpl;

public class CurrentAccountTests {

	private Integer ID = 1;

	@Mock
	private CurrentAccount currentAccountFromDto;
	@Mock
	private CurrentAccountDTO currentAccountDTO;
	@Mock
	private CurrentAccountRepository currentAccountRepository;
	@Mock
	private CurrentAccountConverter currentAccountConverter;

	private AutoCloseable closeable;

	@InjectMocks
	private CurrentAccountServiceImpl currentAccountService;

	@BeforeEach
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	public void add() {
		givenEntityIsConvertedFromDto();
		andAccountNoIsGenerated();
		andEntityIsConvertedToDto();
		whenAdd();
		thenSaveOperationWasPerformed();
	}

	@Test
	public void update() {
		givenEntityIsConvertedFromDto();
		givenEntityIsFoundById();
		andStatusIsChangedToClosed();
		whenUpdate();
		thenSaveOperationWasPerformed();
	}

	@Test
	public void delete() {
		assertThrows(UnsupportedOperationException.class, () -> {
			whenDelete();
		});
	}

	@Test
	public void findOne() {
		givenEntityIsFoundById();
		whenFindOne();
		thenGetOneOperationWasPerformed();
	}

	private void givenEntityIsFoundById() {
		given(currentAccountDTO.getId()).willReturn(ID);
		given(currentAccountRepository.getOne(ID)).willReturn(currentAccountFromDto);
	}

	private void givenEntityIsConvertedFromDto() {
		given(currentAccountConverter.convertFromDTO(currentAccountDTO)).willReturn(currentAccountFromDto);
	}

	private void andAccountNoIsGenerated() {
		given(currentAccountRepository.existsByAccountNo(Mockito.anyString())).willReturn(false);
	}

	private void andEntityIsConvertedToDto() {
		given(currentAccountRepository.save(currentAccountFromDto)).willReturn(currentAccountFromDto);
		given(currentAccountConverter.convertToDTO(currentAccountFromDto)).willReturn(currentAccountDTO);
	}

	private void andStatusIsChangedToClosed() {
		given(currentAccountDTO.getStatus()).willReturn(Status.CLOSED);
	}

	private void whenAdd() {
		currentAccountService.add(currentAccountDTO);
	}

	private void whenUpdate() {
		currentAccountService.update(currentAccountDTO);
	}

	private void whenFindOne() {
		currentAccountService.findOne(ID);
	}

	private void whenDelete() {
		currentAccountService.delete(ID);
	}

	private void thenSaveOperationWasPerformed() {
		verify(currentAccountRepository, times(1)).save(currentAccountFromDto);
	}

	private void thenGetOneOperationWasPerformed() {
		verify(currentAccountRepository, times(1)).getOne(ID);
	}

}
