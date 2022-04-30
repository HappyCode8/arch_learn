package com.wyj.entity;

import com.wyj.model.Role;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * 认证用户模型
 * 用户注册之后，包含其业务属性，如姓名、电话、地址，用于业务，存储于Account对象中
 * 也包含其用于认证的属性，譬如密码、角色、是否停用，存储于AuthenticAccount对象中
 **/

@Data
public class AuthenticAccount extends Account implements UserDetails {
    /**
     * 该用户拥有的授权，譬如读取权限、修改权限、增加权限等等
     */
    private Collection<GrantedAuthority> authorities = new HashSet<>();

    public AuthenticAccount() {
        super();
        authorities.add(new SimpleGrantedAuthority(Role.USER));
    }

    public AuthenticAccount(Account origin) {
        this();
        BeanUtils.copyProperties(origin, this);
        if (getId() == 1) {
            // 由于没有做用户管理功能，默认给系统中第一个用户赋予管理员角色
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN));
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * 账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否被锁定
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
