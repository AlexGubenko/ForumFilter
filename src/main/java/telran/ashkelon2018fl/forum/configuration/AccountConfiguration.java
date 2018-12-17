package telran.ashkelon2018fl.forum.configuration;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;

@Configuration
public class AccountConfiguration {

	@Value("${exp.value}")
	int expPeriod;
	
	@ManagedAttribute
	public int getExpPeriod() {
		System.err.println("getExpPeriod");
		return expPeriod;
	}

	@ManagedAttribute
	public void setExpPeriod(int expPeriod) {
		System.err.println("setExpPeriod");
		this.expPeriod = expPeriod;
	}

	public AccountUserCredetentials tokenDecode(String token) {
		System.err.println("tokenDecode start");
		int index = token.indexOf(" ");
		token = token.substring(index+1);
		byte[] base64DecodeBytes = Base64.getDecoder().decode(token);
		token = new String(base64DecodeBytes);
		String[] auth = token.split(":");
		AccountUserCredetentials credentials = 
				new AccountUserCredetentials(auth[0],auth[1]);
		return credentials;
		
	}
}
