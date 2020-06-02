package com.rohraff.walletdemoapp.gui;

import com.rohraff.walletdemoapp.apiCurrencyClient.service.CurrencyService;
import com.rohraff.walletdemoapp.wallet.model.*;
import com.rohraff.walletdemoapp.wallet.service.ConvertCurrencyService;
import com.rohraff.walletdemoapp.wallet.service.DepositStatisticsService;
import com.rohraff.walletdemoapp.wallet.service.WalletService;
import com.rohraff.walletdemoapp.wallet.service.WithdrawalStatisticsService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

@Route("main")
public class MainView extends VerticalLayout {

    private WalletService walletService;
    private DepositStatisticsService depositStatisticsService;
    private WithdrawalStatisticsService withdrawalStatisticsService;
    private ConvertCurrencyService convertCurrencyService;

    @Autowired
    public MainView(WalletService walletService, DepositStatisticsService depositStatisticsService, WithdrawalStatisticsService withdrawalStatisticsService, ConvertCurrencyService convertCurrencyService) {
        this.walletService = walletService;
        this.depositStatisticsService = depositStatisticsService;
        this.withdrawalStatisticsService = withdrawalStatisticsService;
        this.convertCurrencyService = convertCurrencyService;
        String authenticatedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Wallet> walletOptional = walletService.retrieveCurrentUserWallet(authenticatedUserName);

        //Creating MenuBar to navigate on app
        MenuBar menuBar = new MenuBar();

        menuBar.setOpenOnHover(true);

        menuBar.addItem("Transakcje", e -> UI.getCurrent().navigate("transaction"));
        menuBar.addItem("Wyloguj", e -> {
            SecurityContextHolder.clearContext();
            UI.getCurrent().navigate("login");
        });

        add(menuBar);

        //Select box to convert money
        Select<CurrencyName> selectConvertFunds = new Select<>();
        selectConvertFunds.setLabel("Wybierz walutę");
        selectConvertFunds.setItems(CurrencyName.values());

        Button buttonConvert = new Button();
        buttonConvert.setText("Przelicz");
        buttonConvert.addClickListener(e-> {
            Optional<Wallet> wallet = walletService.retrieveCurrentUserWallet(authenticatedUserName);
            if(wallet.isPresent()) {
                if(!wallet.get().getSavings().equals(BigDecimal.ZERO) && !wallet.get().getCash().equals(BigDecimal.ZERO)) {
                    Dialog dialogConvertFunds = new Dialog();
                    Optional<WalletCurrency> walletCurrencyOptional = convertCurrencyService.convertFunds(authenticatedUserName, selectConvertFunds.getValue());
                    Details convertFundsInfo = new Details();
                    convertFundsInfo.setOpened(true);
                    convertFundsInfo.setSummaryText("Kurs " + walletCurrencyOptional.get().getCurrencyRateName() + " wynosi " + walletCurrencyOptional.get().getRate());
                    convertFundsInfo.addContent(new H3("Saldo Konta:"), new Text(walletCurrencyOptional.get().getBalance().toString()));
                    convertFundsInfo.addContent(new H3("Gotówka: "), new Text(walletCurrencyOptional.get().getCash().toString()));
                    convertFundsInfo.addContent(new H3("Oszczędności"), new Text(walletCurrencyOptional.get().getSavings().toString()));
                    dialogConvertFunds.add(convertFundsInfo);
                    dialogConvertFunds.open();
                }
            } else {
                Notification.show("Brak funduszy do przeliczenia");
            }
        });

        add(selectConvertFunds);
        add(buttonConvert);

        //Layout to create new Wallet, if crated layout is hidden
        HorizontalLayout layoutCreateNewWallet = new HorizontalLayout();
        layoutCreateNewWallet.setWidth("100%");
        layoutCreateNewWallet.setHeight("150px");
        layoutCreateNewWallet.getStyle().set("border", "1px solid #9E9E9E");
        layoutCreateNewWallet.setJustifyContentMode(JustifyContentMode.START);

        TextArea textAreaWalletName = new TextArea("Nazwa portfela");
        textAreaWalletName.setPlaceholder("Podaj nazwę dla swojego e_portfela");

        BigDecimalField bigDecimalFieldCashValue = new BigDecimalField("Gotówka");
        bigDecimalFieldCashValue.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalFieldCashValue.setPrefixComponent(new Icon(VaadinIcon.MONEY));

        BigDecimalField bigDecimalFieldSavingsValue = new BigDecimalField("Oszczędności");
        bigDecimalFieldSavingsValue.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalFieldSavingsValue.setPrefixComponent(new Icon(VaadinIcon.MONEY));

        Button buttonCreateNewWallet = new Button("Stwórz Portfel");
        buttonCreateNewWallet.addClickListener(clickEvent -> {
            walletService.createNewWallet(authenticatedUserName,
                    textAreaWalletName.getValue(),
                    bigDecimalFieldCashValue.getValue(),
                    bigDecimalFieldSavingsValue.getValue() );
            UI.getCurrent().getPage().reload();
        });

        layoutCreateNewWallet.add(textAreaWalletName,
                bigDecimalFieldCashValue,
                bigDecimalFieldSavingsValue,
                buttonCreateNewWallet);

        // shorthand methods for changing the component theme variants
        layoutCreateNewWallet.setPadding(true);
        layoutCreateNewWallet.setMargin(true);
        // just a demonstration of the API, by default the spacing is on
        layoutCreateNewWallet.setSpacing(true);

        //Info about status of wallet. When wallet is not created, make layout to create wallet
        VerticalLayout walletInfoAndConvertFunds = new VerticalLayout();
        add(walletInfoAndConvertFunds);
        Details currentWalletInfo = new Details();
        if(walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            Wallet currentBalance = walletService.getCurrentBalance(wallet);
            currentWalletInfo.setSummaryText("Portfel użytkownika " + authenticatedUserName );
            currentWalletInfo.addContent(new H3("Saldo Konta:"), new Text(currentBalance.getBalance().toString()));
            currentWalletInfo.addContent(new H3("Gotówka: "), new Text(currentBalance.getCash().toString()));
            currentWalletInfo.addContent(new H3("Oszczędności"), new Text(currentBalance.getSavings().toString()));
            currentWalletInfo.addThemeVariants(DetailsVariant.FILLED);
            currentWalletInfo.setOpened(true);
            walletInfoAndConvertFunds.add(currentWalletInfo);
        } else {
            currentWalletInfo.setSummaryText("Stwórz swój pierwszy portfel");
            currentWalletInfo.addContent(new H3(""), new Text("Brak Danych"));
            add(currentWalletInfo);
            add(layoutCreateNewWallet);
        }

        // Wyświertlanie sumy wpłat z każdej kategorii
        HashMap<DepositCategory, Double> map = depositStatisticsService.getSumMap(authenticatedUserName);
        VerticalLayout layoutSumDeposit = new VerticalLayout();
        layoutSumDeposit.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        Label labelInfo = new Label("Suma wpłat z każdej kategorii");
        layoutSumDeposit.add(labelInfo);
        for (DepositCategory category: DepositCategory.values()) {
            Label label = new Label(category.name() + "  " +  map.get(category));
            layoutSumDeposit.add(label);
        }
        add(layoutSumDeposit);

        // Wyświertlanie sumy wpłat z każdej kategorii
        HashMap<WithdrawalCategory, Double> mapWithdrawal = withdrawalStatisticsService.getSumMap(authenticatedUserName);
        VerticalLayout layoutSumWithdrawal = new VerticalLayout();
        layoutSumWithdrawal.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        Label labelInfoWithdrawalSum = new Label("Suma wypłat z każdej kategorii");
        layoutSumWithdrawal.add(labelInfoWithdrawalSum);
        for (WithdrawalCategory category: WithdrawalCategory.values()) {
            Label label = new Label(category.name() + "  " +  mapWithdrawal.get(category));
            layoutSumWithdrawal.add(label);
        }
        add(layoutSumWithdrawal);

        VerticalLayout verticalLayoutDepositStatistics = new VerticalLayout();
        verticalLayoutDepositStatistics.add(layoutSumDeposit);

        VerticalLayout verticalLayoutWithdrawalStatistics = new VerticalLayout();
        verticalLayoutWithdrawalStatistics.add(layoutSumWithdrawal);

        HorizontalLayout horizontalLayoutStatistics = new HorizontalLayout();
        horizontalLayoutStatistics.add(verticalLayoutWithdrawalStatistics, verticalLayoutDepositStatistics );
        add(horizontalLayoutStatistics);

        //Wyświetlanie Procentoiwej wartości każdej wpłaty
        if(depositStatisticsService.validate(authenticatedUserName)) {
            HashMap<DepositCategory, Double> mapPercent = depositStatisticsService.getPercentageMap(authenticatedUserName);
            VerticalLayout layoutPercent = new VerticalLayout();
            layoutPercent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            Label labelInfoPercent = new Label("Procent wpłat z każdej kategorii");
            layoutPercent.add(labelInfoPercent);
            for (DepositCategory category: DepositCategory.values()) {
                Label label = new Label(category.name() + "  " +  mapPercent.get(category) + "%");
                layoutPercent.add(label);
            }
            verticalLayoutDepositStatistics.add(layoutPercent);
        }

        //Wyświetlanie Procentoiwej wartości każdej wypłaty
        if(withdrawalStatisticsService.validateWithdrawal(authenticatedUserName)) {
            HashMap<WithdrawalCategory, Double> mapPercentWithdrawal = withdrawalStatisticsService.getPercentageMap(authenticatedUserName);
            VerticalLayout layoutWithdrawalPercent = new VerticalLayout();
            layoutWithdrawalPercent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            Label labelInfoPercentWithdrawal = new Label("Procent wypłat z każdej kategorii");
            layoutWithdrawalPercent.add(labelInfoPercentWithdrawal);
            for (WithdrawalCategory category: WithdrawalCategory.values()) {
                Label label = new Label(category.name() + "  " +  mapPercentWithdrawal.get(category) + "%");
                layoutWithdrawalPercent.add(label);
            }
            verticalLayoutWithdrawalStatistics.add(layoutWithdrawalPercent);
        }
    }
}
