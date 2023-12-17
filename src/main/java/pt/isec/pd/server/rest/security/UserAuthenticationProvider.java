package pt.isec.pd.server.rest.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.isec.pd.server.rest.controllers.DatabaseController;
import pt.isec.pd.server.rest.managers.UsersManager;
import pt.isec.pd.server.rest.models.User;
import pt.isec.pd.server.rest.utils.DbConnections;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider
{
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        UsersManager usersManager = new UsersManager(databaseController.getConnection());

        User user = usersManager.getUserByEmail(username);

        if (user != null) {
            if (user.getEmail().equals("admin") && user.getPassword().equals(password)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else if (user.getEmail().equals(username) && user.getPassword().equals(password)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("USER"));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
