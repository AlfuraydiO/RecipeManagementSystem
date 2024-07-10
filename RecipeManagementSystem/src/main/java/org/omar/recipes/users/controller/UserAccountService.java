package org.omar.recipes.users.controller;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.omar.recipes.users.entity.UserAccount;
import org.omar.recipes.users.boundary.RegistrationRequest;
import org.omar.recipes.users.entity.UserAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserAccountService implements UserDetailsService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder ;
    private  Validator validator;
    
    private static final Logger LOG = Logger.getLogger(UserAccountService.class.getName());
    
    

    public UserAccountService(PasswordEncoder passwordEncoder, UserAccountRepository userRepository, Validator validator) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public Optional<UserAccount> getUserById(long id){
        return userRepository.findById(id);
     }

     public ResponseEntity<String> saveUser(RegistrationRequest request){
         Optional<UserAccount> userByEmail = userRepository.findUserByEmail(request.email());
         if(userByEmail.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user exsit with provided Email");
         }
         var user = new UserAccount();
         user.setEmail(request.email());
         user.setPassword(request.password());
         user.setEnabled(true);
         user.setAccountNonLocked(true);
         user.setAuthority(request.role());
         Set<ConstraintViolation<UserAccount>> violations = validator.validate(user);
         if (violations.isEmpty()) {
             user.setPassword(passwordEncoder.encode(request.password()));
             user.setId(userByEmail.get().getId());
             userRepository.save(user);
         } else {
             return ResponseEntity.badRequest().body(violations.stream().map(ConstraintViolation::getMessage).toList().toString());
         }
         return ResponseEntity.ok().build();

     }

    public Optional<UserAccount> updateUser(Long id, UserAccount user){
        Optional<UserAccount> exists = userRepository.findById(id);
        if(exists.isPresent()){
            user.setId(exists.get().getId());
            return Optional.of(userRepository.save(user));
        }else{
            return Optional.empty();
        }
    }

    public boolean removeUser(Long id){
        if(!userRepository.existsById(id)){
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }


    public UserAccount loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount user=userRepository.findUserByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
        return new UserAdapter(user);
    }

    public ResponseEntity<String> saveNewUser(RegistrationRequest request) {
        Optional<UserAccount> userByEmail = userRepository.findUserByEmail(request.email());
        if(userByEmail.isPresent()){
              LOG.warning("Email Already exists");
            return ResponseEntity.badRequest().body("Email already exists");
          
        }
        var user = new UserAccount();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAuthority(request.role());
        Set<ConstraintViolation<UserAccount>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.password()));
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } else {
            LOG.warning(" Constraint Violation exists");
            return ResponseEntity.badRequest().body(violations.stream().map(ConstraintViolation::getMessage).toList().toString());
        }
    }
}
