package ohmicode.example.dao;

import ohmicode.example.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepositiry extends JpaRepository<Payment, Long> {
}
