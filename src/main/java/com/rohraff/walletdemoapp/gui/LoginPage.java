package com.rohraff.walletdemoapp.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = LoginPage.ROUTE)
@PageTitle("Login")
public class LoginPage extends VerticalLayout {
    public static final String ROUTE = "login";

    private LoginForm login = new LoginForm(); //

    public LoginPage(){
        login.setAction("login");
        getElement().appendChild(login.getElement());
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        Button buttonRegister = new Button("Zarejestruj się", e-> UI.getCurrent().navigate("register"));
        verticalLayout.add(login);
        verticalLayout.add(buttonRegister);
        add(verticalLayout);
    }
}
