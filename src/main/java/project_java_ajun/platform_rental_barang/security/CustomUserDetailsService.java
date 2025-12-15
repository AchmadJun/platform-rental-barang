package project_java_ajun.platform_rental_barang.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project_java_ajun.platform_rental_barang.repository.AdminRepository;
import project_java_ajun.platform_rental_barang.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

        // Coba cari sebagai ID (UUID user atau admin)
        var userById = userRepository.findById(input);
        if (userById.isPresent()) {
            return CustomUserDetails.fromUser(userById.get());
        }

        var adminById = adminRepository.findById(input);
        if (adminById.isPresent()) {
            return CustomUserDetails.fromAdmin(adminById.get());
        }

        // Coba untuk login admin (username)
        var adminByUsername = adminRepository.findByUsername(input);
        if (adminByUsername.isPresent()) {
            return CustomUserDetails.fromAdmin(adminByUsername.get());
        }

        // Coba untuk login user (email)
        var userByEmail = userRepository.findByEmail(input);
        if (userByEmail.isPresent()) {
            return CustomUserDetails.fromUser(userByEmail.get());
        }
            throw  new UsernameNotFoundException("User not found");
    }
}
