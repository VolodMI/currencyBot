package com.firstbot.ua.CurrencyRateBot.service;

import com.firstbot.ua.CurrencyRateBot.model.CurrencyModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class CurrencyService {

    public static String getCurrencyRate(String message, CurrencyModel model) throws IOException {
        URL url = new URL("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner((InputStream) url.getContent());
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        Date date = new Date();
        JSONArray jsonArray = new JSONArray(result.toString());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            model.setCcy(object.getString("ccy"));
            model.setBase_ccy(object.getString("base_ccy"));
            model.setBuy(object.getDouble("buy"));
            model.setSale(object.optDouble("sale"));
            response.append("Official rate of UAH to ")
                    .append(model.getCcy()).append("\n")
                    .append("on the date: ")
                    .append(date).append("\n")
                    .append("buy: ").append(model.getBuy())
                    .append("\n").append("sale: ")
                    .append(model.getSale());
        }
        return response.toString();
    }
}
