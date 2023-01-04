package uk.ac.ed.inf;

import uk.ac.ed.inf.Exceptions.InvalidCardNoException;
import uk.ac.ed.inf.Exceptions.InvalidCvvException;
import uk.ac.ed.inf.Exceptions.InvalidCreditCardExpiryException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Logic for credit card validation.
 */
public class CreditCardValidation {
    /**
     * Confirms that the credit card details are valid, otherwise
     * throws an appropriate exception.
     * Currently supports Visa and Mastercard credit cards.
     * @param cardNo the 16 digit credit card number.
     * @param expiryDate the expiry date in MM/YY format.
     * @param cvv the 3 digit cvv number.
     * @param orderDate the date of an order for checking expiration.
     * @return <code>true</code> if the credit card is valid.
     * @throws InvalidCardNoException if the credit card number is invalid.
     * @throws InvalidCreditCardExpiryException if the credit card expiration
     * is invalid.
     * @throws InvalidCvvException if the credit card cvv number is invalid.
     */
    public static boolean validateCreditCard(String cardNo, String expiryDate,
                                             String cvv, String orderDate)
            throws InvalidCardNoException,
            InvalidCreditCardExpiryException, InvalidCvvException {
        if (!validCardNo(cardNo)) {
            throw new InvalidCardNoException();
        } else if (expired(expiryDate, orderDate)) {
            throw new InvalidCreditCardExpiryException();
        } else if (!validCvv(cvv)) {
            throw new InvalidCvvException();
        }
        return true;
    }

    /**
     * Confirms that the credit card details are valid, otherwise
     * throws an appropriate exception.
     * Currently supports Visa and Mastercard credit cards.
     * @param cardNo the 16 digit credit card number.
     * @param expiryDate the expiry date in MM/YY format.
     * @param cvv the 3 digit cvv number.
     * @param orderDate the date of an order for checking expiration.
     * @return <code>ValidButNotDelivered</code> if the credit card is valid.
     */
    public static OrderOutcome validateCreditCardAlternative(String cardNo, String expiryDate,
                                             String cvv, String orderDate) {
        if (!validCardNo(cardNo)) {
            return OrderOutcome.InvalidCardNumber;
        } else if (expired(expiryDate, orderDate)) {
            return OrderOutcome.InvalidExpiryDate;
        } else if (!validCvv(cvv)) {
            return OrderOutcome.InvalidCvv;
        }
        return OrderOutcome.ValidButNotDelivered;
    }

    private static boolean validCardNo(String cardNo) {
        if (cardNo == null) {
            return false;
        }
        return cardNo.length() == 16
                && cardNo.matches("\\d+")
                && checkLuhn(cardNo)
                && (isVisa(cardNo) || isMastercard(cardNo));
    }
    private static boolean checkLuhn(String cardNo) {
        // Algorithm from https://www.geeksforgeeks.org/luhn-algorithm/
        var nDigits = cardNo.length();
        var nSum = 0;
        var isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            var d = cardNo.charAt(i) - '0';
            if (isSecond == true) {
                d = d * 2;
            }
            // Add two digits to handle cases that make two digits after doubling
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
                var second = Integer.parseInt(cardNo.substring(0, 4));
                return first || (second >= 2221 && second <= 2720);
            } catch (Exception e) {
                return false; // if the credit card number cannot be parsed it's invalid.
            }
        }

        return false;
    }



    private static boolean expired(String date, String orderDate) {
        if (date == null || orderDate == null) {
            return true;
        }
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
            var finalOrderDate = LocalDate.ofInstant(tempOrderDate.toInstant(),
                    ZoneId.systemDefault());
            return finalExpiration.isBefore(finalOrderDate); // then expired
        } catch (ParseException p) {
            return true; // all invalid dates are treated as expired
        }
    }

    private static boolean validCvv(String cvv) {
        if (cvv == null) {
            return false;
        }
        return cvv.length() == 3 && cvv.matches("\\d+");
    }
}
