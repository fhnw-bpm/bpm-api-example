/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {

    private long id;
    private String pizzaType;
    private String pizzaSize;
    private String pizzaSauce;
    private String pizzaCrust;
    private String pizzaTopping;
    private String pizzaPrice;
    private String businessKey;
    private Date creationTimestamp;
    private CustomerDTO customer;
    private PaymentDTO payment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
}
