package com.denniseckerskorn.security;

import com.denniseckerskorn.entities.user_managment.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom implementation of UserDetails to represent user-specific data.
 */
public class CustomUserDetails implements UserDetails {
    private final User user;

    /**
     * Constructor to initialize the CustomUserDetails with a User object.
     *
     * @param user the User object containing user information
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of GrantedAuthority objects representing user permissions
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermissionName().toString()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns the password of the user.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username of the user.
     *
     * @return the user's email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Returns the account's expiration status.
     *
     * @return true if the account is not expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns the account's lock status.
     *
     * @return true if the account is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns the credentials' expiration status.
     *
     * @return true if the credentials are not expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns the account's enabled status.
     *
     * @return true if the account is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return user.getStatus().name().equals("ACTIVE");
    }

    /**
     * Returns the User object associated with this CustomUserDetails.
     *
     * @return the User object
     */
    public User getUser() {
        return user;
    }
}
