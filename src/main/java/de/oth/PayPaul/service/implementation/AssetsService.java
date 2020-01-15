package de.oth.PayPaul.service.implementation;

import de.oth.PayPaul.persistence.model.Account;
import de.oth.PayPaul.persistence.model.BankAccount;
import de.oth.PayPaul.persistence.model.CreditCard;
import de.oth.PayPaul.persistence.model.PaymentMethod;
import de.oth.PayPaul.persistence.repository.AccountRepository;
import de.oth.PayPaul.persistence.repository.PaymentMethodRepository;
import de.oth.PayPaul.service.interfaces.IAssetsService;
import net.bytebuddy.utility.JavaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssetsService implements IAssetsService {
  private PaymentMethodRepository paymentMethodRepo;
  private AccountRepository accountRepo;

  @Autowired
  public void setRepo(PaymentMethodRepository paymentMethodRepo) {
    this.paymentMethodRepo = paymentMethodRepo;
  }

  @Autowired
  public void setAccountRepo(AccountRepository accountRepo) {
    this.accountRepo = accountRepo;
  }


  @Override
  public List<CreditCard> getAllCreditCardsForUser(String email) {
    List<CreditCard> creditCards = paymentMethodRepo.findAllCreditCardsForUser(email);
    creditCards.forEach(x -> {
      if (x.getPayLimit() == Integer.MAX_VALUE)
        x.setPayLimit(null);
    });
    return creditCards;
  }

  @Override
  public List<BankAccount> getAllBankAccountsForUser(String email) {
    List<BankAccount> bankAccounts = paymentMethodRepo.findAllBankAccountsForUser(email);
    bankAccounts.forEach(x -> {
      if (x.getPayLimit() == Integer.MAX_VALUE)
        x.setPayLimit(null);
    });
    return bankAccounts;
  }

  @Override
  @Transactional
  public void createNewBankAccountForUser(String email, BankAccount bankAccount) throws Exception {
    if(!paymentMethodRepo.bankAccountExists(email, bankAccount.getIBAN())) {
        Account account = accountRepo.findById(email).orElseThrow(
                () -> new Exception("Authentifizierungsproblem")
        );
        if (bankAccount.getPayLimit() == null)
          bankAccount.setPayLimit(Integer.MAX_VALUE);
        paymentMethodRepo.save(bankAccount);
        account.addPaymentMethod(bankAccount);
    }
  }

  @Override
  @Transactional
  public void createNewCreditCardForUser(String email,CreditCard creditCard) throws Exception {
    if(!paymentMethodRepo.creditCardExists(email, creditCard.getCardNumber())) {
      Account account = accountRepo.findById(email).orElseThrow(
              () -> new Exception("Authentifizierungsproblem")
      );
      if (creditCard.getPayLimit() == null)
        creditCard.setPayLimit(Integer.MAX_VALUE);
      paymentMethodRepo.save(creditCard);
      account.addPaymentMethod(creditCard);
    }
  }

  @Override
  @Transactional
  public void activateMethodWithId(String email, int id) {
    PaymentMethod paymentMethod = paymentMethodRepo.findPaymentMethodByIdFromUser(email, id);
    paymentMethod.setActive(true);
  }

  @Override
  @Transactional
  public void deactivateMethodWithId(String email, int id) {
    PaymentMethod paymentMethod = paymentMethodRepo.findPaymentMethodByIdFromUser(email, id);
    paymentMethod.setActive(false);
  }

  @Override
  @Transactional
  public void chargeCredit(String email, int amount) {
    Account currUser = accountRepo.findByEmail(email);
    currUser.addCredit(amount);
  }
}
