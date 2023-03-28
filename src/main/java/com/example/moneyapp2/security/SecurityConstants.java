package com.example.moneyapp2.security;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SecurityConstants {

    public static Date ISSUE_DATE = new Date(System.currentTimeMillis());
    public static Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + 1000 * 60 * 24);

    public static String SECRET_KEY = "66546A576D5A7134743777217A25432A462D4A614E645267556B587032723575";
}
