package com.example.tvshows.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NetworkService {

    private final NetworkRepository networkRepository;

    @Autowired
    public NetworkService(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public HashMap<String, Network> getNetworkHashMapByNames(List<String> networkNames, boolean createNetworkIfMissing) {

        List<Network> foundNetworks = networkRepository.getNetworksByNames(networkNames);
        HashMap<String, Network> networkMap = new HashMap<>();

        if (!createNetworkIfMissing) {
            for (Network foundNetwork : foundNetworks) {
                networkMap.put(foundNetwork.getName(), foundNetwork);
            }
        } else {
            for (String networkName : networkNames) {
                boolean networkFound = false;
                for (Network foundNetwork : foundNetworks) {
                    if (foundNetwork.getName().equalsIgnoreCase(networkName)) {
                        networkMap.put(networkName, foundNetwork);
                        networkFound = true;
                        break;
                    }
                }

                if (!networkFound) {
                    Network newNetwork = new Network(networkName);
                    networkRepository.save(newNetwork);
                    networkMap.put(networkName, newNetwork);
                }
            }
        }

        return networkMap;
    }

}
