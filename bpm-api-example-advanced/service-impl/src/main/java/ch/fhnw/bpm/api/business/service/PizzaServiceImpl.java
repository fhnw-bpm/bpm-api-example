/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.service;

import ch.fhnw.bpm.api.business.dto.*;
import ch.fhnw.bpm.api.data.domain.CustomerEntity;
import ch.fhnw.bpm.api.data.domain.OrderEntity;
import ch.fhnw.bpm.api.data.domain.PaymentEntity;
import ch.fhnw.bpm.api.data.repository.CustomerRepository;
import ch.fhnw.bpm.api.data.repository.OrderRepository;
import ch.fhnw.bpm.api.data.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PizzaServiceImpl implements PizzaService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public OrderDTO createOrder(OrderDTO order){
        OrderEntity orderEntity = orderMapper.dtoToEntity(order);
        List<CustomerEntity> customerList = customerRepository.findByEmail(orderEntity.getCustomer().getEmail());
        if(!customerList.isEmpty()){
            orderEntity.getCustomer().setId(customerList.get(0).getId());
        }
        orderEntity.setCustomer(customerRepository.save(orderEntity.getCustomer()));
        return orderMapper.entityToDto(orderRepository.save(orderEntity));
    }

    @Override
    public PaymentDTO updatePaymentOfOrder(PaymentDTO payment, String businessKey){
        PaymentEntity paymentEntity = paymentMapper.dtoToEntity(payment);
        paymentEntity = paymentRepository.save(paymentEntity);
        List<OrderEntity> orderList = orderRepository.findByBusinessKey(businessKey);
        if(!orderList.isEmpty()){
            OrderEntity orderEntity = orderList.get(0);
            orderEntity.setPayment(paymentEntity);
            orderRepository.save(orderEntity);
        }
        return paymentMapper.entityToDto(paymentEntity);
    }

    @Override
    public OrderDTO readOrder(long id){
        return orderMapper.entityToDto(orderRepository.findById(id).orElse(null));
    }

    @Override
    public OrderDTO updateOrder(OrderDTO order){
        return orderMapper.entityToDto(orderRepository.save(orderMapper.dtoToEntity(order)));
    }

    @Override
    public void deleteOrder(long id){
        orderRepository.deleteById(id);
    }

    @Override
    public PaymentDTO readPayment(long id){
        return paymentMapper.entityToDto(paymentRepository.findById(id).orElse(null));
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO payment){
        return paymentMapper.entityToDto(paymentRepository.save(paymentMapper.dtoToEntity(payment)));
    }

    @Override
    public void deletePayment(long id){
        paymentRepository.deleteById(id);
    }

    @Override
    public List<OrderDTO> findAllOrders(){
        return orderMapper.entitiesToDtos(orderRepository.findAll());
    }

    @Override
    public List<OrderDTO> findAllOrdersOfCustomer(String email){
        return orderMapper.entitiesToDtos(orderRepository.findByCustomerEmail(email));
    }

    @Override
    public List<OrderDTO> findAllUnpaidOrders(){
        return orderMapper.entitiesToDtos(orderRepository.findByPaymentIsNull());
    }

    @Override
    public List<OrderDTO> findAllUnpaidOrdersCustomer(String email){
        return orderMapper.entitiesToDtos(orderRepository.findByCustomerEmailAndPaymentIsNull(email));
    }

}
