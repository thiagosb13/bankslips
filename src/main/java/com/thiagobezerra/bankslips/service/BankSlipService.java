package com.thiagobezerra.bankslips.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;
import com.thiagobezerra.bankslips.service.exception.BankSlipNotFoundException;
import com.thiagobezerra.bankslips.service.exception.InvalidBankSlipException;

@Component
public class BankSlipService {

	private final BankSlipRepository bankSlipRepository;
	private final BeanValidator<BankSlip> bankSlipValidator;
	
	@Autowired
	BankSlipService(BankSlipRepository bankSlipRepository, BeanValidator<BankSlip> bankSlipValidator) {
		this.bankSlipRepository = bankSlipRepository;
		this.bankSlipValidator = bankSlipValidator;
	}
	
	public Collection<BankSlip> listBankSlips() {
		return bankSlipRepository.findAll();
	}
	
	public void save(BankSlip bankSlip) throws InvalidBankSlipException {
		if (!bankSlipValidator.isValid(bankSlip)) {
			throw new InvalidBankSlipException();
		}
		
		bankSlipRepository.save(bankSlip);
	}

	public BankSlip getDetailsById(UUID id) throws BankSlipNotFoundException {
		BankSlip bankSlip = bankSlipRepository.findById(id)
											  .orElseThrow(() -> new BankSlipNotFoundException());
					
		calculateFineRates(bankSlip);
		
		return bankSlip;
	}
	
	public void pay(UUID id) throws BankSlipNotFoundException {
		update(id, Status.PAID);
	}
	
	public void cancel(UUID id) throws BankSlipNotFoundException {
		update(id, Status.CANCELED);
	}
	
	public void update(UUID id, Status status) throws BankSlipNotFoundException {
		BankSlip bankSlip = bankSlipRepository.findById(id)
				  							  .orElseThrow(() -> new BankSlipNotFoundException());
		bankSlip.setStatus(status);
		bankSlipRepository.save(bankSlip);
	}
	
	public void calculateFineRates(BankSlip bankSlip) {
		int delayedDays = LocalDate.now().compareTo(bankSlip.getDueDate());
		
		BigDecimal fine = new BigDecimal("0"); 
		
		if (delayedDays > 10) {
			fine = bankSlip.getTotalInCents().multiply(new BigDecimal("0.01"));
		} else if (delayedDays > 0){
			fine = bankSlip.getTotalInCents().multiply(new BigDecimal("0.005"));
		}
		
		bankSlip.setFine(fine);
	}
}
