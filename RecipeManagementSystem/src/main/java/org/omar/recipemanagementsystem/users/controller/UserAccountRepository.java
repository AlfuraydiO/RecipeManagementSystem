package org.omar.recipemanagementsystem.users.controller;

import org.omar.recipemanagementsystem.users.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

    Optional<UserAccount> findUserByEmail(String email);

}