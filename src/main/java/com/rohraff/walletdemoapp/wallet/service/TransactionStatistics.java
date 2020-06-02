package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.wallet.model.DepositTransaction;
import com.rohraff.walletdemoapp.wallet.model.Wallet;
import com.rohraff.walletdemoapp.wallet.model.WithdrawalTransaction;
import com.rohraff.walletdemoapp.wallet.repository.DepositTransactionRepo;
import com.rohraff.walletdemoapp.wallet.repository.WithdrawalTransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransactionStatistics {

    private DepositTransactionRepo depositTransactionRepo;
    private WithdrawalTransactionRepo withdrawalTransactionRepo;

    public BigDecimal getDifferenceBetweenTwoTransSum(Wallet wallet) {
        return getSumFromDepositTrans(wallet).subtract(getSumFromWithdrawalTrans(wallet));
    }

    private BigDecimal getSumFromDepositTrans(Wallet wallet) {
        return depositTransactionRepo.findAllByWallet(wallet)
                .stream()
                .map(DepositTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getSumFromWithdrawalTrans(Wallet wallet) {
        return withdrawalTransactionRepo.findAllByWallet(wallet)
                .stream()
                .map(WithdrawalTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
