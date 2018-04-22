/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.data.repository;

import ch.fhnw.bpm.api.data.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByBusinessKey(@Param("businessKey") String businessKey);
    List<OrderEntity> findByCustomerEmail(@Param("email") String email);
    List<OrderEntity> findByPaymentIsNull();
    List<OrderEntity> findByCustomerEmailAndPaymentIsNull(@Param("email") String email);
}
