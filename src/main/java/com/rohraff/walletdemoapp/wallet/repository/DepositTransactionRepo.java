package com.rohraff.walletdemoapp.wallet.repository;

import com.rohraff.walletdemoapp.wallet.model.DepositCategory;
import com.rohraff.walletdemoapp.wallet.model.DepositTransaction;
import com.rohraff.walletdemoapp.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositTransactionRepo extends JpaRepository<DepositTransaction, Long> {

    List<DepositTransaction> findAllByWallet_AppUser_UsernameAndCategory(String authenticatedUsername, DepositCategory category);
    void deleteDepositTransactionByWalletAndId(Wallet wallet, Long id);
    DepositTransaction findDepositTransactionByWalletAndId(Wallet wallet, Long id);
    List<DepositTransaction> findAllByWallet(Wallet wallet);
}
