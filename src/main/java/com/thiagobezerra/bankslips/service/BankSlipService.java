package com.thiagobezerra.bankslips.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.BeanValidator;
import com.thiagobezerra.bankslips.service.exception.BankSlipNotFoundException;
import com.thiagobezerra.bankslips.service.exception.InvalidBankSlipException;

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

	public BankSlip findById(UUID id) throws BankSlipNotFoundException {
		return bankSlipRepository.findById(id).orElseThrow(() -> new BankSlipNotFoundException());
	}
	
	public void pay(UUID id) throws BankSlipNotFoundException {
		
	}
	
	public void cancel(UUID id) throws BankSlipNotFoundException {
		
	}
}
