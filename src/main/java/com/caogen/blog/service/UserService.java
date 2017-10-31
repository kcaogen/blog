package com.caogen.blog.service;

import com.caogen.blog.dao.UserDao;
import com.caogen.blog.entity.UserRole;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRole user = userDao.getUserByUsername(username);
        if (user == null )throw new BadCredentialsException(username + " not found");

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String roleName = user.getRole();
        if (!StringUtils.isEmpty(roleName)) {
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        return new User(username, user.getPassword(),authorities);
    }
}
