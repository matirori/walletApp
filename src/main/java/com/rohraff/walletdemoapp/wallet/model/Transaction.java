package com.rohraff.walletdemoapp.wallet.model;

import java.math.BigDecimal;

public interface Transaction {
    void updateBalance(BigDecimal amount);
}
