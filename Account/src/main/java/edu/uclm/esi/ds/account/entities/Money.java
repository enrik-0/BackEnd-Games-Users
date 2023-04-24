package edu.uclm.esi.ds.account.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(
		name = "money",
		indexes = {
				@Index(columnList = "user_id", unique = true)
		})
public class Money {
	
	@Id @Column(length = 36)
	private String id;
	@OneToOne
	private User user;
	
	@Column
	private int amount;
	
	

}
