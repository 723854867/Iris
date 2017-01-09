package com.busap.vcs.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Client;
import com.busap.vcs.data.repository.ClientRepository;
import com.busap.vcs.service.ClientService;

@Service("clientService")
public class ClientServiceImpl extends BaseServiceImpl<Client, Long> implements
		ClientService {
	@Resource(name = "clientRepository")
	private ClientRepository clientRepository;

	@Override
	public Client createClient(Client client) {
		client.setClientId(UUID.randomUUID().toString());
		client.setClientSecret(UUID.randomUUID().toString());
		return clientRepository.save(client);
	}

	@Override
	public Client updateClient(Client client) {
		return clientRepository.save(client);
	}

	@Override
	public void deleteClient(Long clientId) {
		clientRepository.delete(clientId);
	}

	@Override
	public Client findOne(Long id) {
		return clientRepository.findOne(id);
	}

	@Override
	public Client findByClientId(String clientId) {
		return clientRepository.findByClientId(clientId);
	}

	@Override
	public Client findByClientSecret(String clientSecret) {
		return clientRepository.findByClientSecret(clientSecret);
	}

}
