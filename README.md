# BPM API Example

#### Contents:
- [Analysis](#analysis)
- [Design](#design)
    - [API Design](#api-design)
    - [Architecture](#architecture)
- [Application Bootstrapping](#application-bootstrapping)
- [How-to](#how-to)
- [Data Access Layer](#data-access-layer)
    - [Domain Objects](#domain-objects)
    - [Repositories](#repositories)
- [Service Layer](#service-layer)
    - [API Documentation](#api-documentation)
- [Microservice Application](#microservice-application)

## Analysis

This exemplary API is part of the Food Inc. system. The API can be used by an external API client, where it can create some orders, manage the payments and retrieve unpaid orders as depicted in the following **UML Use Case** model:

![ch.fhnw.bpm.usecase](images/ch.fhnw.bpm.usecase.png)

- UC-1 [Manage Orders]: An API client can create and, if not exist, create customer data (UC-4) when an order is placed.
- UC-2 [Manage Payments]: An API client can store payments of an order (UC-1) respectively of a customer (UC-4).
- UC-3 [Retrieve Unpaid Orders]: An API client can retrieve all unpaid orders or unpaid orders of a customer (UC-4).
- UC-4 [Manage Customers]: At this stage, the user management is not accessible from an external actor. However, customer management is required other use cases (UC-1 & UC-3).

## Design

This exemplary microservice as part of the Food Inc. system provides a minimal and basic API as defined in the previous use case model.

### API Design

As depicted in the following image, the API provides very basic methods and is operating with API specific request and response bodies:

![ch.fhnw.bpm.api](images/ch.fhnw.bpm.api.png)

### Architecture

For simplification, this API relies on a two-layer architecture comprising business and service layer combined and a data access (sometimes called persistence) layer as depicted in the following diagram. Usually, the business and service layer is separated. And since the database is seldom considered as belonging to the same tier, it is sometimes not disclosed on a layer.

![ch.fhnw.bpm.architecture](images/ch.fhnw.bpm.architecture.png)

## Application Bootstrapping

This exemplary application is relying on [Spring Boot](https://projects.spring.io/spring-boot), which is the convention-over-configuration solution of the [Spring](https://spring.io) framework for creating stand-alone, production-grade Applications that you can "just run". In detail the application is based on the following:

- [Spring Boot](https://projects.spring.io/spring-boot)
- [Spring Web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
- [Spring Data](https://projects.spring.io/spring-data)
- [Java Persistence API (JPA)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html)
- [H2 Database Engine](https://www.h2database.com)
- [Project Lombok](https://projectlombok.org/)

Please use the Spring Initializr to bootstrap the application on https://start.spring.io based on the following depicted configuration:

![](images/2018-04-22_08h58_54.png)

Download the ZIP-file and extract it somewhere. Then import the project into your favourite Java/Maven IDE such as IntelliJ, NetBeans or Eclipse.

> In Eclipse use `File > Import > Existing Maven Projects` to import your bootstrapped project.

## How-to

- How to compile your project from Eclipse: `Run > Run As > 3 Maven build` using `clean compile` Maven `Goals`.
- How to run your project from Eclipse: `Run > Run As > Java Application` using `BpmApiExampleBasicApplication` main class.
- How to import packages in Eclipse: `Source > Organize Imports`
- How to generate getters and setters in Eclipse: `Source > Generate Getters and Setters`

## Data Access Layer

The data access layer is implemented using [Java Persistence API (JPA)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html) and [Spring Data](https://projects.spring.io/spring-data).

### Domain Objects

The [Java Persistence API (JPA)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html) is providing the following annotations to realize object relational mapping (ORM):

- `@Entity` tells the entity / ORM framework to consider this class as an entity.
- `@Id` defines an attribute to be a primary key that will be automatically generated by using `@GeneratedValue`.
- `@OneToMany` defines an attribute that represents one-to-many or `@OneToOne` for one-to-one entity relationships, which is maybe `mappedBy` the bi-directional relationship indicted by `@ManyToOne` respectively `@OneToOne`.
- `@Temporal(TemporalType.TIMESTAMP)` defines a temporal attribute, which is in this case of type timestamp.
- `@JsonBackReference` is used to prevent infinite JSON serialisation by a bi-directional relationship.

Create the `ch.fhnw.bpm.api.data.domain` package and in this package following domain objects including getters and setters:

```Java
@Entity
public class CustomerEntity {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    @OneToMany(mappedBy = "customer")
    @JsonBackReference
    private List<OrderEntity> orders;
    // getter & setter
}
```

```Java
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
    // getter & setter
}
```

```Java
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
    // getter & setter
}
```

### Repositories

Create the `ch.fhnw.bpm.api.data.repository` package and in this package following repository interfaces:

```Java
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    List<CustomerEntity> findByEmail(@Param("email") String email);
}
```

```Java
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByBusinessKey(@Param("businessKey") String businessKey);
    List<OrderEntity> findByCustomerEmail(@Param("email") String email);
    List<OrderEntity> findByPaymentIsNull();
    List<OrderEntity> findByCustomerEmailAndPaymentIsNull(@Param("email") String email);
}
```

```Java
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {}
```

## Service Layer

```Java
@RestController
@RequestMapping(value = "/api/pizza/v1")
public class PizzaEndpoint {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /* ... */

    @Data private static class OrderRequest { /* ... */ }

    @Data private static class PaymentRequest { /* ... */ }

    @Data private static class PizzaResponse { /* ... */ }
}
```

```Java
@Data
private static class PizzaResponse {
    private long id;

    PizzaResponse(long id) {
        this.id = id;
    }
}
```

```Java
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
```

```Java
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
```

```Java
@RestController
@RequestMapping(value = "/api/pizza/v1")
public class PizzaEndpoint {
    /* ... */
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
    /* ... */
}
```

```Java
@RestController
@RequestMapping(value = "/api/pizza/v1")
public class PizzaEndpoint {
    /* ... */
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
    /* ... */
}
```

```Java
@RestController
@RequestMapping(value = "/api/pizza/v1")
public class PizzaEndpoint {
    /* ... */
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
    /* ... */
}
```

### API Documentation

```XML
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.8.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.8.0</version>
</dependency>
```

```Java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }
}
```

## Microservice Application

`src/main/java/resources/application.properties`

```properties
server.port=8081
#spring.datasource.url=jdbc:h2:file:./data/pizza
#spring.datasource.username=sa
#spring.datasource.password=sa
#spring.h2.console.enabled=true
#spring.h2.console.path=/console
```