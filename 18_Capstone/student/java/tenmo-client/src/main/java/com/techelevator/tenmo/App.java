package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;
import io.cucumber.java.sl.In;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_BLUE = "\u001B[34m";

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;

    private RestTemplate restTemplate = new RestTemplate();
	private AccountService accountService = new AccountService();
	private UserService userService = new UserService();

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		System.out.println("Your current balance is:" + ANSI_GREEN +" $" + accountService.viewCurrentBalance() + ANSI_RESET);
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		System.out.println("-------------------------------------------");
		System.out.println("Transfers");
		System.out.println(String.format("%-12s%-15s%-12s", "ID", "From/To", "Amount"));
		System.out.println("-------------------------------------------");

		TransferInfo[] transfers = accountService.getTransfers();
		for (TransferInfo transfer: transfers){
			String from_to = "";
			if(transfer.getFrom().equals(currentUser.getUser().getUsername())){
				from_to = "To " + transfer.getTo();
			}else{
				from_to = "From " + transfer.getFrom();
			}
			System.out.println(String.format("%-12d%-12s%-12s",transfer.getId(),from_to ,transfer.getAmount()));
		}
		Integer userIdSelection = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
		try {
			if (userIdSelection == 0){
				return;
			} else if (!containsSelectedTransferInfo(Arrays.asList(transfers), userIdSelection)) {
				System.out.println("Sorry, the transfer you selected does not exist. Please Try Again.");
				viewTransferHistory();
			} else {
				System.out.println("-------------------------------------------");
				System.out.println("Transfer Details");
				System.out.println("-------------------------------------------");
				TransferInfo returnedTransfer = accountService.getTransferByID(userIdSelection);
				System.out.println("Id: "+ returnedTransfer.getId());
				System.out.println("From: "+ returnedTransfer.getFrom());
				System.out.println("To: "+ returnedTransfer.getTo());
				System.out.println("Type: "+ returnedTransfer.getType());
				System.out.println("Status: "+ returnedTransfer.getStatus());
				System.out.println("Amount: "+ returnedTransfer.getAmount());
			}
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
	}

	private void sendBucks() {
		List<User> users = userService.getUsers();
		System.out.println("-------------------------------------------");
		System.out.println("Users");
		System.out.println(String.format("%-10s%-10s", "ID", "Name"));
		System.out.println("-------------------------------------------");
		for (User user: users){
			System.out.println(String.format("%-10d%-10s",user.getId(),user.getUsername()));
		}
		System.out.println("--------");
        Transfer newTransfer = new Transfer();
        newTransfer.setFrom(currentUser.getUser().getId());
        newTransfer.setStatus(2);
        newTransfer.setType(2);
		Integer userIdSelection = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
		try {
			if(userIdSelection == 0){
				return;
			} else if (!containsSelectedUser(users, userIdSelection)) {
				System.out.println("Sorry, the user you selected does not exist. Please Try Again.");
				sendBucks();
			} else {
				newTransfer.setTo(userIdSelection);
				String userAmount = console.getUserInput("Enter Amount");
				newTransfer.setAmount(new BigDecimal(userAmount));
				if (newTransfer.getAmount().compareTo(BigDecimal.ONE) < 0) {
					System.out.println("Sorry. That isn't a valid amount, please try again.");
					sendBucks();
				} else {
					Transfer returnedTransfer = accountService.sendBucks(newTransfer);
					if (returnedTransfer != null) {
						System.out.println("\nTransfer ID: " + returnedTransfer.getId() + " Was Succesful!\n" + ANSI_GREEN + "$" + returnedTransfer.getAmount() + ANSI_RESET +
								" was transferred to " + ANSI_BLUE + findUsernameInList(users, userIdSelection) + ANSI_RESET);
					} else {
						System.out.println("Sorry, you have insufficient funds. Please Try again.");
						sendBucks();
					}
				}
			}
		} catch (NumberFormatException e){
			System.out.println(e.getMessage());
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private String findUsernameInList(final List<User> list, final Integer userSelection) {
		String username = list.stream().filter(o -> o.getId().equals(userSelection)).findAny().get().getUsername();
		return username;
	}

	private boolean containsSelectedUser(final List<User> list, final Integer userSelection) {
		return list.stream().anyMatch(o -> o.getId().equals(userSelection));
	}

	private boolean containsSelectedTransferInfo(final List<TransferInfo> list, final Integer userSelection) {
		return list.stream().anyMatch(o -> o.getId().equals(userSelection));
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				accountService.setAuthToken(currentUser.getToken());
				//System.out.println(currentUser.getToken());
				accountService.setCurrentUser(currentUser);
				userService.setCurrentUser(currentUser);

				//System.out.println(currentUser.getToken());
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
