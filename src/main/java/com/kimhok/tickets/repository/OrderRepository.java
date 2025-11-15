package com.kimhok.tickets.repository;

import com.kimhok.tickets.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    @Query("SELECT SUM(o.subtotal) FROM Order o")
    BigDecimal getTotalSubtotal();

    @Query("SELECT SUM(o.transactionFee) FROM Order o")
    BigDecimal getTotalTransactionFee();

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal getTotalIncome();

}
