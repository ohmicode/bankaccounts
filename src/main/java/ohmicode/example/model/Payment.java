package ohmicode.example.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "PAYMENT_SEQUENCE")
    @SequenceGenerator(name="PAYMENT_SEQUENCE", sequenceName="PAYMENT_SEQUENCE", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "amount")
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
