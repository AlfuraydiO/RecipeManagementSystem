package org.omar.recipes.users.controller;

import org.omar.recipes.users.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

  Optional<UserAccount> findChefUserByEmail(String email);

}