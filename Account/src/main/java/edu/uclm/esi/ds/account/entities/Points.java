package edu.uclm.esi.ds.account.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(
		schema = "account",
		name = "points",
		indexes = {
				@Index(columnList = "user_id", unique = true)
		})
public class Points {
	
	@Id
	 private int id;
	@OneToOne
	private User user;
	
	@Column
	private int amount;

	public int getAmount() {
		return amount;
	}
	
	

}
