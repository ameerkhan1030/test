package com.example.test.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.test.entity.User;
import com.example.test.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RedisPublisher redisPublisher;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void addUser(User user) {

		String name = user.getName();
		if (name == null || name.trim().length() == 0) {
			log.warn("Name shouldnt be null or empty");
			return;
		}

		String existingUserWithSameName = userRepo.findByName(name);
		if (existingUserWithSameName != null) {
			log.warn("Name is already existing");
			return;
		}

		userRepo.save(user);
		log.info("User Created Successfully");
		redisPublisher.broadcastConfigStatus(user);
	}

	@Transactional
	@Cacheable(cacheNames = "users")
	public List<User> getUsers() {

		return userRepo.findAll();
	}

	@Transactional
	public JasperPrint userListExport() throws JRException, IOException, SQLException {


		Resource resource = resourceLoader.getResource("classpath:templates/jrxml/User_List.jrxml");

		InputStream userReportStream = resource.getInputStream();
		JasperReport jasperReport = JasperCompileManager.compileReport(userReportStream);

		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(userRepo.findAll());


		JasperPrint print = JasperFillManager.fillReport(jasperReport, null, source);

		return print;

	}

	public void logCheck() {
		log.trace("Trace Log");
		log.debug("Debug Log");
		log.info("Info Log");
		log.warn("Warn Log");
		log.error("Error Log");
	}
}
