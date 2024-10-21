package com.spring.tradeflow.controller.tests.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.tradeflow.model.entities.client.Address;
import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.client.Telephone;
import com.spring.tradeflow.utils.enums.client.Gender;
import com.spring.tradeflow.utils.enums.client.States;
import com.spring.tradeflow.utils.enums.client.TelephoneType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllClients() throws Exception {
        mockMvc.perform(get("/api/client/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testCreateClient() throws Exception {
        Address address = new Address(
                "Cidade teste", States.AM, "Rua teste"
        );
        Telephone phone = new Telephone(
                "12", "123456789", TelephoneType.MOBILE
        );
        List<Telephone> telephones = new ArrayList<>();
        telephones.add(phone);
        Client client = new Client(
                "email@test.com", "Teste nome", "Teste sobrenome",
                "@senha123", address, LocalDate.now(), Gender.FEMALE, telephones
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String clientJson = mapper.writeValueAsString(client);


        mockMvc.perform(post("/api/client/create").contentType("application/json").content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.firstName").value("Teste nome"))
                .andExpect(jsonPath("$.lastName").value("Teste sobrenome"))
                .andExpect(jsonPath("$.password").isNotEmpty());
    }

    @Test
    public void testGetClientById() throws Exception {
        mockMvc.perform(get("/api/client/{id}", 69))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.firstName").value("Teste nome"))
                .andExpect(jsonPath("$.lastName").value("Teste sobrenome"))
                .andExpect(jsonPath("$.password").isNotEmpty());

    }

    @Test
    public void testUpdateFirstName() throws Exception {
        String newFirstName = "Anne";
        mockMvc.perform(put("/api/client/{id}/first-name", 72, newFirstName)
                .contentType("application/json")
                .content(newFirstName))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateLastName() throws Exception {
        String newLastName = "Camargo";
        mockMvc.perform(put("/api/client/{id}/last-name", 72, newLastName)
        .contentType("application/json")
        .content(newLastName))
        .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        Address address = new Address("Rua das flores", States.RJ, "Rio de Janeiro");
        String addressJson = new ObjectMapper().writeValueAsString(address);
        mockMvc.perform(put("/api/client/{id}/address", 70, address)
                .contentType("application/json")
                .content(addressJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddTelephone() throws Exception {
        Telephone phone = new Telephone("11", "987654321", TelephoneType.WORK);
        String telephoneJson = new ObjectMapper().writeValueAsString(phone);
        mockMvc.perform(post("/api/client/{id}/telephones", 70, telephoneJson)
            .contentType("application/json")
                .content(telephoneJson))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteTelephone() throws Exception {
        mockMvc.perform(delete("/api/client/{id}/telephones/{telephoneId}", 70, 94)
        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindClientsByCity() throws Exception {
        mockMvc.perform(get("/api/client/city/{city}", "Rio de Janeiro")
        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
