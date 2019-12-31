package de.oth.PayPaul.service.interfaces;

import de.oth.PayPaul.persistence.model.Account;

public interface IAccountService {
  public void createNewAccount(Account account) throws Exception;
  public int getCreditByEmail(String email);
}
