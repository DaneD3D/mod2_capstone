package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService  {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private RestTemplate restTemplate = new RestTemplate();


    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        try{
            ResponseEntity<List<User>> response =
                    restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(),  new ParameterizedTypeReference<List<User>>() {});
            users = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        if (users == null) {
            List<User> emptyList= new ArrayList<>();
            return emptyList;
        } else
        return users;

    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
