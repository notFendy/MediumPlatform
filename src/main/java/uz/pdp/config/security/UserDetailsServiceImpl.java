package uz.pdp.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.pdp.dao.UserDao;
import uz.pdp.domain.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final User user = userDao.getUserByUsername(username);
            return new UserContext(user);
        } catch (DataAccessException e) {
            log.error("{}", e.getMessage());
            throw new UsernameNotFoundException("User not found with username - " + username);
        }
    }

}