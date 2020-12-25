package com.jiyoung.apigw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jiyoung.apigw.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>
{

}
