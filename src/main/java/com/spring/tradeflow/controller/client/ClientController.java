package com.spring.tradeflow.controller.client;

import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.client.Telephone;
import com.spring.tradeflow.model.services.client.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.tradeflow.model.entities.client.Address;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        log.info("Saving client in database: {}", client);
        Client createdClient = clientService.createClient(client);
        log.info("Client created: {}", createdClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClients() {
        log.info("Getting all clients");
        List<Client> clients = clientService.getAllClients();
        log.info("Client list: {}", clients);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        log.info("Getting client by id: {}", id);
        Client client = clientService.getClientById(id);
        log.info("Client found: {}", client);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}/first-name")
    public ResponseEntity<Void> updateFirstName(@PathVariable Long id, @RequestBody String firstName) {
        log.info("Updating client first name: {}", firstName);
        clientService.updateClientFirstName(id, firstName);
        log.info("Client updated firstName: {}", clientService.getClientById(id).getFirstName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/last-name")
    public ResponseEntity<Void> updateLastName(@PathVariable Long id, @RequestBody String lastName) {
        log.info("Updating client last name: {}", lastName);
        clientService.updateClientLastName(id, lastName);
        log.info("Client updated lastName: {}", clientService.getClientById(id).getLastName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<Client> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        log.info("Updating client address: {}", address);
        Client updatedClient = clientService.updateAddress(id, address);
        log.info("Client updated address: {}", updatedClient);
        return ResponseEntity.ok(updatedClient);
    }

    @PostMapping("/{id}/telephones")
    public ResponseEntity<Client> addTelephone(@PathVariable Long id, @RequestBody Telephone telephone) {
        log.info("Updating client telephone: {}", telephone);
        Client updatedClient = clientService.addTelephone(id, telephone);
        log.info("Client updated telephone: {}", updatedClient);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}/telephones/{telephoneId}")
    public ResponseEntity<Void> removeTelephone(@PathVariable Long id, @PathVariable Long telephoneId) {
        log.info("Removing client telephone: {}", telephoneId);
        clientService.removeTelephone(id, telephoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Client>> findClientsByCity(@PathVariable String city) {
        log.info("Finding client by city: {}", city);
        List<Client> clients = clientService.findClientsByCity(city);
        log.info("Client list by city: {}", clients);
        return ResponseEntity.ok(clients);
    }

}
