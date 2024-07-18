package com.shoescms.domain.auth;

import com.shoescms.common.exception.UserNotFoundException;
import com.shoescms.domain.user.entity.NguoiDungEntity;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final INguoiDungRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDungEntity nguoiDungEntity = userRepository.findById(Long.valueOf(username)).orElseThrow(()->new UserNotFoundException("user"));
        nguoiDungEntity.setAuthorities(List.of(new SimpleGrantedAuthority(nguoiDungEntity.getRole().getRoleCd())));
        return nguoiDungEntity;
    }
}
