package br.com.compassuol.challenge.order_microservice.controllers;

import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import br.com.compassuol.challenge.order_microservice.services.implementations.OrderServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;
    @Mock
    OrderServiceImp orderServiceImp;
    @Mock
    OrderResponse orderResponse;
    MockMvc mockMvc;

    String orderEntity = """
            {
                "cpf": "22245678911",
                "items": [
                    {
                        "name": "item name",
                        "description": "item description",
                        "creationDate": "2000-08-03",
                        "amount": "19.9"
                    },
                    {
                        "name": "item name2",
                        "description": "item description",
                        "creationDate": "2000-08-03",
                        "amount": 99.5
                    }
                    ]
            }""";

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(
                get("/api/order")
        ).andExpect(status().isOk());
    }

    @Test
    void registerOrder() {

    }

    @Test
    void getById() throws Exception {
        mockMvc.perform(
                get("/api/order/10")
        ).andExpect(status().isOk());
    }

    @Test
    void updateOrder() throws Exception {
        mockMvc.perform(
                put("/api/order/10")
                        .content(orderEntity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteOrder() throws Exception {
        mockMvc.perform(
                delete("/api/order/10")
                        .content(orderEntity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void updatePayment() {
    }
}