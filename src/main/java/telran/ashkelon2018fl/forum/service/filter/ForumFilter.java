package telran.ashkelon2018fl.forum.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2018fl.forum.configuration.AccountConfiguration;
import telran.ashkelon2018fl.forum.configuration.AccountUserCredetentials;
import telran.ashkelon2018fl.forum.dao.UserAccountRepository;
import telran.ashkelon2018fl.forum.domain.UserAccount;

@Service
@Order(3)
public class ForumFilter implements Filter {

	@Autowired
	UserAccountRepository repository;	

	@Autowired
	AccountConfiguration configuration;

	@Override
	public void doFilter(ServletRequest reqs, 
			ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		//String method = request.getMethod();
		if (!(path.startsWith("/forum/posts"))) {
			String token = request.getHeader("Authorization");

			AccountUserCredetentials userCredetentials = configuration.tokenDecode(token);
			UserAccount userAccount = repository.findById(userCredetentials.getLogin()).orElse(null);
			if (userAccount == null) {
				response.sendError(401, "Unauthorized");
			} else {
				if (BCrypt.checkpw(userCredetentials.getPassword(), userAccount.getPassword())) {
					response.sendError(403, "Wrong password");
				}
			}
		}
		chain.doFilter(request, response);

	}


}
