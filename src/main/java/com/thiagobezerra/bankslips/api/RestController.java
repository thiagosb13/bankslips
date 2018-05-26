package com.thiagobezerra.bankslips.api;

import java.util.Collection;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thiagobezerra.bankslips.model.BankSlip;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/bankslips")
public class RestController {
	@GetMapping
	Collection<BankSlip> getBankSlips() {

		return Collections.EMPTY_LIST;
	}
}
