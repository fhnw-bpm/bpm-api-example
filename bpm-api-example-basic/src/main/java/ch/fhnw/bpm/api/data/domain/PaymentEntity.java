/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue
    private long id;
    private String payment;
    private boolean receipt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp = new Date();
    @OneToOne(mappedBy = "payment")
    @JsonBackReference
    private OrderEntity order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public boolean isReceipt() {
        return receipt;
    }

    public void setReceipt(boolean receipt) {
        this.receipt = receipt;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
}
