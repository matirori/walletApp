package com.rohraff.walletdemoapp.gui;

import com.rohraff.walletdemoapp.appConfig.model.RegisterStatus;
import com.rohraff.walletdemoapp.appConfig.service.RegisterService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = RegistrationPage.ROUTE)
@PageTitle("register")
public class RegistrationPage extends HorizontalLayout {

    public static final String ROUTE = "register";
    private RegisterService registerService;

    public RegistrationPage(RegisterService registerService) {
        this.registerService = registerService;

        VerticalLayout formLayoutRegistration = new VerticalLayout();
        formLayoutRegistration.setJustifyContentMode(JustifyContentMode.BETWEEN);
        formLayoutRegistration.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        TextField username = new TextField();
        username.setLabel("Podaj nazwę użytkownika");
        PasswordField userPassword = new PasswordField();
        userPassword.setLabel("Wprowadź Hasło");
        PasswordField userPasswordConfirm = new PasswordField();
        userPasswordConfirm.setLabel("Potwierdź hasło");
        Button buttonSignUp = new Button("Zarejestruj się");
        buttonSignUp.addClickListener(clickEvent -> {
            RegisterStatus info = registerService.registerNewUser(username.getValue(),
                            userPassword.getValue(),
                            userPasswordConfirm.getValue());
                    if(info.isRegistered()) {
                        UI.getCurrent().navigate("login");
                    } else {
                        Notification.show(
                                info.getInfo());
                    }
                });

        formLayoutRegistration.add(username);
        formLayoutRegistration.add(userPassword);
        formLayoutRegistration.add(userPasswordConfirm);
        formLayoutRegistration.add(buttonSignUp);

        add(formLayoutRegistration);
    }
}
