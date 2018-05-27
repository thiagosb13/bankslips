package com.thiagobezerra.bankslips.api;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.BeanValidator;
import com.thiagobezerra.bankslips.service.BankSlipRepository;

@RestController
@RequestMapping("/rest/bankslips")
class BankSlipController {
	private final BankSlipRepository bankSlipRepository;
	private final BeanValidator<BankSlip> bankSlipValidator;

	@Autowired
	BankSlipController(BankSlipRepository bankSlipRepository, BeanValidator<BankSlip> bankSlipValidator) {
		this.bankSlipRepository = bankSlipRepository;
		this.bankSlipValidator = bankSlipValidator;
	}

	@GetMapping
	Collection<BankSlip> getBankSlips() {
		return bankSlipRepository.findAll();
	}

	@PostMapping
	ResponseEntity<?> add(@RequestBody BankSlip bankSlip) {
		if (!bankSlipValidator.isValid(bankSlip)) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		bankSlipRepository.save(bankSlip);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
												  .path("/{id}")
												  .buildAndExpand(bankSlip.getId())
												  .toUri();
		
		return ResponseEntity.created(location).build();
	}
}
