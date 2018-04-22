/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.endpoint;

import ch.fhnw.bpm.api.data.domain.CustomerEntity;
import ch.fhnw.bpm.api.data.domain.OrderEntity;
import ch.fhnw.bpm.api.data.domain.PaymentEntity;
import ch.fhnw.bpm.api.data.repository.CustomerRepository;
import ch.fhnw.bpm.api.data.repository.OrderRepository;
import ch.fhnw.bpm.api.data.repository.PaymentRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/pizza/v1")
public class PizzaEndpoint {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping(path = "/order", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public PizzaResponse postOrder(@RequestBody OrderRequest order, UriComponentsBuilder uriComponentsBuilder) {
        OrderEntity orderEntity = order.getOrderEntity();
        List<CustomerEntity> customerList = customerRepository.findByEmail(orderEntity.getCustomer().getEmail());
        if(!customerList.isEmpty()){
            orderEntity.getCustomer().setId(customerList.get(0).getId());
        }
        orderEntity.setCustomer(customerRepository.save(orderEntity.getCustomer()));
        return new PizzaResponse(orderRepository.save(orderEntity).getId());
    }

    @GetMapping(path = "/order", produces = "application/json")
    public List<OrderEntity> getOrders(@RequestParam(defaultValue = "false") boolean unpaidOnly, @RequestParam(required = false) String customerEmail) {
        if(customerEmail != null) {
            if (unpaidOnly)
                return orderRepository.findByCustomerEmailAndPaymentIsNull(customerEmail);
            return orderRepository.findByCustomerEmail(customerEmail);
        }
        if (unpaidOnly)
            return orderRepository.findByPaymentIsNull();
        return orderRepository.findAll();
    }

    @PostMapping(path = "/payment/{businessKey}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public PizzaResponse postPayment(@RequestBody PaymentRequest payment, @PathVariable(value = "businessKey") String businessKey) {
        PaymentEntity paymentEntity = payment.getPaymentEntity();
        paymentEntity = paymentRepository.save(paymentEntity);
        List<OrderEntity> orderList = orderRepository.findByBusinessKey(businessKey);
        if(!orderList.isEmpty()){
            OrderEntity orderEntity = orderList.get(0);
            orderEntity.setPayment(paymentEntity);
            orderRepository.save(orderEntity);
        }
        return new PizzaResponse(paymentEntity.getId());
    }

    @Data
    private static class OrderRequest {
        private String pizzaType;
        private String pizzaSize;
        private String pizzaSauce;
        private String pizzaCrust;
        private String pizzaTopping;
        private String pizzaPrice;
        private String businessKey;
        private String firstName;
        private String lastName;
        private String address;
        private String email;

        OrderEntity getOrderEntity() {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setFirstName(this.firstName);
            customerEntity.setLastName(this.lastName);
            customerEntity.setAddress(this.address);
            customerEntity.setEmail(this.email);
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setPizzaType(this.pizzaType);
            orderEntity.setPizzaSize(this.pizzaSize);
            orderEntity.setPizzaSauce(this.pizzaSauce);
            orderEntity.setPizzaCrust(this.pizzaCrust);
            orderEntity.setPizzaTopping(this.pizzaTopping);
            orderEntity.setPizzaPrice(this.pizzaPrice);
            orderEntity.setBusinessKey(this.businessKey);
            orderEntity.setCustomer(customerEntity);
            return orderEntity;
        }
    }

    @Data
    private static class PaymentRequest {
        private String payment;
        private boolean receipt;

        PaymentEntity getPaymentEntity() {
            PaymentEntity paymentEntity = new PaymentEntity();
            paymentEntity.setPayment(this.payment);
            paymentEntity.setReceipt(this.receipt);
            return paymentEntity;
        }
    }

    @Data
    private static class PizzaResponse {
        private long id;

        PizzaResponse(long id) {
            this.id = id;
        }
    }
}


