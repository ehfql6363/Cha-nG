package com.ssafy.chaing.auth.service;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new UserPrincipal(String.valueOf(user.getId()), user.getEmailAddress(), null);
    }
}
