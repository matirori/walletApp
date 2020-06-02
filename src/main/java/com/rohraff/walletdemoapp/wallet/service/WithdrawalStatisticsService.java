package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.wallet.model.DepositCategory;
import com.rohraff.walletdemoapp.wallet.model.DepositTransaction;
import com.rohraff.walletdemoapp.wallet.model.WithdrawalCategory;
import com.rohraff.walletdemoapp.wallet.model.WithdrawalTransaction;
import com.rohraff.walletdemoapp.wallet.repository.WithdrawalTransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class WithdrawalStatisticsService {

    private WithdrawalTransactionRepo withdrawalTransactionRepo;

    public HashMap<WithdrawalCategory, Double> getSumMap(String currentUser) {
        HashMap<WithdrawalCategory, Double> map = new HashMap<>();
        for(WithdrawalCategory category: WithdrawalCategory.values()) {
            Double total = withdrawalTransactionRepo.findAllByWallet_AppUser_UsernameAndCategory(currentUser, category)
                    .stream()
                    .map(WithdrawalTransaction::getAmount)
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();
            map.put(category, total);
        }
        return map;
    }

    public HashMap<WithdrawalCategory, Double> getPercentageMap(String currentUsername) {
        HashMap<WithdrawalCategory, Double> mapPercentage = getSumMap(currentUsername);
        double sum = 0;
        for (WithdrawalCategory key : mapPercentage.keySet()) {
            sum += mapPercentage.get(key);
        }
        for (WithdrawalCategory key : mapPercentage.keySet()) {
            Double currentValue = mapPercentage.get(key);
            mapPercentage.replace(key, currentValue, BigDecimal.valueOf(currentValue).divide(BigDecimal.valueOf(sum), RoundingMode.DOWN).multiply(new BigDecimal(100)).doubleValue());
        }
        return mapPercentage;
    }

    public boolean validateWithdrawal(String currentUsername) {
        double zero = 0;
        boolean divideByZero = false;
        HashMap<WithdrawalCategory, Double> map = getSumMap(currentUsername);
        for (WithdrawalCategory key : map.keySet()) {
            if(!map.get(key).equals(zero)) {
                divideByZero = true;
                break;
            }
        }
        return divideByZero;
    }
}
