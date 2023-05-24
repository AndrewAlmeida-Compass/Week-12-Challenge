# Compass Uol - week 12 challenge

> 2 microservices for an online store.

## About the services

#### Order service
- <b>port</b>: 8080
- <b>database</b>: mongodb
- <b>port</b>: 27017
- <b>database name</b>: mydb_mongo
- <b>username</b>: root
- <b>password</b>: root

#### Payment service
- <b>port</b>: 8081
- <b>database</b>: postgre
- <b>port: 27017
- <b>database name</b>: mydb_postgre
- <b>username</b>: root
- <b>password</b>: root

#### Rabbitmq
- <b>port</b>: 5672 
- <b>username</b>: guest 
- <b>password</b>: guest

## Endpoints

#### Order Service

- [POST] /api/order - create an order
- [GET] /api/order - get all orders
- [GET] /api/order/{id} - get one order by id
- [PUT] /api/order/{id} - update an order
- [DELETE] /api/order/{id} - delete an order
- [POST] /api/item - create an item
- [PUT] /api/item/{id} - update an item 

#### Payment service
- [GET] /api/payment - get all payments

## Instructions on how to use

After an order has been created by the order service, automatically a payment is created by the payment service, initially the order is created with status "IN_PROGRESS" and the payment is created with status "PROCESSING".
You can change the payment status by creating a message in RabbitMQ in the Payment queue, this will update the payment and order status.

> Payload example to update a payment/order status<br><br>
> {<br>
    "orderId": 213, <br>
    "totalOrder": 119.40,<br>
    "paymentStatus": "PROCESSING"<br>
> }

After updating a payment to "PROCESSING" the order is updated to "IN_PROGRESS",
changing payment status "APPROVED" updates order to "FINISHED" and 
finally changing the payment status to "REJECTED" puts the order on "CANCELED" status.

## Instruções para Download/Run

Clone/baixe o repositório e abra as duas pastas (order-microservice/ e payment-microservice/) em seu editor (utilizei o IntelliJ),
clique em Run nas duas classes principais de cada microsserviço (OrderMicroserviceApplication e PaymentMicroserviceApplication).

É necessário os serviços externos já estarem em execução (mongoDB, postgre e rabbitMQ) e o java versão 17 instalado.

## Instructors

<table>
  <tr>
    <td align="center">
        <img src="https://avatars.githubusercontent.com/u/31413079" width="100px;"/><br>
        <sub>
          <b>Anderson Matte</b>
        </sub>
    </td>
    <td align="center">
        <img src="https://avatars.githubusercontent.com/u/83078469" width="100px;" alt="Foto do Mark Zuckerberg"/><br>
        <sub>
          <b>Erika Keisy</b>
        </sub>
    </td>
    <td align="center">
        <img src="https://avatars.githubusercontent.com/u/34167149?s=64&v=4" width="100px;" alt="Foto do Steve Jobs"/><br>
        <sub>
          <b>Cassiano Jovino</b>
        </sub>
    </td>
    <td align="center">
        <img src="https://avatars.githubusercontent.com/u/67009807" width="100px;" alt="Foto do Steve Jobs"/><br>
        <sub>
          <b>Juliane Maran</b>
        </sub>
    </td>
    <td align="center">
        <img src="https://avatars.githubusercontent.com/u/79537802" width="100px;" alt="Foto do Steve Jobs"/><br>
        <sub>
          <b>Mateus de Oliveira</b>
        </sub>
    </td>
    <td align="center">
        <img src="https://avatars.githubusercontent.com/u/84927709" width="100px;" alt="Foto do Steve Jobs"/><br>
        <sub>
          <b>Yasmin Wichinievski</b>
        </sub>
    </td>
  </tr>
</table>

[⬆ back to the top](#compass-uol---week-12-challenge)<br>