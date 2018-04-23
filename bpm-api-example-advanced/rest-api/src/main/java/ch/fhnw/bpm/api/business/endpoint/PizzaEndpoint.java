/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.bpm.api.business.endpoint;

import ch.fhnw.bpm.api.business.dto.CustomerDTO;
import ch.fhnw.bpm.api.business.dto.OrderDTO;
import ch.fhnw.bpm.api.business.dto.PaymentDTO;
import ch.fhnw.bpm.api.business.service.PizzaService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(path = "/api/pizza/v1")
public class PizzaEndpoint {
    @Autowired
    private PizzaService pizzaService;

    private static final ModelMapper modelMapper = new ModelMapper();

    @PostMapping(path = "/order", consumes = "application/json", produces = "application/json")
    public ResponseEntity postOrder(@RequestBody OrderRequest order) {
        HttpHeaders headers = new HttpHeaders();
        OrderDTO orderDTO = pizzaService.createOrder(order.getOrderDTO());
        headers.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orderDTO.getId()).toUri());
        return new ResponseEntity<>(new PizzaResponse(orderDTO.getId()), headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/order", produces = "application/json")
    public List<OrderDTO> getOrders(@RequestParam(defaultValue = "false") boolean unpaidOnly, @RequestParam(required = false) String customerEmail) {
        if(customerEmail != null) {
            if (unpaidOnly)
                return pizzaService.findAllUnpaidOrdersCustomer(customerEmail);
            return pizzaService.findAllOrdersOfCustomer(customerEmail);
        }
        if (unpaidOnly)
            return pizzaService.findAllUnpaidOrders();
        return pizzaService.findAllOrders();
    }

    @GetMapping(path = "/order/{id}", produces = "application/json")
    public OrderDTO getOrder(@PathVariable(value = "id") long id) {
        return pizzaService.readOrder(id);
    }

    @PatchMapping (path = "/order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchOrder(@PathVariable(value = "id") long id, @RequestBody OrderRequest order) {
        OrderDTO orderDTO = order.getOrderDTO();
        orderDTO.setId(id);
        pizzaService.updateOrder(orderDTO);
    }

    @DeleteMapping(path = "/order/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteOrder(@PathVariable(value = "id") long id) {
        pizzaService.deleteOrder(id);
    }

    @PostMapping(path = "/payment/{businessKey}", consumes = "application/json", produces = "application/json")
    public ResponseEntity postPayment(@RequestBody PaymentRequest payment, @PathVariable(value = "businessKey") String businessKey, UriComponentsBuilder uriComponentsBuilder) {
        HttpHeaders headers = new HttpHeaders();
        PaymentDTO paymentDTO = pizzaService.updatePaymentOfOrder(payment.getPaymentDTO(), businessKey);
        headers.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(paymentDTO.getId()).toUri());
        return new ResponseEntity<>(new PizzaResponse(paymentDTO.getId()), headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/payment/{id}", produces = "application/json")
    public PaymentDTO getPayment(@PathVariable(value = "id") long id) {
        return pizzaService.readPayment(id);
    }

    @PatchMapping (path = "/payment/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchOrder(@PathVariable(value = "id") long id, @RequestBody PaymentRequest payment) {
        PaymentDTO paymentDTO = payment.getPaymentDTO();
        paymentDTO.setId(id);
        pizzaService.updatePayment(paymentDTO);
    }

    @DeleteMapping(path = "/payment/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePayment(@PathVariable(value = "id") long id) {
        pizzaService.deletePayment(id);
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

        OrderDTO getOrderDTO() {
            CustomerDTO customerDTO = modelMapper.map(this, CustomerDTO.class);
            OrderDTO orderDTO = modelMapper.map(this, OrderDTO.class);
            orderDTO.setCustomer(customerDTO);
            return orderDTO;
        }
    }

    @Data
    private static class PaymentRequest {
        private String payment;
        private boolean receipt;

        PaymentDTO getPaymentDTO() {
            return modelMapper.map(this, PaymentDTO.class);
        }
    }

    @Data
    private static class PizzaResponse<T> {
        private long id;

        PizzaResponse(long id) {
            this.id = id;
        }
    }
}


