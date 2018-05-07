/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.dto;

import ch.fhnw.bpm.api.data.domain.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mappings({@Mapping(target = "order.payment", ignore = true), @Mapping(target = "order.customer", ignore = true)})
    PaymentDTO entityToDto(PaymentEntity entity);
    @Mappings({@Mapping(target = "order.payment", ignore = true), @Mapping(target = "order.customer", ignore = true)})
    PaymentEntity dtoToEntity(PaymentDTO dto);

}
