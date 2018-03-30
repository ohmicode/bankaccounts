package ohmicode.example.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "ACCOUNT_SEQUENCE")
    @SequenceGenerator(name="ACCOUNT_SEQUENCE", sequenceName="ACCOUNT_SEQUENCE", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "number")
    private String number;

    @Column(name = "amount")
    private BigDecimal amount;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
