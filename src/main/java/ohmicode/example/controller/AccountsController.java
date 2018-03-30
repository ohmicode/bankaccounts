package ohmicode.example.controller;

import ohmicode.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@RestController
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/")
    public String index() {
        return "use POST methods /deposit /withdraw /move";
    }

    @PostMapping("/deposit/{accountId}/{amount}")
    public String deposit(@PathVariable Long accountId, @PathVariable BigDecimal amount, HttpServletResponse response) {
        if (accountService.deposit(accountId, amount)) {
            return "success";
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "account not found";
        }
    }

    @PostMapping("/withdraw/{accountId}/{amount}")
    public String withdraw(@PathVariable Long accountId, @PathVariable BigDecimal amount, HttpServletResponse response) {
        if (accountService.withdraw(accountId, amount)) {
            return "success";
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "account not found or has not enough money";
        }
    }

    @PostMapping("/move/{fromId}/{toId}/{amount}")
    public String move(@PathVariable Long fromId, @PathVariable Long toId, @PathVariable BigDecimal amount, HttpServletResponse response) {
        if (accountService.move(fromId, toId, amount)) {
            return "success";
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "account not found or has not enough money";
        }
    }

    @GetMapping("/state")
    public String getState() {
        return accountService.getState();
    }

    @GetMapping("/balance/{accountId}")
    public String getBalance(@PathVariable Long accountId) {
        return accountService.getBalance(accountId).toString();
    }

}
