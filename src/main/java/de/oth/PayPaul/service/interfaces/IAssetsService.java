package de.oth.PayPaul.service.interfaces;

import de.oth.PayPaul.persistence.model.BankAccount;
import de.oth.PayPaul.persistence.model.CreditCard;
import de.oth.PayPaul.persistence.model.PaymentMethod;

import java.util.List;
import java.util.Map;

public interface IAssetsService {
  public List<CreditCard> getAllCreditCardsForUser(String email);
  public List<BankAccount> getAllBankAccountsForUser(String email);
}
