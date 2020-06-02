package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.wallet.model.*;
import com.rohraff.walletdemoapp.wallet.repository.WithdrawalTransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class WithdrawalTransactionService {

    private WithdrawalTransactionRepo withdrawalTransactionRepo;
    private WalletService walletService;

    public Optional<List<WithdrawalTransaction>> getAllWithdrawalTransaction() {
        return Optional.ofNullable(withdrawalTransactionRepo.findAll());
    }


    public void createWithdrawalTransaction (String currentUser, String name, BigDecimal amount, WithdrawalCategory category, LocalDate date, LocalTime time) {
        Optional<Wallet> walletOptional;
        walletOptional = walletService.retrieveCurrentUserWallet(currentUser);
        if(walletOptional.isPresent()) {
            WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction();
            withdrawalTransaction.setTransactionName(name);
            withdrawalTransaction.setAmount(amount);
            withdrawalTransaction.setCategory(category);
            withdrawalTransaction.setDate(date);
            withdrawalTransaction.setTimeOfTransaction(time);
            withdrawalTransaction.setWallet(walletOptional.get());
            withdrawalTransactionRepo.save(withdrawalTransaction);
        }
    }

    @Transactional
    public void deleteWithdrawalTransaction(String currentUser, Set<WithdrawalTransaction> transactionSet) {
        Optional<Wallet> walletOptional;
        walletOptional = walletService.retrieveCurrentUserWallet(currentUser);
        if(walletOptional.isPresent()) {
            for(WithdrawalTransaction transaction : transactionSet) {
                withdrawalTransactionRepo.deleteWithdrawalTransactionByWalletAndId(walletOptional.get(),transaction.getId());
            }
        }
    }

    public void editTransaction(Set<WithdrawalTransaction> value, String authenticatedUserName, String transactionName, BigDecimal amount, WithdrawalCategory category, LocalDate date, LocalTime time) {
        Optional<Wallet> walletOptional =  walletService.retrieveCurrentUserWallet(authenticatedUserName);
        WithdrawalTransaction withdrawalTransaction = withdrawalTransactionRepo.findWithdrawalTransactionByWalletAndId(walletOptional.get(), value.iterator().next().getId());
        withdrawalTransaction.setAmount(amount);
        withdrawalTransaction.setTimeOfTransaction(time);
        withdrawalTransaction.setDate(date);
        withdrawalTransaction.setTransactionName(transactionName);
        withdrawalTransaction.setCategory(category);
        withdrawalTransactionRepo.save(withdrawalTransaction);
    }
}
