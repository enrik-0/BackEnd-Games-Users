package edu.uclm.esi.ds.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.account.entities.Money;


public interface MoneyDAO extends JpaRepository<Money, String> {

	Money findByUser(String user_id);

}
