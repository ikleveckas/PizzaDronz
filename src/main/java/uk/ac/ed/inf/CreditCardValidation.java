package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.InvalidCardNoException;
import uk.ac.ed.inf.Exceptions.InvalidCvvException;
import uk.ac.ed.inf.Exceptions.InvalidExpiryDateException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreditCardValidation {
    // Need to think about exceptions!
    public static boolean validateCreditCard(String cardNo, String expiryDate,
                                             String cvv, String orderDate)
            throws InvalidCardNoException,
            InvalidExpiryDateException, InvalidCvvException {
        if (!validCardNo(cardNo)) {
            throw new InvalidCardNoException();
        } else if (expired(expiryDate, orderDate)) {
            throw new InvalidExpiryDateException();
        } else if (!validCvv(cvv)) {
            throw new InvalidCvvException();
        }
        return true;
    }

    private static boolean validCardNo(String cardNo) {
        return cardNo.length() == 16
                && cardNo.matches("\\d+")
                && checkLuhn(cardNo)
                && (isVisa(cardNo) || isMastercard(cardNo));
    }
    private static boolean checkLuhn(String cardNo) {
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

    private static boolean isVisa(String cardNo) {
        if (cardNo.length() > 0) {
            return cardNo.charAt(0) == '4';
        }
        return false;
    }


    private static boolean isMastercard(String cardNo) {
        if (cardNo.length() > 3) {
            var first = cardNo.charAt(0) == '5' && (cardNo.charAt(1) == '1'
                    || cardNo.charAt(1) == '2'
                    || cardNo.charAt(1) == '3'
                    || cardNo.charAt(1) == '4'
                    || cardNo.charAt(1) == '5');
            try {
                var second = Integer.parseInt(cardNo.substring(0, 4)); // maybe no need to parse
                return first || (second >= 2221 && second <= 2720);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }



    private static boolean expired(String date, String orderDate) { // MIGHT NEED TO CHANGE FROM NOW TO ORDER DATE
        SimpleDateFormat creditCardFormat = new SimpleDateFormat("MM/yy");
        creditCardFormat.setLenient(false);
        SimpleDateFormat isoFormat = new SimpleDateFormat("yy-MM-dd");
        isoFormat.setLenient(false);
        try {
            var tempExpiryDate = creditCardFormat.parse(date);
            var tempOrderDate = isoFormat.parse(orderDate);
            // expiration on last day of the month
            var finalExpiration = LocalDate.ofInstant(tempExpiryDate.toInstant(),
                    ZoneId.systemDefault()).plusMonths(1);
            var finalOrderDate = LocalDate.ofInstant(tempExpiryDate.toInstant(),
                    ZoneId.systemDefault());
            return finalExpiration.isBefore(finalOrderDate);
        } catch (ParseException p) {
            return true; // all invalid dates are treated as expired
        }
    }

    private static boolean validCvv(String cvv) {
        return cvv.length() == 3 && cvv.matches("\\d+");
    }
}
