package com.spring.game.service;

import com.spring.game.dto.UserDTO;
import com.spring.game.dto.UserRegistrationDTO;
import com.spring.game.model.Role;
import com.spring.game.model.User;
import com.spring.game.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationService authenticationService;

    public User saveUser(UserRegistrationDTO userRegistrationDetails) {
        if(authenticationService.validate(userRegistrationDetails)){
            User user = new User();
            user.setName(userRegistrationDetails.getName());
            user.setUsername(userRegistrationDetails.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDetails.getPassword()));
            user.setWallet_amt(userRegistrationDetails.getWallet_amt());
            user.setRoles(List.of(new Role(userRegistrationDetails.getRole())));
            user.setCurrencyCode(userRegistrationDetails.getCurrency());
            System.out.println(user);
            return userRepo.save(user);
        }
        return null;
    }

    public void updateUser(User user) {
        userRepo.save(user);
    }
    public List<UserDTO> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::convetEntityToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO convetEntityToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setWallet_amt(user.getWallet_amt());
        userDTO.setCurrencyCode(user.getCurrencyCode());
        return userDTO;
    }
    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Invalid username or password");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
