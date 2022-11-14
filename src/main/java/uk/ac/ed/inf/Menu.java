package uk.ac.ed.inf;

/**
 * Represents one menu entry of a restaurant.
 * @param name pizza name.
 * @param priceInPence price of this pizza in pence.
 */
public record Menu(String name, int priceInPence) {}
