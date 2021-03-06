package telran.ashkelon2018fl.forum.service;

import java.util.Set;

import telran.ashkelon2018fl.forum.dto.UserProfileDto;
import telran.ashkelon2018fl.forum.dto.UserRegDto;

public interface AccountService {

	UserProfileDto addUser(UserRegDto userRegDto, String token);

	UserProfileDto editUser(UserRegDto userRegDto, String token);

	UserProfileDto removeUser(String login, String token);

	Set<String> addRole(String login, String role, String token);

	Set<String> removeRole(String login, String role, String token);

	void changePassword(String password, String token);
	
	UserProfileDto getUser(String login);
	
	UserProfileDto login(String token);
}
