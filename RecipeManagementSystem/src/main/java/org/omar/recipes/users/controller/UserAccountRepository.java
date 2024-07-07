package org.omar.recipes.users.controller;

import org.omar.recipes.users.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

  Optional<UserAccount> findUserByEmail(String email);

}