package com.rohraff.walletdemoapp.appConfig.service;

import com.rohraff.walletdemoapp.appConfig.model.AppUser;
import com.rohraff.walletdemoapp.appConfig.model.RegisterInfo;
import com.rohraff.walletdemoapp.appConfig.model.RegisterStatus;
import com.rohraff.walletdemoapp.appConfig.repository.AppUserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {

    private PasswordEncoder passwordEncoder;
    private AppUserRepo appUserRepo;
    private RegisterInfo registerInfo;

    public RegisterService(PasswordEncoder passwordEncoder, AppUserRepo appUserRepo, RegisterInfo registerInfo) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepo = appUserRepo;
        this.registerInfo = registerInfo;
    }

    public RegisterStatus registerNewUser(String username, String password, String passwordValidator) {
        if(checkIsUsernameAvailable(username)) {
            if(checkIsPasswordAreSame(password, passwordValidator)) {
                if(checkPassword(password)) {
                    saveUser(username, password);
                    return new RegisterStatus(true, registerInfo.getRegisterCorrectMessage());
                } else {
                    return new RegisterStatus(false, registerInfo.getWrongPassword());
                }
            } else {
                return new RegisterStatus(false, registerInfo.getDifferentPassword());
            }
        }
        return new RegisterStatus(false, registerInfo.getUsernameNotAvailable());
    }

    private boolean checkIsPasswordAreSame(String password, String passwordValidator) {
        boolean passwordAreSame = false;
        if(password.equals(passwordValidator)) {
            passwordAreSame = true;
        }
        return passwordAreSame;
    }

    private boolean checkPassword(String password) {
        boolean isPasswordCorrect = false;
        int counter = 0;
        for(int i=0; i<password.length(); i++) {
            if(Character.isDigit(password.charAt(i))) {
                counter++;
            }
        }
        if(password.length() > 10 && password.length() < 20 && counter > 2) {
            isPasswordCorrect = true;
        }
        return isPasswordCorrect;
    }

    private boolean checkIsUsernameAvailable (String username) {
        boolean freeUsername = false;
        if(Optional.ofNullable(appUserRepo.findAllByUsername(username)).isEmpty()) {
            freeUsername = true;
        }
        return freeUsername;
    }

    private boolean saveUser(String username, String password) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(password));
        appUserRepo.save(appUser);
        return true;
    }
}
