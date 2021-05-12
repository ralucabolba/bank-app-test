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
import org.mockito.MockitoAnnotations;

import com.test.bank.dto.CurrentAccountDTO;
import com.test.bank.dto.TransactionDTO;
import com.test.bank.dto.converter.CurrentAccountConverter;
import com.test.bank.dto.converter.TransactionConverter;
import com.test.bank.model.CurrentAccount;
import com.test.bank.model.Transaction;
import com.test.bank.repository.TransactionRepository;
import com.test.bank.service.CurrentAccountService;
import com.test.bank.service.impl.TransactionServiceImpl;

public class TransactionTests {

	private Integer CURRENT_ACCOUNT_ID = 1;
	private Integer ID = 2;
	private Double BALANCE = 100d;
	private Double DEBIT = -10d;

	@Mock
	private Transaction transaction;
	@Mock
	private TransactionDTO transactionDTO;
	@Mock
	private CurrentAccount currentAccount;
	@Mock
	private CurrentAccountDTO currentAccountDTO;
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private TransactionConverter transactionConverter;
	@Mock
	private CurrentAccountConverter currentAccountConverter;
	@Mock
	private CurrentAccountService currentAccountService;

	private AutoCloseable closeable;

	@InjectMocks
	private TransactionServiceImpl transactionService;

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
		andEntityIsConvertedToDto();
		andCurrentAccountIsAssociatedToTransaction();
		andCurrentAccountIsConverterToDto();
		whenAdd();
		thenSaveOperationWasPerformed();
	}

	@Test
	public void update() {
		assertThrows(UnsupportedOperationException.class, () -> {
			whenUpdate();
		});
	}

	@Test
	public void delete() {
		whenDelete();
		thenDeleteByIdOperationWasPerformed();
	}

	@Test
	public void findOne() {
		givenEntityIsFoundById();
		whenFindOne();
		thenGetOneOperationWasPerformed();
	}

	private void givenEntityIsFoundById() {
		given(transactionDTO.getId()).willReturn(ID);
		given(transactionDTO.getDebit()).willReturn(DEBIT);
		given(transactionRepository.getOne(ID)).willReturn(transaction);
	}

	private void givenEntityIsConvertedFromDto() {
		given(transactionDTO.getCurrentAccountId()).willReturn(CURRENT_ACCOUNT_ID);
		given(transactionConverter.convertFromDTO(transactionDTO)).willReturn(transaction);
	}

	private void andEntityIsConvertedToDto() {
		given(transactionRepository.save(transaction)).willReturn(transaction);
		given(transactionConverter.convertToDTO(transaction)).willReturn(transactionDTO);
	}

	private void andCurrentAccountIsAssociatedToTransaction() {
		given(transaction.getCurrentAccount()).willReturn(currentAccount);
		given(currentAccount.getBalance()).willReturn(BALANCE);
	}

	private void andCurrentAccountIsConverterToDto() {
		given(currentAccountConverter.convertToDTO(currentAccount)).willReturn(currentAccountDTO);
	}

	private void whenAdd() {
		transactionService.add(transactionDTO);
	}

	private void whenUpdate() {
		transactionService.update(transactionDTO);
	}

	private void whenFindOne() {
		transactionService.findOne(ID);
	}

	private void whenDelete() {
		transactionService.delete(ID);
	}

	private void thenSaveOperationWasPerformed() {
		verify(currentAccountService, times(1)).update(currentAccountDTO);
		verify(transactionRepository, times(1)).save(transaction);
	}

	private void thenGetOneOperationWasPerformed() {
		verify(transactionRepository, times(1)).getOne(ID);
	}

	private void thenDeleteByIdOperationWasPerformed() {
		verify(transactionRepository, times(1)).deleteById(ID);
	}
}
