package com.rohraff.walletdemoapp.wallet.repository;

import com.rohraff.walletdemoapp.wallet.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalTransactionRepo extends JpaRepository<WithdrawalTransaction, Long> {

    List<WithdrawalTransaction> findAllByWallet_AppUser_UsernameAndCategory(String authenticatedUsername, WithdrawalCategory category);
    void deleteWithdrawalTransactionByWalletAndId(Wallet wallet, Long id);
    WithdrawalTransaction findWithdrawalTransactionByWalletAndId(Wallet wallet, Long id);
    List<WithdrawalTransaction> findAllByWallet(Wallet wallet);
}
