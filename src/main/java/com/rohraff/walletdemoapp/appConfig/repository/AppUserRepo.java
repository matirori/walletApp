package com.rohraff.walletdemoapp.appConfig.repository;

import com.rohraff.walletdemoapp.appConfig.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    AppUser findAllByUsername(String username);
    AppUser findAppUserByUsername(String name);
}

