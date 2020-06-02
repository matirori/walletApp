package com.rohraff.walletdemoapp.wallet.repository;

import com.rohraff.walletdemoapp.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {

    Wallet findWalletByAppUser_Id(Long id);
}
