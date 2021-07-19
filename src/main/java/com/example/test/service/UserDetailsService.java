package com.example.test.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

			grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
			return new UserDetails() {

				@Override
				public boolean isEnabled() {
					// TODO Auto-generated method stub
					return true;
				}

				@Override
				public boolean isCredentialsNonExpired() {
					// TODO Auto-generated method stub
					return true;
				}

				@Override
				public boolean isAccountNonLocked() {
					// TODO Auto-generated method stub
					return true;
				}

				@Override
				public boolean isAccountNonExpired() {
					// TODO Auto-generated method stub
					return true;
				}

				@Override
				public String getUsername() {
					// TODO Auto-generated method stub
					return "ameer";
				}

				@Override
				public String getPassword() {
					// TODO Auto-generated method stub
					return "{noop}khan";
				}

				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					// TODO Auto-generated method stub
					return grantedAuthorities;
				}
			};
	}

}
