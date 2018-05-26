package com.thiagobezerra.bankslips.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class BankSlip {
	private UUID id;
	private Date dueDate;
	private BigDecimal totalInCents;
	private String customer;
	private Status status;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(BigDecimal totalInCents) {
		this.totalInCents = totalInCents;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
