package airline;
import java.time.LocalDate;

public class Flight {
    String flightNo;
    String airline;
    String source;
    String destination;
    String type;
    LocalDate departureDate;
    LocalDate arrivalDate;
    int maxCapacity;
    double basePrice;
    int seatsFilled;

    public Flight(String flightNo, String airline, String source, String destination, String type, String departureDate, String arrivalDate, int maxCapacity, double basePrice) {
        this.flightNo = flightNo;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.departureDate = LocalDate.parse(departureDate);
        this.arrivalDate = LocalDate.parse(arrivalDate);
        this.maxCapacity = maxCapacity;
        this.basePrice = basePrice;
        this.seatsFilled = 0;
    }

    public String toString() {
        return "Flight: " + flightNo + " | Airline: " + airline + " | Date: " + departureDate +  " | Price: ₹" + basePrice + " | Type : " + type;
    }
}