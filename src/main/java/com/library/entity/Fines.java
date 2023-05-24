package com.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fines {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private long fines;
	
	private int fineAmount;

	public long getFines() {
		return fines;
	}

	public void setFines(long fines) {
		this.fines = fines;
	}

	public int getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(int fineAmount) {
		this.fineAmount = fineAmount;
	}

	public Fines() {
		super();
	}

	public Fines(long fines, int fineAmount) {
		super();
		this.fines = fines;
		this.fineAmount = fineAmount;
	}

	@Override
	public String toString() {
		return "Fines [fines=" + fines + ", fineAmount=" + fineAmount + "]";
	}
	
}

