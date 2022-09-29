package am.itspace.company_employee_spring.security;

import am.itspace.company_employee_spring.entity.User;
import am.itspace.company_employee_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepo.findByEmail(username);
        if (byEmail.isEmpty()) {
            throw new UsernameNotFoundException("username does not exist");
        }

        return new CurrentUser(byEmail.get());
    }
}
