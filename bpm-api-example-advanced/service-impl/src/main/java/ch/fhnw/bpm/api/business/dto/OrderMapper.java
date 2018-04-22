/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.dto;

import ch.fhnw.bpm.api.data.domain.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings(@Mapping(target = "customer.orders", ignore = true))
    OrderDTO entityToDto(OrderEntity entity);
    @Mappings(@Mapping(target = "customer.orders", ignore = true))
    OrderEntity dtoToEntity(OrderDTO dto);
    @Mappings(@Mapping(target = "customer.orders", ignore = true))
    List<OrderDTO> entitiesToDtos(List<OrderEntity> entities);
}
