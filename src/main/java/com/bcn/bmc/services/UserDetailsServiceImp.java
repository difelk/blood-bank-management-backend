package com.bcn.bmc.services;

import com.bcn.bmc.models.User;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository repository;



    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//        return (UserDetails)
//                repository.findByUsername(username)
//                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }
}