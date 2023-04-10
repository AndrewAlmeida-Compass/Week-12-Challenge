package br.com.compassuol.challenge.order_microservice.controllers;

import br.com.compassuol.challenge.order_microservice.services.implementations.ItemServiceImp;
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
class ItemControllerTest {
    @InjectMocks
    ItemController itemController;

    @Mock
    ItemServiceImp itemServiceImp;
    MockMvc mockMvc;
    String entityTest = """
                {
                    "name": "item name",
                    "description": "item description",
                    "creationDate": "2000-08-03",
                    "amount": 29
                }""";

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    void registerItem() throws Exception {
        mockMvc.perform(
                post("/api/item")
                        .content(entityTest)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception {
        mockMvc.perform(
                put("/api/item/10")
                        .content(entityTest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}