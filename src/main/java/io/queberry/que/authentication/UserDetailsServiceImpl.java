package io.queberry.que.authentication;
import io.queberry.que.employee.Employee;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.exception.UserNotActiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : fahadfazil
 * @since : 22/12/17
 */


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = employeeRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User "+username+" doesn't exist");

        if (!user.isActive())
            throw new UserNotActiveException("User "+username+" is not active");

        return user;
    }
}
