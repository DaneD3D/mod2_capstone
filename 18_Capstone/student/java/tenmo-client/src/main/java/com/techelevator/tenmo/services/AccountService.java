package com.techelevator.tenmo.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferInfo;
import com.techelevator.tenmo.model.User;
import org.openqa.selenium.json.Json;
import org.springframework.http.*;
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

    public Transfer sendBucks(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);

        Transfer returnedTransfer = null;

        try{
            returnedTransfer = restTemplate.postForObject(API_BASE_URL + "transfer", entity, Transfer.class);
        }catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
            //System.out.println("I'm a Teapot");
        }
        return returnedTransfer;

    }

    public TransferInfo[] getTransfers(){
        TransferInfo[] transfers = null;

        try{
            ResponseEntity<TransferInfo[]> response =
                    restTemplate.exchange(API_BASE_URL + "userTransfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), TransferInfo[].class);
            transfers = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transfers;

    }

    public TransferInfo getTransferByID(Integer transferId){
        TransferInfo transfer = null;

        try{
            ResponseEntity<TransferInfo> response =
                    restTemplate.exchange(API_BASE_URL + "transfer/" + transferId, HttpMethod.GET, makeAuthEntity(), TransferInfo.class);
            transfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transfer;

    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }


}
