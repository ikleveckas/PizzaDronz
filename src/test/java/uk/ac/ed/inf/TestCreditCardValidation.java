package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CreditCardValidation class
 */
public class TestCreditCardValidation {

    // Mock orders
    Order invalidCardNoLength = new Order("1", "2023-02-14", "2400000",
            "04/28", "123", 2400, new String[0]);
    Order invalidCardNoValue = new Order("1", "2023-02-14", "2921228248918894",
            "04/28", "123", 2400, new String[0]);
    Order invalidCardNoNull = new Order("1", "2023-02-14", null,
            "04/28", "123", 2400, new String[0]);

    Order invalidCardNoNegative = new Order("1", "2023-02-14", "-2921228248918894",
            "04/28", "123", 2400, new String[0]);
    Order invalidExpiryDate = new Order("2", "2023-02-15", "2221228248918894",
            "04/20", "123", 2400, new String[0]);
    Order invalidExpiryFormatValue = new Order("2", "2023-02-15", "2221228248918894",
            "65/42", "123", 2400, new String[0]);
    Order invalidExpiryFormat = new Order("2", "2023-02-15", "2221228248918894",
            "42", "123", 2400, new String[0]);
    Order invalidExpiryNull = new Order("2", "2023-02-15", "2221228248918894",
            null, "123", 2400, new String[0]);

    Order invalidExpiryJustExpired = new Order("2", "2023-02-15", "2221228248918894",
            "12/22", "123", 2400, new String[0]);
    Order invalidCvv = new Order("3", "2023-02-16", "4143510992401156",
            "04/29", "1234", 2400, new String[0]);
    Order invalidCvvNull = new Order("3", "2023-02-16", "4143510992401156",
            "04/29", null, 2400, new String[0]);
    Order invalidCvvEmpty = new Order("3", "2023-02-16", "4143510992401156",
            "04/29", "", 2400, new String[0]);
    Order validCreditCardDetails1 = new Order("4", "2023-02-17", "2720455119170273",
            "05/29", "123", 2400, new String[0]);

    Order validCreditCardDetails2 = new Order("45", "2023-03-20", "5279293980002467",
            "01/29", "568", 2400, new String[0]);

    @Test
    void testCreditCardNumberValidationLength() {
        Order order = invalidCardNoLength;
        OrderOutcome expected = OrderOutcome.InvalidCardNumber;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardNumberValidationValue() {
        Order order = invalidCardNoValue;
        OrderOutcome expected = OrderOutcome.InvalidCardNumber;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardNumberValidationNull() {
        Order order = invalidCardNoNull;
        OrderOutcome expected = OrderOutcome.InvalidCardNumber;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardNumberValidationNegative() {
        Order order = invalidCardNoNegative;
        OrderOutcome expected = OrderOutcome.InvalidCardNumber;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardExpiryValidation() {
        Order order = invalidExpiryDate;
        OrderOutcome expected = OrderOutcome.InvalidExpiryDate;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardExpiryValidationFormatValue() {
        Order order = invalidExpiryFormatValue;
        OrderOutcome expected = OrderOutcome.InvalidExpiryDate;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardExpiryValidationNull() {
        Order order = invalidExpiryNull;
        OrderOutcome expected = OrderOutcome.InvalidExpiryDate;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardExpiryValidationJustExpired() {
        Order order = invalidExpiryJustExpired;
        OrderOutcome expected = OrderOutcome.InvalidExpiryDate;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardExpiryValidationFormat() {
        Order order = invalidExpiryFormat;
        OrderOutcome expected = OrderOutcome.InvalidExpiryDate;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardCvvValidation() {
        Order order = invalidCvv;
        OrderOutcome expected = OrderOutcome.InvalidCvv;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardCvvValidationNull() {
        Order order = invalidCvvNull;
        OrderOutcome expected = OrderOutcome.InvalidCvv;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testCreditCardCvvValidationEmpty() {
        Order order = invalidCvvEmpty;
        OrderOutcome expected = OrderOutcome.InvalidCvv;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testValidCreditCard1() {
        Order order = validCreditCardDetails1;
        OrderOutcome expected = OrderOutcome.ValidButNotDelivered;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }

    @Test
    void testValidCreditCard2() {
        Order order = validCreditCardDetails2;
        OrderOutcome expected = OrderOutcome.ValidButNotDelivered;
        OrderOutcome actual = CreditCardValidation.validateCreditCard(order.getCreditCardNumber(),
                order.getCreditCardExpiry(), order.getCvv(), order.getOrderDate());
        assertEquals(expected, actual);
    }
}
