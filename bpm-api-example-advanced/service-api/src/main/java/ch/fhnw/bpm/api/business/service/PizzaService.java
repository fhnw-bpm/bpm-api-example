/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.service;

import ch.fhnw.bpm.api.business.dto.OrderDTO;
import ch.fhnw.bpm.api.business.dto.PaymentDTO;

import java.util.List;

public interface PizzaService {
    OrderDTO createOrder(OrderDTO order);

    PaymentDTO updatePaymentOfOrder(PaymentDTO payment, String businessKey);

    OrderDTO readOrder(long id);

    OrderDTO updateOrder(OrderDTO order);

    void deleteOrder(long id);

    PaymentDTO readPayment(long id);

    PaymentDTO updatePayment(PaymentDTO payment);

    void deletePayment(long id);

    List<OrderDTO> findAllOrders();

    List<OrderDTO> findAllOrdersOfCustomer(String email);

    List<OrderDTO> findAllUnpaidOrders();

    List<OrderDTO> findAllUnpaidOrdersCustomer(String email);
}
