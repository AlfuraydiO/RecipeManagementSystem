package org.omar.recipes.users.controller;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.omar.recipes.users.entity.ChefUser;
import org.omar.recipes.users.entity.RegistrationRequest;
import org.omar.recipes.users.entity.UserAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
public class ChefUserService implements UserDetailsService {

    private final ChefUserRepository userRepository;
    private final PasswordEncoder passwordEncoder ;
    private  Validator validator;

    public ChefUserService(PasswordEncoder passwordEncoder, ChefUserRepository userRepository, Validator validator) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public Optional<ChefUser> getUserById(long id){
        return userRepository.findById(id);
     }

     public ResponseEntity<String> saveUser(RegistrationRequest request){
         Optional<ChefUser> userByEmail = userRepository.findChefUserByEmail(request.email());
         if(userByEmail.isEmpty()){
             return ResponseEntity.badRequest().body("User does not exits");
         }
         var user = new ChefUser();
         user.setEmail(request.email());
         user.setPassword(request.password());
         user.setEnabled(true);
         user.setAccountNonLocked(true);
         user.setAuthority("ROLE_USER");
         Set<ConstraintViolation<ChefUser>> violations = validator.validate(user);
         if (violations.isEmpty()) {
             user.setPassword(passwordEncoder.encode(request.password()));
             user.setId(userByEmail.get().getId());
             userRepository.save(user);
         } else {
             return ResponseEntity.badRequest().body(violations.stream().map(ConstraintViolation::getMessage).toList().toString());
         }
         return ResponseEntity.ok().build();

     }

    public Optional<ChefUser> updateUser(Long id, ChefUser user){
        Optional<ChefUser> exists = userRepository.findById(id);
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


    public ChefUser loadChefUserByEmail(String username) throws UsernameNotFoundException {
        return userRepository.findChefUserByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ChefUser user=userRepository.findChefUserByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("Not found"));
        return new UserAdapter(user);
    }

    public ResponseEntity<String> saveNewUser(RegistrationRequest request) {
        Optional<ChefUser> userByEmail = userRepository.findChefUserByEmail(request.email());
        if(userByEmail.isPresent()){
            return ResponseEntity.badRequest().body("Email already exists");
        }
        var user = new ChefUser();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAuthority("ROLE_USER");
        Set<ConstraintViolation<ChefUser>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.password()));
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(violations.stream().map(ConstraintViolation::getMessage).toList().toString());
        }
    }
}
