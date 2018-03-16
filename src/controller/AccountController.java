package controller;

import database.UserTableGateway;

public class AccountController implements MyController {

	private UserTableGateway gateway;
	
	public AccountController(UserTableGateway gateway) {
    	this.gateway = gateway;
    }
}
