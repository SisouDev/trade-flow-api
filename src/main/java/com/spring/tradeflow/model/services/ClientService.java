package com.spring.tradeflow.model.services;

import com.spring.tradeflow.exceptions.InvalidDataException;
import com.spring.tradeflow.exceptions.ResourceNotFoundException;
import com.spring.tradeflow.model.entities.Address;
import com.spring.tradeflow.model.entities.Client;
import com.spring.tradeflow.model.entities.Telephone;
import com.spring.tradeflow.model.repositories.AddressRepository;
import com.spring.tradeflow.model.repositories.ClientRepository;
import com.spring.tradeflow.model.repositories.TelephoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    public final ClientRepository clientRepository;
    public final AddressRepository addressRepository;
    public final TelephoneRepository telephoneRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, AddressRepository addressRepository, TelephoneRepository telephoneRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.telephoneRepository = telephoneRepository;
    }

    @Transactional
    public Client createClient(Client client) {
        if (client == null || client.getAddress() == null || client.getPassword() == null) {
            throw new InvalidDataException("Client cannot be null");
        }
        Address address = client.getAddress();
        addressRepository.save(address);
        client.setAddress(address);

        if (client.getTelephones() != null) {
            for (Telephone telephone : client.getTelephones()) {
                telephone.setClient(client);
            }
        }
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return (List<Client>) clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    @Transactional
    public Client addTelephone(Long clientId, Telephone telephone) {
        if (telephone == null || telephone.getAreaCode() == null || telephone.getType() == null || telephone.getNumber() == null) {
            throw new InvalidDataException("Telephone fields cannot be null");
        }
        if (telephone.getAreaCode().isEmpty() || telephone.getNumber().isEmpty()) {
            throw new InvalidDataException("Area code or number cannot be empty");
        }
        if (telephone.getAreaCode().length() != 2) {
            throw new InvalidDataException("Area code length must be 2.");
        }
        if (telephone.getNumber().length() != 9){
            throw new InvalidDataException("Number length must be 9.");
        }
        Client client = getClientById(clientId);
        telephone.setClient(client);
        client.addTelephone(telephone);
        return clientRepository.save(client);
    }

    @Transactional
    public Client removeTelephone(Long clientId, Long telephoneId) {
        Client client = getClientById(clientId);
        Telephone telephone = client.getTelephones().stream()
                .filter(t -> t.getTelephoneId().equals(telephoneId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Telephone not found"));
        client.removeTelephone(telephone);
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateAddress(Long clientId, Address address) {
        Client client = getClientById(clientId);
        client.setAddress(address);
        addressRepository.save(address);
        return clientRepository.save(client);
    }
}
