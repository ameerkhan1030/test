package com.example.test.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.component.JWTUtil;
import com.example.test.entity.User;
import com.example.test.service.UserDetailsService;
import com.example.test.service.UserService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping
	public void addUser(@RequestBody final User user) {
		userService.addUser(user);
	}

	@GetMapping
	public List<User> getUsers() {

		return userService.getUsers();
	}

	@GetMapping(value = "/export")
	public void userListExport(HttpServletResponse servletResponse) throws IOException, JRException, SQLException {
		servletResponse.setContentType("application/x-download");
		servletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"users.pdf\""));
		ServletOutputStream outputStream = servletResponse.getOutputStream();

		JasperExportManager.exportReportToPdfStream(userService.userListExport(), outputStream);

	}

	@GetMapping(value = "/logs")
	public void logCheck() {
		userService.logCheck();
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> authenticationRequest)
			throws Exception {

		// authenticate(authenticationRequest.get("username"),
		// authenticationRequest.get("password"));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.get("username"));

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(token);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
