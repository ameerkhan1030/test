package com.example.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@NamedQuery(name = "User.findByName", query = "select u.name from User u where u.name= ?1")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5941590266632168597L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

}
