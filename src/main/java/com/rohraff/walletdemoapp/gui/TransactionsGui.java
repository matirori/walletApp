package com.rohraff.walletdemoapp.gui;

import com.rohraff.walletdemoapp.wallet.model.*;
import com.rohraff.walletdemoapp.wallet.service.DepositTrasactionService;
import com.rohraff.walletdemoapp.wallet.service.WalletService;
import com.rohraff.walletdemoapp.wallet.service.WithdrawalTransactionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("transaction")
public class TransactionsGui extends VerticalLayout {

    private DepositTrasactionService depositTrasactionService;
    private WithdrawalTransactionService withdrawalTransactionService;
    private WalletService walletService;


    @Autowired
    public TransactionsGui(DepositTrasactionService depositTrasactionService, WalletService walletService, WithdrawalTransactionService withdrawalTransactionService) {
        this.depositTrasactionService = depositTrasactionService;
        this.walletService = walletService;
        this.withdrawalTransactionService = withdrawalTransactionService;
        //Variables needed in whole GUI
        String authenticatedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Wallet> walletOptional = walletService.retrieveCurrentUserWallet(authenticatedUserName);

        //Creating MenuBar to navigate on app
        MenuBar menuBar = new MenuBar();

        menuBar.setOpenOnHover(true);

        menuBar.addItem("Strona Główna", e -> UI.getCurrent().navigate("main"));
        menuBar.addItem("Wyloguj", e -> {
            SecurityContextHolder.clearContext();
            UI.getCurrent().navigate("login");
        });

        //Okno dialogowe wpłaty i jego dodatki
        TextArea textAreaWalletName = new TextArea("Nowa Transakcja");
        textAreaWalletName.setPlaceholder("Wpisz nazwę dla nowej transakcji");

        BigDecimalField bigDecimalFieldCashValue = new BigDecimalField("Kwota Transakcji");
        bigDecimalFieldCashValue.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalFieldCashValue.setPrefixComponent(new Icon(VaadinIcon.MONEY));

        Select<DepositCategory> selectBoxDeposit = new Select<>();
        selectBoxDeposit.setLabel("Kategorie");
        selectBoxDeposit.setItems(DepositCategory.values());

        DatePicker labelDatePickerDeposit = new DatePicker();
        labelDatePickerDeposit.setLabel("Podaj Datę transakcji");

        TimePicker timePickerDeposit = new TimePicker();
        timePickerDeposit.setLabel("Podaj czas wykonania transakcji");

        Button buttonDeposit = new Button("Utwórz wpłatę pieniędzy");

        Dialog dialogDeposit = new Dialog();
        dialogDeposit.setCloseOnOutsideClick(false);
        HorizontalLayout horizontalLayoutDialogDeposit = new HorizontalLayout();
        horizontalLayoutDialogDeposit.add(textAreaWalletName);
        horizontalLayoutDialogDeposit.add(selectBoxDeposit);
        horizontalLayoutDialogDeposit.add(bigDecimalFieldCashValue);
        horizontalLayoutDialogDeposit.add(labelDatePickerDeposit);
        horizontalLayoutDialogDeposit.add(timePickerDeposit);
        dialogDeposit.add(horizontalLayoutDialogDeposit);

        Button confirmButton = new Button("Dodaj", event -> {
            if(walletOptional.isPresent()) {
                depositTrasactionService.createDepositTransaction(
                        authenticatedUserName,
                        textAreaWalletName.getValue(),
                        bigDecimalFieldCashValue.getValue(),
                        selectBoxDeposit.getValue(),
                        labelDatePickerDeposit.getValue(),
                        timePickerDeposit.getValue());
                UI.getCurrent().getPage().reload();
                Notification.show("Dodano!");
                dialogDeposit.close();
            } else {
                Notification.show("Przed utworzeniem transakcji wymagane jest utworzenie portfela");
            }
        });
        Button cancelButton = new Button("Odrzuć", event -> {
            Notification.show("Odrzucono...");
            dialogDeposit.close();
        });
        dialogDeposit.add(confirmButton, cancelButton);

        buttonDeposit.addClickListener(event -> dialogDeposit.open());

        //Okno dialogowe wypłat i jego elementy
        TextArea textAreaWalletNameWithdrawal = new TextArea("Nowa Transakcja");
        textAreaWalletNameWithdrawal.setPlaceholder("Wpisz nazwę dla nowej transakcji");
        textAreaWalletNameWithdrawal.setWidth("200px");

        BigDecimalField bigDecimalFieldCashValueWithdrawal = new BigDecimalField("Kwota Transakcji");
        bigDecimalFieldCashValueWithdrawal.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalFieldCashValueWithdrawal.setPrefixComponent(new Icon(VaadinIcon.MONEY));

        Select<WithdrawalCategory> selectWithdrawal = new Select<>();
        selectWithdrawal.setLabel("Kategoria");
        selectWithdrawal.setItems(WithdrawalCategory.values());

        DatePicker labelDatePickerWithdrawal = new DatePicker();
        labelDatePickerWithdrawal.setLabel("Podaj Datę transakccji");

        TimePicker timePickerWithdrawal = new TimePicker();
        timePickerWithdrawal.setLabel("Podaj czas wykonania transakcji");

        Button buttonWithdrawal = new Button("Utwórz wypłatę pieniędzy");

        Dialog dialogWithdrawal = new Dialog();
        dialogWithdrawal.setCloseOnOutsideClick(false);
        HorizontalLayout horizontalLayoutDialogWithdrawal = new HorizontalLayout();
        horizontalLayoutDialogWithdrawal.add(textAreaWalletNameWithdrawal);
        horizontalLayoutDialogWithdrawal.add(bigDecimalFieldCashValueWithdrawal);
        horizontalLayoutDialogWithdrawal.add(selectWithdrawal);
        horizontalLayoutDialogWithdrawal.add(labelDatePickerWithdrawal);
        horizontalLayoutDialogWithdrawal.add(timePickerWithdrawal);
        dialogWithdrawal.add(horizontalLayoutDialogWithdrawal);

        Button confirmButtonWithdrawal = new Button("Dodaj", event -> {
            if(walletOptional.isPresent()) {
                withdrawalTransactionService.createWithdrawalTransaction(
                        authenticatedUserName,
                        textAreaWalletNameWithdrawal.getValue(),
                        bigDecimalFieldCashValueWithdrawal.getValue(),
                        selectWithdrawal.getValue(),
                        labelDatePickerWithdrawal.getValue(),
                        timePickerWithdrawal.getValue());
                UI.getCurrent().getPage().reload();
                Notification.show("Dodano!");
                dialogWithdrawal.close();
            } else {
                Notification.show("Przed utworzeniem transakcji wymagane jest utworzenie portfela");
            }
        });
        Button cancelButtonWithdrawal = new Button("Odrzuć", event -> {
            Notification.show("Odrzucono...");
            dialogWithdrawal.close();
        });
        dialogWithdrawal.add(confirmButtonWithdrawal, cancelButtonWithdrawal);

        buttonWithdrawal.addClickListener(event -> dialogWithdrawal.open());

        //Wyświetlanie całej listy tranzakcji depozytowych
        Grid<DepositTransaction> gridDeposit = new Grid<>();
        Optional<List<DepositTransaction>> depositTransactionList = depositTrasactionService.getAllDepositTransactions();

        List<DepositTransaction> depositTransactionDTOS = new ArrayList<>(depositTransactionList.get());
        gridDeposit.setThemeName("Lista Wpłat");
        gridDeposit.setItems(depositTransactionDTOS);
        gridDeposit.addColumn(DepositTransaction::getTransactionName).setHeader("Nazwa Transakcji");
        gridDeposit.addColumn(DepositTransaction::getCategory).setHeader("Kategoria");
        gridDeposit.addColumn(DepositTransaction::getAmount).setHeader("Kwota");
        gridDeposit.addColumn(DepositTransaction::getDate).setHeader("Data");
        gridDeposit.addColumn((DepositTransaction::getTimeOfTransaction)).setHeader("Godzina");
        gridDeposit.setHeightByRows(true);
        gridDeposit.setMaxHeight("2000px");
        gridDeposit.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        gridDeposit.setSelectionMode(Grid.SelectionMode.MULTI);
        Button removeButtonDeposit = new Button();
        removeButtonDeposit.setText("Usuń pozycje");
        removeButtonDeposit.addClickListener(event -> {
            depositTrasactionService.deleteDepositTransaction(authenticatedUserName, gridDeposit.asMultiSelect().getValue());
            UI.getCurrent().getPage().reload();
        });

        //Wyświetlanie Listy Wypłat
        Grid<WithdrawalTransaction> gridWithdrawal = new Grid<>();
        Optional<List<WithdrawalTransaction>> withdrawalTransactionList = withdrawalTransactionService.getAllWithdrawalTransaction();

        List<WithdrawalTransaction> withdrawalTransactionDTOS = new ArrayList<>(withdrawalTransactionList.get());
        gridWithdrawal.setThemeName("Lista Wpłat");
        gridWithdrawal.setItems(withdrawalTransactionDTOS);
        gridWithdrawal.addColumn(WithdrawalTransaction::getTransactionName).setHeader("Nazwa Transakcji").setKey("name");
        gridWithdrawal.addColumn(WithdrawalTransaction::getCategory).setHeader("Kategoria");
        gridWithdrawal.addColumn(WithdrawalTransaction::getAmount).setHeader("Kwota");
        gridWithdrawal.addColumn(WithdrawalTransaction::getDate).setHeader("Data");
        gridWithdrawal.addColumn(WithdrawalTransaction::getTimeOfTransaction).setHeader("Godzina");
        gridWithdrawal.setHeightByRows(true);
        gridWithdrawal.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        //Dodanie możliwości usuwania elementu z Gridu(Wypłaty)
        gridWithdrawal.setSelectionMode(Grid.SelectionMode.MULTI);
        Button removeButtonWithdrawal = new Button();
        removeButtonWithdrawal.setText("Usuń pozycje");
        removeButtonWithdrawal.addClickListener(event -> {
                withdrawalTransactionService.deleteWithdrawalTransaction(authenticatedUserName, gridWithdrawal.asMultiSelect().getValue());
                UI.getCurrent().getPage().reload();
        });

        //Okno dialogowe modyfikacji wpłat
        Dialog dialogEditDeposit = new Dialog();

        TextArea textAreaWalletEditName = new TextArea("Edytuj nazwe transakcji");
        textAreaWalletEditName.setPlaceholder("Wpisz nową nazwę");

        BigDecimalField bigDecimalFieldEditCashValue = new BigDecimalField("Edytuj Kwotę Transakcji");
        bigDecimalFieldEditCashValue.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalFieldEditCashValue.setPrefixComponent(new Icon(VaadinIcon.MONEY));

        Select<DepositCategory> selectBoxDepositEdit = new Select<>();
        selectBoxDepositEdit.setLabel("Kategoria");
        selectBoxDepositEdit.setItems(DepositCategory.values());

        DatePicker labelDatePickerDepositEdit = new DatePicker();
        labelDatePickerDepositEdit.setLabel("Podaj Datę transakcji");

        TimePicker timePickerDepositEdit = new TimePicker();
        timePickerDepositEdit.setLabel("Podaj czas wykonania transakcji");

        HorizontalLayout horizontalLayoutEditDialogDeposit = new HorizontalLayout();
        horizontalLayoutEditDialogDeposit.add(textAreaWalletEditName);
        horizontalLayoutEditDialogDeposit.add(selectBoxDepositEdit);
        horizontalLayoutEditDialogDeposit.add(bigDecimalFieldEditCashValue);
        horizontalLayoutEditDialogDeposit.add(labelDatePickerDepositEdit);
        horizontalLayoutEditDialogDeposit.add(timePickerDepositEdit);
        dialogEditDeposit.add(horizontalLayoutEditDialogDeposit);
        Button buttonConfirmDepositEdition = new Button("Potwierdź edycje", buttonClickEvent -> {
            depositTrasactionService.editTransaction(
                    gridDeposit.asMultiSelect().getValue(),
                    authenticatedUserName,
                    textAreaWalletEditName.getValue(),
                    bigDecimalFieldEditCashValue.getValue(),
                    selectBoxDepositEdit.getValue(),
                    labelDatePickerDepositEdit.getValue(),
                    timePickerDepositEdit.getValue());
                    Notification.show("Dokonano edycji");
                    dialogEditDeposit.close();
                    UI.getCurrent().getPage().reload();

        });
        dialogEditDeposit.add(buttonConfirmDepositEdition);

        Button buttonEditDeposit = new Button("Edytuj wybraną transakcje", buttonClickEvent -> {
            if(gridDeposit.asMultiSelect().getValue().size() != 1) {
                Notification.show("Wybierz dokładnie jedną transakcję do edycji");
            } else {
                dialogEditDeposit.open();
            }
        });

        //Okno dialogowe modyfikacji wypłat
        Dialog dialogEditWithdrawal = new Dialog();
        TextArea textAreaWalletNameWithdrawalEdit = new TextArea("Nowa Transakcja");
        textAreaWalletNameWithdrawalEdit.setPlaceholder("Wpisz nazwę dla nowej transakcji");
        textAreaWalletNameWithdrawalEdit.setWidth("200px");

        BigDecimalField bigDecimalFieldCashValueWithdrawalEdit = new BigDecimalField("Kwota Transakcji");
        bigDecimalFieldCashValueWithdrawalEdit.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalFieldCashValueWithdrawalEdit.setPrefixComponent(new Icon(VaadinIcon.MONEY));

        Select<WithdrawalCategory> selectWithdrawalEdit = new Select<>();
        selectWithdrawalEdit.setLabel("Kategoria");
        selectWithdrawalEdit.setItems(WithdrawalCategory.values());

        DatePicker labelDatePickerWithdrawalEdit = new DatePicker();
        labelDatePickerWithdrawalEdit.setLabel("Podaj Datę transakccji");

        TimePicker timePickerWithdrawalEdit = new TimePicker();
        timePickerWithdrawalEdit.setLabel("Podaj czas wykonania transakcji");

        HorizontalLayout horizontalLayoutWithdrawalEdit = new HorizontalLayout();
        horizontalLayoutWithdrawalEdit.add(textAreaWalletNameWithdrawalEdit);
        horizontalLayoutWithdrawalEdit.add(bigDecimalFieldCashValueWithdrawalEdit);
        horizontalLayoutWithdrawalEdit.add(selectWithdrawalEdit);
        horizontalLayoutWithdrawalEdit.add(labelDatePickerWithdrawalEdit);
        horizontalLayoutWithdrawalEdit.add(timePickerWithdrawalEdit);
        dialogEditWithdrawal.add(horizontalLayoutWithdrawalEdit);
        Button buttonConfirmWithdrawalEdition = new Button("Potwierdź edycje", buttonClickEvent -> {
            withdrawalTransactionService.editTransaction(
                    gridWithdrawal.asMultiSelect().getValue(),
                    authenticatedUserName,
                    textAreaWalletNameWithdrawalEdit.getValue(),
                    bigDecimalFieldCashValueWithdrawalEdit.getValue(),
                    selectWithdrawalEdit.getValue(),
                    labelDatePickerWithdrawalEdit.getValue(),
                    timePickerWithdrawalEdit.getValue());
            Notification.show("Dokonano edycji");
            dialogEditDeposit.close();
            UI.getCurrent().getPage().reload();
        });
        dialogEditWithdrawal.add(buttonConfirmWithdrawalEdition);

        Button buttonEditWithdrawal = new Button("Edytuj wybraną transakcje", buttonClickEvent -> {
            if(gridWithdrawal.asMultiSelect().getValue().size() != 1) {
                Notification.show("Wybierz dokładnie jedną transakcję do edycji");
            } else {
                dialogEditWithdrawal.open();
            }
        });

        //Dodawanie elementów do widoku
        HorizontalLayout horizontalLayoutButtonsWithdrawal = new HorizontalLayout();
        horizontalLayoutButtonsWithdrawal.add(buttonWithdrawal, removeButtonWithdrawal, buttonEditWithdrawal);

        HorizontalLayout horizontalLayoutButtonsDeposit = new HorizontalLayout();
        horizontalLayoutButtonsDeposit.add(buttonDeposit, removeButtonDeposit, buttonEditDeposit);

        add(menuBar);
        add(dialogDeposit);
        add(dialogWithdrawal);
        add(dialogEditDeposit);
        add(horizontalLayoutButtonsDeposit);
        add(gridDeposit);
        add(horizontalLayoutButtonsWithdrawal);
        add(gridWithdrawal);
    }
}
