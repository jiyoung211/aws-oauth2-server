package com.jiyoung.apigw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiyoung.apigw.model.Client;
import com.jiyoung.apigw.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(path = "/client")
public class ClientController
{
	@Autowired
	private ClientRepository clientRepository;

	@GetMapping
	public String main(Model model)
	{
		List<Client> clients = this.getAllClients();
		model.addAttribute("clients", clients);
		return "pub/client";
	}

	@GetMapping("/page")
	public String page(Model model)
	{
		List<Client> clients = this.getAllClients();
		model.addAttribute("clients", clients);
		return "/WEB-INF/view/pub/client.jsp";
	}

	@GetMapping("/list")
	public @ResponseBody List<Client> getAllClients()
	{
		return clientRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Client> getClientById(@PathVariable(value = "id") String clientid)
			throws Exception
	{
		Client client = clientRepository.findById(clientid)
				.orElseThrow(() -> new Exception("Client not found for this id :: " + clientid));
		log.debug("client :: " + client);
		return ResponseEntity.ok().body(client);
	}

	@PostMapping
	public @ResponseBody Client createClient(@RequestBody Client client)
	{
		return clientRepository.save(client);
	}

	@PutMapping("/{id}")
	public @ResponseBody ResponseEntity<Client> updateClient(@PathVariable(value = "id") String clientid,
			@RequestBody Client clientDetails) throws Exception
	{
		Client client = clientRepository.findById(clientid)
				.orElseThrow(() -> new Exception("Client not found for this id :: " + clientid));

		client.setClientId(clientDetails.getClientId());
		client.setApplicationDescription(clientDetails.getApplicationDescription());
		client.setApplicationLogoUri(clientDetails.getApplicationLogoUri());
		client.setApplicationLogoutUri(clientDetails.getApplicationLogoutUri());
		client.setApplicationName(clientDetails.getApplicationName());
		client.setApplicationWebUri(clientDetails.getApplicationWebUri());
		client.setClientIpAddress(clientDetails.getClientIpAddress());
		client.setClientSecret(clientDetails.getClientSecret());
		client.setConfidential(clientDetails.getConfidential());
		client.setHomeRealm(clientDetails.getHomeRealm());
		client.setRegisteredAt(clientDetails.getRegisteredAt());
		client.setRegisteredDynamically(clientDetails.getRegisteredDynamically());
		client.setTokenEndpointAuthMethod(clientDetails.getTokenEndpointAuthMethod());
		client.setResourceOwnerSubjectId(clientDetails.getResourceOwnerSubjectId());
		client.setSubjectId(clientDetails.getSubjectId());

		final Client updatedClient = clientRepository.save(client);
		log.debug("update client :: " + updatedClient);

		return ResponseEntity.ok(updatedClient);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody Map<String, Boolean> deleteClient(@PathVariable(value = "id") String clientid) throws Exception
	{
		Client client = clientRepository.findById(clientid)
				.orElseThrow(() -> new Exception("Client not found for this id :: " + clientid));

		clientRepository.delete(client);
		log.debug("delete client :: " + client);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
