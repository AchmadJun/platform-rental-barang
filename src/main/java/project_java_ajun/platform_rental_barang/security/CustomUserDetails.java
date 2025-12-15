package project_java_ajun.platform_rental_barang.security;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project_java_ajun.platform_rental_barang.entity.Admin;
import project_java_ajun.platform_rental_barang.entity.Role;
import project_java_ajun.platform_rental_barang.entity.User;

import java.util.Collection;
import java.util.List;


@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private String id;
    private String username;    // email (USER) atau username (ADMIN)
    private String password;
    private Role role;

    //User
    public static CustomUserDetails fromUser(User user){
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    //Admin
    public static CustomUserDetails fromAdmin(Admin admin){
        return new CustomUserDetails(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getRole()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
