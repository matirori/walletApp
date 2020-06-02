package com.rohraff.walletdemoapp.wallet.service;

import com.rohraff.walletdemoapp.wallet.model.DepositCategory;
import com.rohraff.walletdemoapp.wallet.model.DepositTransaction;
import com.rohraff.walletdemoapp.wallet.repository.DepositTransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class DepositStatisticsService {

    private DepositTransactionRepo depositTransactionRepo;

    public HashMap<DepositCategory, Double> getSumMap(String currentUser) {
        HashMap<DepositCategory, Double> map = new HashMap<>();
        for(DepositCategory category: DepositCategory.values()) {
            Double total = depositTransactionRepo.findAllByWallet_AppUser_UsernameAndCategory(currentUser, category)
                    .stream()
                    .map(DepositTransaction::getAmount)
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();
            map.put(category, total);
        }
        return map;
    }

    public HashMap<DepositCategory, Double> getPercentageMap(String currentUsername) {
        HashMap<DepositCategory, Double> mapPercentage = getSumMap(currentUsername);
        double sum = 0;
        for (DepositCategory key : mapPercentage.keySet()) {
            sum += mapPercentage.get(key);
        }
        for (DepositCategory key : mapPercentage.keySet()) {
            Double currentValue = mapPercentage.get(key);
            mapPercentage.replace(key, currentValue, BigDecimal.valueOf(currentValue).divide(BigDecimal.valueOf(sum), RoundingMode.DOWN).multiply(new BigDecimal(100)).doubleValue());
        }
        return mapPercentage;
    }

    public boolean validate(String currentUsername) {
        double zero = 0;
        boolean divideByZero = false;
        HashMap<DepositCategory, Double> map = getSumMap(currentUsername);
        for (DepositCategory key : map.keySet()) {
            if(!map.get(key).equals(zero)) {
                divideByZero = true;
                break;
            }
        }
        return divideByZero;
    }
}
