package de.oth.PayPaul.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String getLoginView() {
    return "login";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String redirectToDefaultView() {
    return "redirect:/transactions";
  }
}

