package com.rohraff.walletdemoapp.wallet.model;

import com.rohraff.walletdemoapp.appConfig.model.AppUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "WALLETS")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal cash;
    private BigDecimal savings;
    private BigDecimal balance;
    @OneToOne
    private AppUser appUser;
    @OneToMany(mappedBy ="wallet")
    private Set<DepositTransaction> depositTransactionSet;
    @OneToMany(mappedBy ="wallet")
    private Set<WithdrawalTransaction> withdrawalTransactionSet;

    public Wallet() {
    }
}
