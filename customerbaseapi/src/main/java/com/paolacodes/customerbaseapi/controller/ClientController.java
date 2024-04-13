package com.paolacodes.customerbaseapi.controller;

import com.paolacodes.customerbaseapi.domain.model.Client;
import com.paolacodes.customerbaseapi.domain.repository.ClientRepository;
import com.paolacodes.customerbaseapi.domain.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private ClientRepository clientRepository;
    private ClientService clientService;

    @GetMapping
    public List<Client> findAllClient(){
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findClientById(@PathVariable Long id){
       return clientRepository.findById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client addNewClient(@Valid @RequestBody Client client){

        return clientService.saveClient(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client, @PathVariable Long id){
        if(!clientRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        client.setId(id);
        client = clientService.saveClient(client);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        if(!clientRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
