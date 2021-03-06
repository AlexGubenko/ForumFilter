package telran.ashkelon2018fl.forum.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2018fl.forum.dto.UserProfileDto;
import telran.ashkelon2018fl.forum.dto.UserRegDto;
import telran.ashkelon2018fl.forum.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountManagementController {
	@Autowired
	AccountService accountService;
	
	@PostMapping
	public UserProfileDto register(@RequestBody UserRegDto userRegDto, 
			@RequestHeader("Authorization") String token) {
		System.err.println("UserProfileDto register");
		return accountService.addUser(userRegDto, token);
	}
	
	@PutMapping
	public UserProfileDto update(@RequestBody UserRegDto userRegDto, 
			@RequestHeader("Authorization") String token) {
		System.err.println("UserProfileDto update");
		return accountService.editUser(userRegDto, token);
	}
	
	@GetMapping
	public UserProfileDto loginUser
	(@RequestHeader("Authorization") String token) {
		return accountService.login(token);
	}
	
	@DeleteMapping("/{login}")
	public UserProfileDto remove(@PathVariable String login, 
			@RequestHeader("Authorization") String token) {
		System.err.println("UserProfileDto remove");
		return accountService.removeUser(login, token);
	}
	
	@PutMapping("/role/{login}/{role}")
	public Set<String> addRole(@PathVariable String login, 
			@PathVariable String role, 
			@RequestHeader("Authorization") String token){
		System.err.println("Set<String> addRole");
		return accountService.addRole(login, role, token);
	}

	@DeleteMapping("/role/{login}/{role}")
	public Set<String> removeRole(@PathVariable String login, 
			@PathVariable String role, 
			@RequestHeader("Authorization") String token){
		System.err.println("Set<String> removeRole");
		return accountService.removeRole(login, role, token);
	}

	@PutMapping("/password")
	public void changePassword
			(@RequestHeader("X-Authorization")String password,
			@RequestHeader("Authorization") String token) {
		System.err.println("void changePassword");
		accountService.changePassword(password, token);
	}
	
	@GetMapping("/{login}")
	public UserProfileDto getUser(@PathVariable String login) {
		System.err.println("void changePassword");
		return accountService.getUser(login);
	}
}
