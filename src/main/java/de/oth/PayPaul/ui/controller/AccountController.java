package de.oth.PayPaul.ui.controller;

import de.oth.PayPaul.persistence.model.Account;
import de.oth.PayPaul.service.implementation.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {
  AccountService accountService;

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String getRegisterView(Model model) {
    model.addAttribute("account", new Account());
    return "register";
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String register(Model model, @ModelAttribute("account") Account account) {
    return "";
  }
}
