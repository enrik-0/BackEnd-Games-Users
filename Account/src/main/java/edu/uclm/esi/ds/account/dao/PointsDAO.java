package edu.uclm.esi.ds.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.account.entities.Points;


public interface PointsDAO extends JpaRepository<Points, String> {

	Points findByUser(String user_id);

}
