package org.omar.recipes.rating.controller;

import org.omar.recipes.users.controller.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import org.omar.recipes.rating.entity.Rating;
import org.omar.recipes.users.entity.UserAccount;

@Service
public class RatingService {
 
    RatingRepository ratingRepository;
    UserAccountService accountService;

    public RatingService(RatingRepository ratingRepository, UserAccountService accountService) {
        this.ratingRepository = ratingRepository;
        this.accountService = accountService;
    }
    
    public Optional<Rating> getRatingById(long id){
        return ratingRepository.findById(id);
     }

     public Optional<Rating> saveRecipe(Rating rating,String email){
        rating.setLocalDateTime(LocalDateTime.now());
         rating.setUserAccount(accountService.loadChefUserByEmail(email));
        return Optional.of(ratingRepository.save(rating));
     }

    public ResponseEntity<?> updateRating(Long id, Rating rating, String email){
        Optional<Rating> exists = ratingRepository.findById(id);
        if(exists.isPresent()){
            UserAccount user = accountService.loadChefUserByEmail(email);
            if(exists.get().getUserAccount().getEmail().equals(email)){
                rating.setLocalDateTime(LocalDateTime.now());
                rating.setId(exists.get().getId());
                rating.setUserAccount(user);
                Rating saved = ratingRepository.save(rating);
                return ResponseEntity.noContent().build();
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    public HttpStatus removeRecipe(Long id, String email){
        Optional<Rating> byId = ratingRepository.findById(id);
        if(byId.isPresent()){
            if (byId.get().getUserAccount().getEmail().equals(email)){
                ratingRepository.delete(byId.get());
                return HttpStatus.NO_CONTENT;
            }else{
                return HttpStatus.FORBIDDEN;
            }
        }else {
            return HttpStatus.NOT_FOUND;
        }

    }
}
