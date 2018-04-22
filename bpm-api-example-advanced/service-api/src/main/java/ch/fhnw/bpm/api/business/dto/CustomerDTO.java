/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private List<OrderDTO> orders;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
