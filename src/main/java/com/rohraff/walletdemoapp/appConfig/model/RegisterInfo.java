package com.rohraff.walletdemoapp.appConfig.model;

import org.springframework.stereotype.Component;

@Component
public class RegisterInfo {

    private final String registerCorrectMessage = "Użytkownik został zarejestrowany pomyślnie";
    private final String usernameNotAvailable = "Nazwa użytkownika jest już zajęta";
    private final String differentPassword = "Podane hasła różnią się od siebie";
    private final String wrongPassword = "Podane hasło jest za krótkie bądź, nie zawiera minmum dwóch znaków";

    public RegisterInfo() {
    }

    public String getRegisterCorrectMessage() {
        return registerCorrectMessage;
    }

    public String getUsernameNotAvailable() {
        return usernameNotAvailable;
    }

    public String getDifferentPassword() {
        return differentPassword;
    }

    public String getWrongPassword() {
        return wrongPassword;
    }
}
