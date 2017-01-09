package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Client;

public interface ClientService extends BaseService<Client, Long> {
	Client createClient(Client client);

	Client updateClient(Client client);

	void deleteClient(Long clientId);

	Client findOne(Long clientId);

	List<Client> findAll();

	Client findByClientId(String clientId);

	Client findByClientSecret(String clientSecret);
}
