package telran.ashkelon2018fl.forum.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
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
@Order(1)
public class AutentificationFilter implements Filter {

	@Autowired
	UserAccountRepository repository;

	@Autowired
	AccountConfiguration configuration;

	@Override
	public void doFilter(ServletRequest reqs, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;
		System.err.println("Filter1");
		String path = request.getServletPath();
		// System.out.println(path);
		String method = request.getMethod();
		// System.out.println(method);
		boolean filter1 = path.startsWith("/account")
				&& !("POST".equals(method));
		boolean filter2 = path.startsWith("/forum")
				&& !path.startsWith("/forum/posts");
		if (filter1||filter2) {			
			String token = request.getHeader("Authorization");
			if(token==null) {
				response.sendError(401, "Unauthorized");
				return;
			}
//			AccountUserCredetentials userCredetentials = configuration.tokenDecode(token);
//			UserAccount userAccount = repository.findById(userCredetentials.getLogin()).orElse(null);
			AccountUserCredetentials userCredetentials = null;
			try {
				userCredetentials = configuration.tokenDecode(token);
			} catch (Exception e) {
				response.sendError(401, "Unauthorized");
				return;
			}
			UserAccount userAccount = repository.findById(userCredetentials.getLogin()).orElse(null);
			if (userAccount == null) {
				response.sendError(401, "Unauthorized");
				return;
			} else {
				if (BCrypt.checkpw(userCredetentials.getPassword(), userAccount.getPassword())) {
					response.sendError(403, "Wrong password");
					return;
				}
			}
		}
		chain.doFilter(request, response);

	}

//	@Override
//	public void destroy() {
//		
//	}

}
