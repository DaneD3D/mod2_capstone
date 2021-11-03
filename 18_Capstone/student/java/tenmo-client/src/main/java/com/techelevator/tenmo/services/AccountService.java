package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public void viewCurrentBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<BigDecimal> entity = new HttpEntity(headers);

        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance/" + currentUser.getUser().getId(),
                    HttpMethod.GET, entity, BigDecimal.class);
            balance = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        System.out.println(balance);
    }

    public void sendBucks() {

    }





}
