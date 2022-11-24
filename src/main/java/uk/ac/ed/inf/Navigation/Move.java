package uk.ac.ed.inf.Navigation;

public record Move(String orderNo, double fromLongitude,
                   double fromLatitude, Double angle,
                   double toLongitude, double toLatitude,
                   int ticksSinceStartOfCalculation) {
}
