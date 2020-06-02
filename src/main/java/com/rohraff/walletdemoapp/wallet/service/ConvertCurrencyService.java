package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.apiCurrencyClient.model.Rates;
import com.rohraff.walletdemoapp.apiCurrencyClient.service.CurrencyService;
import com.rohraff.walletdemoapp.wallet.model.CurrencyName;
import com.rohraff.walletdemoapp.wallet.model.Wallet;
import com.rohraff.walletdemoapp.wallet.model.WalletCurrency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConvertCurrencyService {

    private CurrencyService currencyService;
    private WalletService walletService;

    public Optional<WalletCurrency> convertFunds(String authenticatedUser, CurrencyName currencyToConvert) {
        Wallet wallet = walletService.getCurrentBalance(walletService.retrieveCurrentUserWallet(authenticatedUser).get());
        Rates rates = currencyService.getCurrentCurrencyRate(currencyToConvert).getRates();
        //need to change when option to choose base Currency for wallet is enable
        BigDecimal walletCurrencyRate = BigDecimal.valueOf(rates.getPLN());
        WalletCurrency walletCurrency = new WalletCurrency();
        walletCurrency.setBalance(wallet.getBalance().divide(walletCurrencyRate, RoundingMode.HALF_UP));
        walletCurrency.setCash(wallet.getCash().divide(walletCurrencyRate , RoundingMode.HALF_UP));
        walletCurrency.setSavings(wallet.getSavings().divide(walletCurrencyRate, RoundingMode.HALF_UP));
        walletCurrency.setRate(walletCurrencyRate.setScale(2, RoundingMode.HALF_UP));
        walletCurrency.setCurrencyRateName(currencyToConvert);
        return Optional.ofNullable(walletCurrency);
    }
}
