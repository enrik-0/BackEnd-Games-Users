package edu.uclm.esi.ds.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.account.entities.Token;

public interface TokenDAO extends JpaRepository<Token, String> {

}
