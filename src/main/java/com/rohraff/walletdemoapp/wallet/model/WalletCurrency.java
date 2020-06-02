package com.rohraff.walletdemoapp.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletCurrency {
    private BigDecimal balance;
    private BigDecimal cash;
    private BigDecimal savings;
    private BigDecimal rate;
    private CurrencyName currencyRateName;
}
