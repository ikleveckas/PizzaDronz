package uk.ac.ed.inf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Validation {
    static boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    static boolean expired(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        try {
            var exp = simpleDateFormat.parse(date);
            // expiration on last day of the month
            var finalExpiration = LocalDate.ofInstant(exp.toInstant(),
                    ZoneId.systemDefault()).plusMonths(1);
            return finalExpiration.isBefore(LocalDate.now());
        } catch (ParseException p) {
            return true; // all invalid dates are treated as expired
        }
    }

    static boolean validCvv(String cardNo, String cvv) {
        return cvv.length() == 3;
    }

    static boolean isVisa(String cardNo) {
        if (cardNo.length() > 0) {
            return cardNo.charAt(0) == '4';
        }
        return false;
    }

    static boolean isMastercard(String cardNo) {
        if (cardNo.length() > 1) {
            return cardNo.charAt(0) == '5' && (cardNo.charAt(1) == '1'
                    || cardNo.charAt(1) == '2'
                    || cardNo.charAt(1) == '3'
                    || cardNo.charAt(1) == '4'
                    || cardNo.charAt(1) == '5');
        }
        return false;
    }

    static boolean isAmex(String cardNo) {
        // TO DO
        return false;
    }
}
