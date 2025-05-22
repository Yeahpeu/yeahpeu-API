package com.yeahpeu.common.validation;

public class AmountValidator {

    public static boolean validate(long price) {
        return price >= 0 && price <= 1_000_000_000;
    }
}
