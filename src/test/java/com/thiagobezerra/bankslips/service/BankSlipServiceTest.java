package com.thiagobezerra.bankslips.service;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.BankSlipValidator;
import com.thiagobezerra.bankslips.model.Status;
import com.thiagobezerra.bankslips.service.exception.BankSlipNotFoundException;
import com.thiagobezerra.bankslips.service.exception.InvalidBankSlipException;

@RunWith(MockitoJUnitRunner.class)
public class BankSlipServiceTest {
	@Mock
	protected BankSlipRepository bankSlipRepository;  

	@Mock
	protected BankSlipValidator bankSlipValidator;  
	
	private BankSlipService bankSlipService;
	
	@Before
    public void setup() throws Exception {
        this.bankSlipService = new BankSlipService(bankSlipRepository, bankSlipValidator);
    }
	
	@Test
	public void listBankSlipsShouldReturnRepositoryResult() {
		bankSlipService.listBankSlips();
		
		verify(bankSlipRepository, only()).findAll();
	}
	
	@Test(expected = InvalidBankSlipException.class)
	public void whenSavingABackSlipAndItIsInvalidShouldThrownAnException() throws InvalidBankSlipException {
		doReturn(false).when(bankSlipValidator).isValid(Mockito.any(BankSlip.class));
		
		bankSlipService.save(new BankSlip());
	}
	
	@Test
	public void whenSavingABackSlipAndItIsValidShouldCallRepositoryToSaveIt() throws InvalidBankSlipException {
		doReturn(true).when(bankSlipValidator).isValid(Mockito.any(BankSlip.class));
		
		BankSlip bankSlip = new BankSlip();
		
		bankSlipService.save(bankSlip);
		
		verify(bankSlipRepository, only()).save(bankSlip);
	}
	
	@Test
	public void whenRetrievingBankSlipByIdShouldGetItFromRepository() throws BankSlipNotFoundException {
		doReturn(Optional.of(new BankSlip())).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		
		UUID id = new UUID(10, 10);
		
		bankSlipService.getDetailsById(id);
		
		verify(bankSlipRepository, only()).findById(id);
	}	
	
	@Test(expected = BankSlipNotFoundException.class)
	public void whenRetrievingBankSlipByIdAndItDoesNotExistShouldThrowAnException() throws BankSlipNotFoundException {
		doReturn(Optional.empty()).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		
		UUID id = new UUID(10, 10);
		
		bankSlipService.getDetailsById(id);
	}	
	
	@Test
	public void shouldCalculateFineRatesWhenGettingBankSlipDetails() throws BankSlipNotFoundException {
		BankSlipService mock = mock(BankSlipService.class, withSettings().useConstructor(bankSlipRepository, bankSlipValidator));
		doCallRealMethod().when(mock).getDetailsById(Mockito.any(UUID.class));
		BankSlip bankSlip = mock(BankSlip.class);
		doReturn(Optional.of(bankSlip)).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		doNothing().when(mock).calculateFineRates(bankSlip);
		
		UUID id = new UUID(10, 10);
		
		mock.getDetailsById(id);
		
		verify(mock).calculateFineRates(bankSlip);
	}
	
	@Test(expected = BankSlipNotFoundException.class)
	public void whenPayingABankSlipWhichNotExistsShouldThrowAnException() throws BankSlipNotFoundException {
		doReturn(Optional.empty()).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		
		UUID id = new UUID(10, 10);
		
		bankSlipService.pay(id);
	}
	
	@Test
	public void whenPayingAnExistentBankSlipShouldUpdateItWithPaidStatus() throws BankSlipNotFoundException {
		BankSlipService mock = mock(BankSlipService.class);
		doCallRealMethod().when(mock).pay(Mockito.any(UUID.class));
		doNothing().when(mock).update(Mockito.any(UUID.class), Mockito.any(Status.class));
		
		UUID id = new UUID(10, 10);
		
		mock.pay(id);
		
		verify(mock).update(id, Status.PAID);
	}
	
	@Test(expected = BankSlipNotFoundException.class)
	public void whenCancelingABankSlipWhichNotExistsShouldThrowAnException() throws BankSlipNotFoundException {
		doReturn(Optional.empty()).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		
		UUID id = new UUID(10, 10);
		
		bankSlipService.cancel(id);
	}
	
	@Test
	public void whenCancelingAnExistentBankSlipShouldUpdateItWithCanceledStatus() throws BankSlipNotFoundException {
		BankSlipService mock = mock(BankSlipService.class);
		doCallRealMethod().when(mock).cancel(Mockito.any(UUID.class));
		doNothing().when(mock).update(Mockito.any(UUID.class), Mockito.any(Status.class));
		
		UUID id = new UUID(10, 10);
		
		mock.cancel(id);
		
		verify(mock).update(id, Status.CANCELED);
	}
	
	@Test(expected = BankSlipNotFoundException.class)
	public void whenUpdatingStatusOfABankSlipAndItDoesNotExistShouldThrowAnException() throws BankSlipNotFoundException {
		doReturn(Optional.empty()).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		
		UUID id = new UUID(10, 10);
		
		bankSlipService.update(id, Status.PAID);
	}
	
	@Test
	public void whenUpdatingStatusOfAnExistentBankSlipShouldSaveItOnDatabase() throws BankSlipNotFoundException {
		BankSlip bankSlip = mock(BankSlip.class);
		doReturn(Optional.of(bankSlip)).when(bankSlipRepository).findById(Mockito.any(UUID.class));
		
		UUID id = new UUID(10, 10);
		
		bankSlipService.update(id, Status.PAID);
		
		verify(bankSlip).setStatus(Status.PAID);
		verify(bankSlipRepository).save(bankSlip);
	}
}
