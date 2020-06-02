package com.rohraff.walletdemoapp.apiCurrencyClient.service;

import com.rohraff.walletdemoapp.apiCurrencyClient.model.Currency;
import com.rohraff.walletdemoapp.wallet.model.CurrencyName;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    private final String baseUrl = "https://api.exchangeratesapi.io/latest";

    public Currency getCurrentCurrencyRate(CurrencyName currencyName) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(getUrl(currencyName.toString()), Currency.class);
    }

    private String getUrl(String currencyName) {
        return baseUrl+"?base="+currencyName;
    }
}
