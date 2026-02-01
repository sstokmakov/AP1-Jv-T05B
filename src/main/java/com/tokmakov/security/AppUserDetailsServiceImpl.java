package com.tokmakov.security;

import com.tokmakov.domain.model.User;
import com.tokmakov.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLogin(username);
        return AppUserDetails.builder()
                .uuid(user.getUuid())
                .username(user.getLogin())
                .password(user.getPassword())
                .build();
    }
}
