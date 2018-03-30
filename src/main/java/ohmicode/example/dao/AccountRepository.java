package ohmicode.example.dao;

import ohmicode.example.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Deprecated
    @Query("SELECT sum(amount) FROM Payment WHERE account = ?1")
    BigDecimal calculateBalance(Account account);
}
