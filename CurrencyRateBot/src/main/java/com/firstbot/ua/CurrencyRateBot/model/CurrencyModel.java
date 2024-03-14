package com.firstbot.ua.CurrencyRateBot.model;

import lombok.Data;

@Data
public class CurrencyModel {
    String ccy;
    String base_ccy;
    double buy;
    double sale;
}
