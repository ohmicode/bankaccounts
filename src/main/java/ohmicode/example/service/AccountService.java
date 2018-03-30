package ohmicode.example.service;

import ohmicode.example.dao.AccountRepository;
import ohmicode.example.dao.PaymentRepositiry;
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
    private PaymentRepositiry paymentRepositiry;

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
        BigDecimal balance = calculateBalance(account);
        if (account != null && balance.compareTo(amount)>=0) {
            withdrawFromAccount(account, amount);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean move(Long fromId, Long toId, BigDecimal amount) {
        Account accountFrom = accountRepository.findById(fromId).orElse(null);
        Account accountTo = accountRepository.findById(toId).orElse(null);
        BigDecimal balance = calculateBalance(accountFrom);
        if (accountFrom != null && accountTo != null && balance.compareTo(amount)>=0) {
            withdrawFromAccount(accountFrom, amount);
            depositToAccount(accountTo, amount);
            return true;
        }
        return false;
    }

    public String getState() {
        return accountRepository.findAll().stream()
                .map(a -> String.format("%s,%s,%s", a.getId(), a.getUser().getName(), calculateBalance(a)))
                .collect(Collectors.joining("; "));
    }

    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        return calculateBalance(account);
    }


    private BigDecimal calculateBalance(Account account) {
        BigDecimal balance = accountRepository.calculateBalance(account);
        return balance == null ? BigDecimal.ZERO : balance;
    }

    private void depositToAccount(Account account, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setAmount(amount);
        paymentRepositiry.save(payment);
    }

    private void withdrawFromAccount(Account account, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setAmount(amount.negate());
        paymentRepositiry.save(payment);
    }
}
