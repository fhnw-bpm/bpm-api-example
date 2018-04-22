/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.data.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class OrderEntity {

    @Id
    @GeneratedValue
    private long id;
    private String pizzaType;
    private String pizzaSize;
    private String pizzaSauce;
    private String pizzaCrust;
    private String pizzaTopping;
    private String pizzaPrice;
    private String businessKey;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp = new Date();
    @ManyToOne
    private CustomerEntity customer;
    @OneToOne
    private PaymentEntity payment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPizzaType() {
        return pizzaType;
    }

    public void setPizzaType(String pizzaType) {
        this.pizzaType = pizzaType;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public String getPizzaSauce() {
        return pizzaSauce;
    }

    public void setPizzaSauce(String pizzaSauce) {
        this.pizzaSauce = pizzaSauce;
    }

    public String getPizzaCrust() {
        return pizzaCrust;
    }

    public void setPizzaCrust(String pizzaCrust) {
        this.pizzaCrust = pizzaCrust;
    }

    public String getPizzaTopping() {
        return pizzaTopping;
    }

    public void setPizzaTopping(String pizzaTopping) {
        this.pizzaTopping = pizzaTopping;
    }

    public String getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(String pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
}
