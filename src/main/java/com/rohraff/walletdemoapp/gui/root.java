package com.rohraff.walletdemoapp.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class root extends VerticalLayout {

    public root() {
        UI.getCurrent().navigate("login");
        UI.getCurrent().getPage().reload();
    }
}
