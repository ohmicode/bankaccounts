package ohmicode.example.service;

import ohmicode.example.dao.AccountRepository;
import ohmicode.example.dao.PaymentRepository;
import ohmicode.example.model.Account;
import ohmicode.example.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public boolean deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            depositToAccount(account, amount);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null && account.getAmount().compareTo(amount)>=0) {
            withdrawFromAccount(account, amount);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean move(Long fromId, Long toId, BigDecimal amount) {
        Account accountFrom = accountRepository.findById(fromId).orElse(null);
        Account accountTo = accountRepository.findById(toId).orElse(null);
        if (accountFrom != null && accountTo != null && accountFrom.getAmount().compareTo(amount)>=0) {
            withdrawFromAccount(accountFrom, amount);
            depositToAccount(accountTo, amount);
            return true;
        }
        return false;
    }

    public String getState() {
        return accountRepository.findAll().stream()
                .map(a -> String.format("%s,%s,%s", a.getId(), a.getUser().getName(), a.getAmount()))
                .collect(Collectors.joining("; "));
    }

    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        return account == null ? BigDecimal.ZERO : account.getAmount();
    }


    private void depositToAccount(Account account, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setAmount(amount);
        paymentRepository.save(payment);

        account.setAmount(account.getAmount().add(amount));
        accountRepository.save(account);
    }

    private void withdrawFromAccount(Account account, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setAmount(amount.negate());
        paymentRepository.save(payment);

        account.setAmount(account.getAmount().subtract(amount));
        accountRepository.save(account);
    }
}
