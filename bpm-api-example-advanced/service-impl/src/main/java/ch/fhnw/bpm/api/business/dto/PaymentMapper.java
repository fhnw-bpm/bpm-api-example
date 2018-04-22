/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.dto;

import ch.fhnw.bpm.api.data.domain.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDTO entityToDto(PaymentEntity entity);
    PaymentEntity dtoToEntity(PaymentDTO dto);

}
