package de.oth.PayPaul.ui.controller;

import de.oth.PayPaul.service.implementation.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {
  AccountService accountService;

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(value = "/register")
  public String register(Model model) {
    return "register";
  }
}
