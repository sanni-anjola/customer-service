package com.example.customerservice.repository;

import com.example.customerservice.model.BillingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingDetailRepository extends JpaRepository<BillingDetail, Long> {
}
