package de.oth.PayPaul.ui.controller;

import de.oth.PayPaul.persistence.model.PaymentNotification;
import de.oth.PayPaul.service.implementation.NotificationService;
import de.oth.PayPaul.service.interfaces.INotificationService;
import de.oth.PayPaul.ui.model.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentNotificationController {

  private INotificationService notificationService;

  @Autowired
  public void setInterface(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @RequestMapping(value = "/paymentNotifications", method = RequestMethod.GET)
  public String getPaymentNotificationsView(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("notifications", notificationService.getAllNotificationsForUser(auth.getName()));
    return "paymentNotifications";
  }

  @RequestMapping(value = "/paymentNotifications/addNew", method = RequestMethod.GET)
  public String getNewPaymentNotificationView(Model model) {
    model.addAttribute("paymentNotification", new PaymentNotification());
    return "newPaymentNotification";
  }

  @RequestMapping(value = "/paymentNotifications/addNew", method = RequestMethod.POST)
  public String newPaymentNotification(@ModelAttribute("paymentNotification")PaymentNotification paymentNotification,
                                       RedirectAttributes redirectAttributes) {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      notificationService.createNewNotificationForUser(auth.getName(), paymentNotification);
      redirectAttributes.addFlashAttribute("successMessage",
              new CustomResponse("Neue Payment Notification wurde erstellt und aktiviert.",
                      "Sie erhalten Benachrichtigungen zu Ihren Zahlungen."));
      return "redirect:/paymentNotifications";
    } catch(Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage",
              new CustomResponse("Fehler! Payment Notification konnte nicht hinzugefügt werden.", e.getMessage()));
      return "redirect:/paymentNotifications/addNew";
    }
  }

  @RequestMapping(value = "/paymentNotifications/activateNotification")
  public ResponseEntity<String> activateNotificationWithId(@RequestParam int id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    notificationService.activateNotificationWithId(auth.getName(), id);
    return new ResponseEntity<>("activated " + id, HttpStatus.OK);
  }

  @RequestMapping(value = "/paymentNotifications/deactivateNotification")
  public ResponseEntity<String> deactivateNotificationWithId(@RequestParam int id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    notificationService.deactivateNotificationWithId(auth.getName(), id);
    return new ResponseEntity<>("deactivated " + id, HttpStatus.OK);
  }
}
