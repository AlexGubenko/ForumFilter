package telran.ashkelon2018fl.forum.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018fl.forum.configuration.AccountConfiguration;
import telran.ashkelon2018fl.forum.configuration.AccountUserCredetentials;
import telran.ashkelon2018fl.forum.dao.UserAccountRepository;
import telran.ashkelon2018fl.forum.domain.UserAccount;
import telran.ashkelon2018fl.forum.dto.UserProfileDto;
import telran.ashkelon2018fl.forum.dto.UserRegDto;
import telran.ashkelon2018fl.forum.exeptions.UserConflictExeption;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	UserAccountRepository userRepository;
	
	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDto addUser(UserRegDto userRegDto, String token) {
		AccountUserCredetentials credentials = 
				accountConfiguration.tokenDecode(token);
		if(userRepository.existsById(credentials.getLogin())) {
			throw new UserConflictExeption();
		}
		String hashPassword = 
				BCrypt.hashpw(credentials.getPassword(),
				BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()
				.login(credentials.getLogin())
				.password(hashPassword)
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName())
				.role("User")
				.expdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
				.build();
		userRepository.save(userAccount);
		return convertToUserProfileDto(userAccount);
	}
	
	private UserProfileDto convertToUserProfileDto(UserAccount userAccount) {
		return UserProfileDto.builder()
				.firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName())
				.login(userAccount.getLogin())
				.roles(userAccount.getRoles())
				.build();
	}

	@Override
	public UserProfileDto editUser(UserRegDto userRegDto, String token) {
		AccountUserCredetentials credetentials = 
				accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById
				(credetentials.getLogin()).get();
		if (userRegDto.getFirstName() != null) {
			userAccount.setFirstName(userRegDto.getFirstName());
		}
		if (userRegDto.getLastName() != null) {
			userAccount.setFirstName(userRegDto.getLastName());
		}
		userRepository.save(userAccount);
		return convertToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto removeUser(String login, String token) {
		AccountUserCredetentials credetentials = 
				accountConfiguration.tokenDecode(token);
		UserAccount userAccountAktiv = userRepository.findById
				(credetentials.getLogin()).get();
		Set<String> roles = userAccountAktiv.getRoles();
		//if(userAccountAktiv)
		
		UserAccount userAccountPassiv = userRepository.findById(login).orElse(null);
		if(userAccountPassiv != null) {
			userRepository.delete(userAccountPassiv);
		}
		return convertToUserProfileDto(userAccountPassiv);
	}

	@Override
	public Set<String> addRole(String login, String role, String token) {
		// FIXME
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		if(userAccount != null) {
			userAccount.addRole(role);
			userRepository.save(userAccount);
		}else {
			return null;
		}
		return userAccount.getRoles();
	}

	@Override
	public Set<String> removeRole(String login, String role, String token) {
		// FIXME
				UserAccount userAccount = userRepository.findById(login).orElse(null);
				if(userAccount != null) {
					userAccount.removeRole(role);
					userRepository.save(userAccount);
				}else {
					return null;
				}
				return userAccount.getRoles();
	}

	@Override
	public void changePassword(String password, String token) {
		AccountUserCredetentials credetentials = 
				accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById
				(credetentials.getLogin()).get();
		String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userAccount.setPassword(hashPassword);
		userAccount.setExpdate((LocalDateTime.now()
				.plusDays(accountConfiguration.getExpPeriod())));
		userRepository.save(userAccount);
	}

	@Override
	public UserProfileDto getUser(String login) {
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		if(userAccount!=null) {
			return convertToUserProfileDto(userAccount);
		}
		return null;
	}

	@Override
	public UserProfileDto login(String token) {
		AccountUserCredetentials credetentials = 
				accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById
				(credetentials.getLogin()).get();
		return convertToUserProfileDto(userAccount);
	}

}
