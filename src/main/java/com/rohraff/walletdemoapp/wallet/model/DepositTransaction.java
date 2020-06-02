package com.rohraff.walletdemoapp.wallet.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "DEPOSIT_TRANSACTIONS")
public class DepositTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionName;
    private BigDecimal amount;
    private Enum<DepositCategory> category;
    private LocalDate date;
    private LocalTime timeOfTransaction;
    @ManyToOne
    private Wallet wallet;

}
