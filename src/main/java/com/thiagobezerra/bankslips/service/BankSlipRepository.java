package com.thiagobezerra.bankslips.service;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiagobezerra.bankslips.model.BankSlip;

public interface BankSlipRepository extends JpaRepository<BankSlip, UUID> {
	
}
