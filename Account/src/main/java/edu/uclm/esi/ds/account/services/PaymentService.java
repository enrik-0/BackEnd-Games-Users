package edu.uclm.esi.ds.account.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.account.dao.MoneyDAO;
import edu.uclm.esi.ds.account.entities.Money;
import edu.uclm.esi.ds.account.entities.User;

@Service
public class PaymentService {

	@Autowired
	private MoneyDAO moneyDAO;

	public Money getMoneyByUser(User user) {
		return moneyDAO.findByUser(user.getId());
	}

}
