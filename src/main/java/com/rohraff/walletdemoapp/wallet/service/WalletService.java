package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.appConfig.repository.AppUserRepo;
import com.rohraff.walletdemoapp.wallet.model.Wallet;
import com.rohraff.walletdemoapp.wallet.repository.WalletRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private WalletRepo walletRepo;
    private AppUserRepo appUserRepo;
    private TransactionStatistics transactionStatistics;

    public Optional<Wallet> retrieveCurrentUserWallet(String name) {
        return Optional.ofNullable(walletRepo.findWalletByAppUser_Id(appUserRepo.findAppUserByUsername(name).getId()));
    }

    public void createNewWallet(String currentUser, String name, BigDecimal cash, BigDecimal savings) {
        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setCash(cash);
        wallet.setSavings(savings);
        wallet.setBalance(cash.add(savings));
        wallet.setAppUser(appUserRepo.findAppUserByUsername(currentUser));
        walletRepo.save(wallet);
    }

    public void updateWallet(Wallet wallet) {
        walletRepo.save(wallet);
    }

    public Wallet getCurrentBalance(Wallet wallet) {
        wallet.setCash(wallet.getCash().add(transactionStatistics.getDifferenceBetweenTwoTransSum(wallet)));
        wallet.setBalance(wallet.getCash().add(wallet.getSavings()));
        return wallet;
    }
}
