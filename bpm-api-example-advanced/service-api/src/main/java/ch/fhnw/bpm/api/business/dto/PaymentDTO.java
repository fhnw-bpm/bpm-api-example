/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentDTO {

    private long id;
    private String payment;
    private boolean receipt;
    private Date creationTimestamp;
    private OrderDTO order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
