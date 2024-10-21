package com.spring.tradeflow.controller.client;

import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.client.Telephone;
import com.spring.tradeflow.model.services.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.tradeflow.model.entities.client.Address;

import java.util.List;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}/first-name")
    public ResponseEntity<Void> updateFirstName(@PathVariable Long id, @RequestBody String firstName) {
        clientService.updateClientFirstName(id, firstName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/last-name")
    public ResponseEntity<Void> updateLastName(@PathVariable Long id, @RequestBody String lastName) {
        clientService.updateClientLastName(id, lastName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<Client> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        Client updatedClient = clientService.updateAddress(id, address);
        return ResponseEntity.ok(updatedClient);
    }

    @PostMapping("/{id}/telephones")
    public ResponseEntity<Client> addTelephone(@PathVariable Long id, @RequestBody Telephone telephone) {
        Client updatedClient = clientService.addTelephone(id, telephone);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}/telephones/{telephoneId}")
    public ResponseEntity<Void> removeTelephone(@PathVariable Long id, @PathVariable Long telephoneId) {
        clientService.removeTelephone(id, telephoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Client>> findClientsByCity(@PathVariable String city) {
        List<Client> clients = clientService.findClientsByCity(city);
        return ResponseEntity.ok(clients);
    }

}
