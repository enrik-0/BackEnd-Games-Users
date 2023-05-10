package edu.uclm.esi.ds.account.entities;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
	schema = "account",
	name = "tokens"
)
public class Token {
	@Id @Column(length = 36)
	private String id;
	@Column @NotEmpty
	private Long creationTime;
	@ManyToOne
	private User user;
	
	public Token() {
		this.id = UUID.randomUUID().toString();
		this.creationTime = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
