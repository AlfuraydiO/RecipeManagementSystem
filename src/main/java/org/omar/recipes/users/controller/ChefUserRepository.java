package org.omar.recipes.users.controller;

import org.omar.recipes.users.entity.ChefUser;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface ChefUserRepository extends CrudRepository<ChefUser, Long> {

  Optional<ChefUser> findChefUserByEmail(String email);

}