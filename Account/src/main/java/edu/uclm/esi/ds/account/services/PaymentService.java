package edu.uclm.esi.ds.account.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.account.dao.PointsDAO;
import edu.uclm.esi.ds.account.entities.Points;
import edu.uclm.esi.ds.account.entities.User;

@Service
public class PaymentService {

	@Autowired
	private PointsDAO moneyDAO;

	public Points getMoneyByUser(User user) {
		return moneyDAO.findByUser(user.getId());
	}

}
