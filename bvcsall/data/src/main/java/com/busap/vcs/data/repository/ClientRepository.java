package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Client;

@Resource(name = "clientRepository")
public interface ClientRepository extends BaseRepository<Client, Long> {

	Client findByClientSecret(String clientSecret);

	Client findByClientId(String clientId);

}
