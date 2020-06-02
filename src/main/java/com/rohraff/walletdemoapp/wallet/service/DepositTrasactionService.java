package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.wallet.model.*;
import com.rohraff.walletdemoapp.wallet.repository.DepositTransactionRepo;
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
public class DepositTrasactionService{

    private DepositTransactionRepo depositTransactionRepo;
    private WalletService walletService;

    public void createDepositTransaction(String currentUser, String name, BigDecimal amount, DepositCategory category, LocalDate date, LocalTime time) {
        Optional<Wallet> walletOptional = walletService.retrieveCurrentUserWallet(currentUser);
        if(walletOptional.isPresent()) {
            DepositTransaction depositTransaction = new DepositTransaction();
            depositTransaction.setTransactionName(name);
            depositTransaction.setAmount(amount);
            depositTransaction.setCategory(category);
            depositTransaction.setDate(date);
            depositTransaction.setTimeOfTransaction(time);
            depositTransaction.setWallet(walletOptional.get());
            depositTransactionRepo.save(depositTransaction);
        }
    }

    public Optional<List<DepositTransaction>> getAllDepositTransactions() {
        return Optional.ofNullable(depositTransactionRepo.findAll());
    }

    @Transactional
    public void deleteDepositTransaction(String currentUser, Set<DepositTransaction> transactionSet) {
        Optional<Wallet> walletOptional =  walletService.retrieveCurrentUserWallet(currentUser);
        if(walletOptional.isPresent()) {
            for(DepositTransaction transaction : transactionSet) {
                depositTransactionRepo.deleteDepositTransactionByWalletAndId(walletOptional.get(),transaction.getId());
            }
        }
    }

    public void editTransaction(Set<DepositTransaction> value, String authenticatedUserName, String transactionName, BigDecimal amount, DepositCategory category, LocalDate date, LocalTime time) {
        Optional<Wallet> walletOptional =  walletService.retrieveCurrentUserWallet(authenticatedUserName);
        DepositTransaction depositTransaction = depositTransactionRepo.findDepositTransactionByWalletAndId(walletOptional.get(), value.iterator().next().getId());
        depositTransaction.setAmount(amount);
        depositTransaction.setTimeOfTransaction(time);
        depositTransaction.setDate(date);
        depositTransaction.setTransactionName(transactionName);
        depositTransaction.setCategory(category);
        depositTransactionRepo.save(depositTransaction);
    }
}
