package com.yeahpeu.user.dto;

import com.yeahpeu.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;
    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    @Override
    // role 값을 반환하는 메서드
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    //계정이 만료되었는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    //계정이 블록되었는지
    public boolean isAccountNonLocked() {
        return true;
    }

    //신용이 만료되었는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
