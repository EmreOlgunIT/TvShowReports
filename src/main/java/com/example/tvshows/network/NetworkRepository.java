package com.example.tvshows.network;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Integer> {

    @Query("SELECT n FROM Network n WHERE n.name IN :networkNames")
    List<Network> getNetworksByNames(@Param("networkNames") List<String> networkNames);

}