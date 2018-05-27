package com.thiagobezerra.bankslips.api;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.StatusWrapper;
import com.thiagobezerra.bankslips.service.BankSlipService;
import com.thiagobezerra.bankslips.service.exception.BankSlipNotFoundException;
import com.thiagobezerra.bankslips.service.exception.InvalidBankSlipException;

@RestController
@RequestMapping("/rest/bankslips")
class BankSlipController {
	private final BankSlipService bankSlipService;

	@Autowired
	BankSlipController(BankSlipService bankSlipService) {
		this.bankSlipService = bankSlipService;
	}

	@GetMapping
	Collection<BankSlip> getBankSlips() {
		return bankSlipService.listBankSlips();
	}
	
	@GetMapping("/{id}")
	ResponseEntity<?> getBankSlip(@PathVariable UUID id) {
		try {
			return ResponseEntity.ok(bankSlipService.getDetailsById(id));
		} catch (BankSlipNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	ResponseEntity<?> save(@RequestBody BankSlip bankSlip) {
		try {
			bankSlipService.save(bankSlip);
		} catch (InvalidBankSlipException e) {
			return ResponseEntity.unprocessableEntity().body("Invalid bankslip provided.");
		}
			
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
												  .path("/{id}")
												  .buildAndExpand(bankSlip.getId())
												  .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	ResponseEntity<?> changeStatus(@RequestBody StatusWrapper statusWrapper, @PathVariable UUID id) {
		if (statusWrapper.getStatus() == null)
			return ResponseEntity.unprocessableEntity().body("Invalid status provided.");
		
		try {
			switch (statusWrapper.getStatus()) {
			case PAID:
				bankSlipService.pay(id);
				break;
			case CANCELED:
				bankSlipService.cancel(id);
				break;
			default:
				return ResponseEntity.unprocessableEntity().body("Invalid status provided.");
			}
			
			return ResponseEntity.ok().build();
		} catch (BankSlipNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
