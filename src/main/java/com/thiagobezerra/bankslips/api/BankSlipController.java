package com.thiagobezerra.bankslips.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.service.BankSlipRepository;

@RestController
@RequestMapping("/rest/bankslips")
class BankSlipController {
	private final BankSlipRepository bankSlipRepository;
	
	@Autowired
	BankSlipController(BankSlipRepository bankSlipRepository) {
		this.bankSlipRepository = bankSlipRepository;
	}
	
	@GetMapping
	Collection<BankSlip> getBankSlips() {
		return bankSlipRepository.findAll();
	}
}
