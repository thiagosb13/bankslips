package com.thiagobezerra.bankslips.model;

public class StatusWrapper {
	private Status status;
	
	public StatusWrapper() {}
	
	private StatusWrapper(Status status) {
		this.status = status;
	}
	
	public static StatusWrapper PaidStatus() {
		return new StatusWrapper(Status.PAID);
	}

	public static StatusWrapper CancelStatus() {
		return new StatusWrapper(Status.CANCELED);
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
