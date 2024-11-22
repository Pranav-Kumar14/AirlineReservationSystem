package airline; 

import airline.Flight;
import airline.InvalidDateFormatException;
import airline.GenericArray;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;


public class AirlineReservationSystemFX extends Application {

    public GenericArray<Flight> flights = new GenericArray<>(); 
    
    public void addFlight(Flight flight) {
        flights.add(flight); 
    }
    
    public Flight getFlight(int index) {
        return flights.get(index); 
    }


    public String invoiceDetails = "";
    public double userTotal = 0;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        initializeFlights();

        GridPane mainLayout = new GridPane();
        Scene mainScene = new Scene(mainLayout, 400, 300);
		mainLayout.setHgap(10);  mainLayout.setVgap(10);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Airline Reservation System");

        Label welcomeLabel = new Label("Welcome to Airline Reservation System");
        mainLayout.add(welcomeLabel, 2, 1, 2, 1);

        Button travellerButton = new Button("Traveller");
        Button managerButton = new Button("Manager");
        Button exitButton = new Button("Exit");

        mainLayout.add(travellerButton, 2, 2);
        mainLayout.add(managerButton, 3, 2);
        mainLayout.add(exitButton, 1, 11, 2, 1);

        travellerButton.setOnAction(e -> showTravellerMenu());
        managerButton.setOnAction(e -> showManagerMenu());
        exitButton.setOnAction(e -> primaryStage.close());

        primaryStage.show();
    }

    public void showTravellerMenu() {
        Stage travellerStage = new Stage();
        GridPane travellerLayout = new GridPane();
		travellerLayout.setHgap(10); travellerLayout.setVgap(10);
        Scene travellerScene = new Scene(travellerLayout, 400, 300);
        travellerStage.setScene(travellerScene);
        travellerStage.setTitle("Traveller Menu");

        Label nameLabel = new Label("Enter your name:");
        TextField nameField = new TextField();
        Button searchButton = new Button("Search Flights");
        Button printInvoiceButton = new Button("Print Invoice");
        Button logoutButton = new Button("Logout");

        travellerLayout.add(nameLabel, 1, 1);
        travellerLayout.add(nameField, 2, 1);
        travellerLayout.add(searchButton, 1, 2);
        travellerLayout.add(printInvoiceButton, 2, 2);
        travellerLayout.add(logoutButton, 1, 6, 2, 1);

        searchButton.setOnAction(e -> searchAndBookFlight(nameField.getText()));
        printInvoiceButton.setOnAction(e -> printInvoice(nameField.getText()));
        logoutButton.setOnAction(e -> { invoiceDetails = "";
        userTotal = 0.0;
        showAlert("You have successfully logged out.");
            travellerStage.close();});

        travellerStage.show();
    }

    public void showManagerMenu() {
        Stage managerStage = new Stage();
        GridPane managerLayout = new GridPane();
		managerLayout.setHgap(10); managerLayout.setVgap(10);
        Scene managerScene = new Scene(managerLayout, 400, 300);
        managerStage.setScene(managerScene);
        managerStage.setTitle("Manager Menu");

        Label passwordLabel = new Label("Enter Manager Password:");
        PasswordField passwordField = new PasswordField();
        Button authenticateButton = new Button("Authenticate");

        managerLayout.add(passwordLabel, 1, 1);
        managerLayout.add(passwordField, 2, 1);
        managerLayout.add(authenticateButton, 1, 3);

        authenticateButton.setOnAction(e -> {
            if (authenticate(passwordField.getText())) {
                showManagerOptions(managerStage);
            } else {
                showAlert("Authentication failed!");
            }
        });

        managerStage.show();
    }

    public void showManagerOptions(Stage managerStage) {
        GridPane managerOptionsLayout = new GridPane();
        Scene managerOptionsScene = new Scene(managerOptionsLayout, 400, 300);
		managerOptionsLayout.setVgap(10); managerOptionsLayout.setHgap(10);
        managerStage.setScene(managerOptionsScene);
        managerStage.setTitle("Manager Options");


        Button addFlightButton = new Button("Add Flight");
        Button updateFlightButton = new Button("Update Flight");
        Button generateReportButton = new Button("Generate Report");
        Button logoutButton = new Button("Logout");

        managerOptionsLayout.add(addFlightButton, 1, 1);
        managerOptionsLayout.add(updateFlightButton, 2, 1);
        managerOptionsLayout.add(generateReportButton, 3, 1);
        managerOptionsLayout.add(logoutButton, 1, 7);

        addFlightButton.setOnAction(e -> addFlight());
        updateFlightButton.setOnAction(e -> updateFlight());
        generateReportButton.setOnAction(e -> generateReport());
        logoutButton .setOnAction(e -> { showAlert("You have successfully logged out.");
            managerStage.close();});
    }
    public LocalDate getDate(String dateInput) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use yyyy-mm-dd.");
        }
    }
    
    public void searchAndBookFlight(String travellerName) {

        Stage searchFlightStage = new Stage();
        GridPane searchFlightLayout = new GridPane();
        searchFlightLayout.setVgap(10); searchFlightLayout.setHgap(10); 
        Scene searchFlightScene = new Scene(searchFlightLayout, 500, 450);
        searchFlightStage.setScene(searchFlightScene);
        searchFlightStage.setTitle("Search Flights");
    
        Label sourceLabel = new Label("Source:");
        TextField sourceField = new TextField();
        Label destinationLabel = new Label("Destination:");
        TextField destinationField = new TextField();
        Label departureDateLabel = new Label("Departure Date (yyyy-mm-dd):");
        TextField departureDateField = new TextField();
        Label ticketLabel = new Label("Tickets:");
        TextField ticketField = new TextField();
    
        Label cateringLabel = new Label("Catering Service(₹1000/tix):");
        RadioButton cateringYes = new RadioButton("Yes");
        RadioButton cateringNo = new RadioButton("No");
        ToggleGroup cateringGroup = new ToggleGroup();
        cateringYes.setToggleGroup(cateringGroup);
        cateringNo.setToggleGroup(cateringGroup);
        cateringNo.setSelected(true); 
    
        Button searchButton = new Button("Search");
        ListView<Flight> flightsListView = new ListView<>();
        Button bookButton = new Button("Book");
        Button backButton = new Button("Back");
        Label marginBottom = new Label("");
    
        searchFlightLayout.add(sourceLabel, 1, 1);
        searchFlightLayout.add(sourceField, 2, 1);
        searchFlightLayout.add(destinationLabel, 1, 2);
        searchFlightLayout.add(destinationField, 2, 2);
        searchFlightLayout.add(departureDateLabel, 1, 3);
        searchFlightLayout.add(departureDateField, 2, 3);
        searchFlightLayout.add(ticketLabel, 1, 4);
        searchFlightLayout.add(ticketField, 2, 4);
        searchFlightLayout.add(cateringLabel, 1, 5);
        searchFlightLayout.add(cateringYes, 2, 5);
        searchFlightLayout.add(cateringNo, 3, 5);
        searchFlightLayout.add(searchButton, 1, 7);
        searchFlightLayout.add(flightsListView, 1, 8, 3, 1);
        searchFlightLayout.add(bookButton, 1, 9, 1, 1);
        searchFlightLayout.add(backButton, 2, 9, 1, 1);
        searchFlightLayout.add(marginBottom, 1, 10);
    
        searchButton.setOnAction(e -> {
            String source = sourceField.getText();
            String destination = destinationField.getText();
            LocalDate departureDate = null;
            try{
                departureDate = getDate(departureDateField.getText());
            } catch(InvalidDateFormatException exc){
                showAlert(exc.getMessage());
            }
            flightsListView.getItems().setAll(searchFlights(source, destination, departureDate));
        });
        
        bookButton.setOnAction(e -> {
            Flight selectedFlight = flightsListView.getSelectionModel().getSelectedItem();
            int tickets = Integer.parseInt(ticketField.getText());
            boolean isCateringSelected = cateringYes.isSelected(); 
    
            if (selectedFlight != null ) {
                new Thread(() -> bookFlight(selectedFlight, travellerName, tickets, isCateringSelected)).start();
            } else {
                showAlert("Please select a flight to book.");
            }
        });
    
        backButton.setOnAction(e -> searchFlightStage.close());
    
        searchFlightStage.show();
    }
    

    public void printInvoice(String travellerName) {
        Stage invoiceStage = new Stage();
        GridPane invoiceLayout = new GridPane();
        invoiceLayout.setHgap(10); invoiceLayout.setVgap(10);
        Scene invoiceScene = new Scene(invoiceLayout, 400, 300);
        invoiceStage.setScene(invoiceScene);
        invoiceStage.setTitle("Invoice");

        Label invoiceLabel = new Label("Invoice for " + travellerName);
        TextArea invoiceTextArea = new TextArea(invoiceDetails);
        invoiceTextArea.setEditable(false);
        Label totalPricing = new Label("User Total : ₹"+ userTotal);
        Button backButton = new Button("Back");

        invoiceLayout.add(invoiceLabel, 1, 1, 2, 1);
        invoiceLayout.add(invoiceTextArea, 1, 2, 1, 1);
        invoiceLayout.add(totalPricing, 1, 3);
        invoiceLayout.add(backButton, 1, 4, 2, 1);

        backButton.setOnAction(e -> invoiceStage.close());

        invoiceStage.show();
    }

    public boolean authenticate(String password) {
        return "admin123".equals(password);
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    public void initializeFlights() {
        addFlight(new Flight("AI101", "Air India", "Delhi", "Mumbai", "Domestic", "2024-11-15", "2024-11-15", 150, 5000));
        addFlight(new Flight("AI102", "Air India", "Mumbai", "Delhi", "Domestic", "2024-11-16", "2024-11-16", 150, 5000));
        addFlight(new Flight("SG203", "SpiceJet", "Bangalore", "Chennai", "Domestic", "2024-12-05", "2024-12-05", 120, 4000));
        addFlight(new Flight("SG204", "SpiceJet", "Chennai", "Bangalore", "Domestic", "2024-12-06", "2024-12-06", 120, 4000));
        addFlight(new Flight("EK301", "Emirates", "Delhi", "Dubai", "International", "2024-12-20", "2024-12-20", 200, 15000));
        addFlight(new Flight("EK302", "Emirates", "Mumbai", "Dubai", "International", "2024-12-22", "2024-12-22", 200, 15000));
        addFlight(new Flight("BA401", "British Airways", "Mumbai", "London", "International", "2025-01-10", "2025-01-10", 250, 20000));
        addFlight(new Flight("BA402", "British Airways", "Delhi", "London", "International", "2025-01-12", "2025-01-12", 250, 20000));
        addFlight(new Flight("SQ501", "Singapore Airlines", "Chennai", "Singapore", "International", "2025-02-15", "2025-02-15", 220, 18000));
        addFlight(new Flight("SQ502", "Singapore Airlines", "Kolkata", "Singapore", "International", "2025-02-18", "2025-02-18", 220, 18000));
        addFlight(new Flight("AI103", "Air India", "Delhi", "Bangalore", "Domestic", "2024-11-18", "2024-11-18", 180, 5500));
        addFlight(new Flight("SG205", "SpiceJet", "Mumbai", "Kolkata", "Domestic", "2024-11-20", "2024-11-20", 170, 4800));
        addFlight(new Flight("VJ301", "Vistara", "Chennai", "Mumbai", "Domestic", "2024-11-25", "2024-11-25", 150, 4900));
        addFlight(new Flight("QF101", "Qatar Airways", "Mumbai", "Doha", "International", "2024-12-10", "2024-12-10", 200, 14000));
        addFlight(new Flight("UA102", "United Airlines", "Delhi", "New York", "International", "2024-12-14", "2024-12-14", 280, 45000));
        addFlight(new Flight("SG206", "SpiceJet", "Bangalore", "Kolkata", "Domestic", "2024-12-22", "2024-12-22", 120, 4100));
        addFlight(new Flight("AI104", "Air India", "Chennai", "Delhi", "Domestic", "2024-12-24", "2024-12-24", 150, 5000));
        addFlight(new Flight("QR401", "Qatar Airways", "Kolkata", "Doha", "International", "2025-01-05", "2025-01-05", 220, 15500));
        addFlight(new Flight("EK303", "Emirates", "Delhi", "Dubai", "International", "2025-01-15", "2025-01-15", 250, 16000));
        addFlight(new Flight("AI105", "Air India", "Delhi", "Bangalore", "Domestic", "2025-01-20", "2025-01-20", 180, 5500));
        addFlight(new Flight("SG207", "SpiceJet", "Mumbai", "Delhi", "Domestic", "2025-01-22", "2025-01-22", 150, 5100));
        addFlight(new Flight("BA403", "British Airways", "Mumbai", "London", "International", "2025-02-10", "2025-02-10", 230, 19500));
        addFlight(new Flight("SQ503", "Singapore Airlines", "Delhi", "Singapore", "International", "2025-02-18", "2025-02-18", 220, 18000));
        addFlight(new Flight("EK304", "Emirates", "Mumbai", "Dubai", "International", "2025-02-22", "2025-02-22", 200, 15000));
        addFlight(new Flight("AI106", "Air India", "Bangalore", "Mumbai", "Domestic", "2025-03-02", "2025-03-02", 170, 5000));
        addFlight(new Flight("AI107", "Air India", "Delhi", "Goa", "Domestic", "2025-03-05", "2025-03-05", 150, 5300));
        addFlight(new Flight("SG208", "SpiceJet", "Mumbai", "Bangalore", "Domestic", "2025-03-07", "2025-03-07", 150, 5100));
        addFlight(new Flight("UA103", "United Airlines", "Mumbai", "New York", "International", "2025-03-10", "2025-03-10", 270, 46000));
        addFlight(new Flight("QR402", "Qatar Airways", "Delhi", "Doha", "International", "2025-03-14", "2025-03-14", 210, 15500));
        addFlight(new Flight("AI108", "Air India", "Bangalore", "Kolkata", "Domestic", "2025-03-20", "2025-03-20", 160, 4900));
        addFlight(new Flight("AI109", "Air India", "Chennai", "Delhi", "Domestic", "2025-03-22", "2025-03-22", 150, 5000));
        addFlight(new Flight("EK305", "Emirates", "Delhi", "Dubai", "International", "2025-03-25", "2025-03-25", 230, 15500));
        addFlight(new Flight("AI110", "Air India", "Mumbai", "Delhi", "Domestic", "2025-03-30", "2025-03-30", 150, 5100));
        addFlight(new Flight("SG209", "SpiceJet", "Kolkata", "Chennai", "Domestic", "2025-04-05", "2025-04-05", 120, 4000));
        addFlight(new Flight("BA404", "British Airways", "Delhi", "London", "International", "2025-04-10", "2025-04-10", 260, 21000));
        addFlight(new Flight("SQ504", "Singapore Airlines", "Mumbai", "Singapore", "International", "2025-04-15", "2025-04-15", 220, 18000));
        addFlight(new Flight("AI111", "Air India", "Chennai", "Kolkata", "Domestic", "2025-04-18", "2025-04-18", 160, 4900));
        addFlight(new Flight("QR403", "Qatar Airways", "Bangalore", "Doha", "International", "2025-04-22", "2025-04-22", 220, 15000));
        addFlight(new Flight("AI112", "Air India", "Mumbai", "Chennai", "Domestic", "2025-04-24", "2025-04-24", 150, 5100));
        addFlight(new Flight("SG210", "SpiceJet", "Delhi", "Bangalore", "Domestic", "2025-04-27", "2025-04-27", 160, 4900));
        addFlight(new Flight("EK306", "Emirates", "Mumbai", "Dubai", "International", "2025-05-02", "2025-05-02", 210, 15000));
        addFlight(new Flight("AI113", "Air India", "Delhi", "Goa", "Domestic", "2025-05-05", "2025-05-05", 160, 5200));
        addFlight(new Flight("BA405", "British Airways", "Delhi", "London", "International", "2025-05-10", "2025-05-10", 250, 20000));
        addFlight(new Flight("SG211", "SpiceJet", "Bangalore", "Chennai", "Domestic", "2025-05-12", "2025-05-12", 120, 4100));
        addFlight(new Flight("QR404", "Qatar Airways", "Mumbai", "Doha", "International", "2025-05-15", "2025-05-15", 210, 15000));
        addFlight(new Flight("AI114", "Air India", "Kolkata", "Delhi", "Domestic", "2025-05-20", "2025-05-20", 180, 5500));
        addFlight(new Flight("SG505", "Singapore Airlines", "Chennai", "Singapore", "International", "2025-05-25", "2025-05-25", 220, 17500));
        addFlight(new Flight("AI115", "Air India", "Delhi", "Mumbai", "Domestic", "2025-05-28", "2025-05-28", 150, 5000));
        addFlight(new Flight("SG212", "SpiceJet", "Mumbai", "Delhi", "Domestic", "2025-06-02", "2025-06-02", 150, 4900));
        addFlight(new Flight("VJ302", "Vistara", "Delhi", "Bangalore", "Domestic", "2025-06-05", "2025-06-05", 160, 5100));
        addFlight(new Flight("AI116", "Air India", "Delhi", "Goa", "Domestic", "2025-06-10", "2025-06-10", 160, 5300));
        addFlight(new Flight("SQ506", "Singapore Airlines", "Kolkata", "Singapore", "International", "2025-06-15", "2025-06-15", 220, 18000));
        addFlight(new Flight("BA406", "British Airways", "Mumbai", "London", "International", "2025-06-20", "2025-06-20", 250, 20000));
        addFlight(new Flight("UA104", "United Airlines", "Delhi", "New York", "International", "2025-06-25", "2025-06-25", 270, 46000));
        addFlight(new Flight("QR405", "Qatar Airways", "Bangalore", "Doha", "International", "2025-06-30", "2025-06-30", 210, 15500));
        addFlight(new Flight("EK307", "Emirates", "Chennai", "Dubai", "International", "2025-07-05", "2025-07-05", 220, 15000));
        addFlight(new Flight("AI117", "Air India", "Mumbai", "Delhi", "Domestic", "2025-07-10", "2025-07-10", 150, 5000));
        addFlight(new Flight("SG213", "SpiceJet", "Delhi", "Bangalore", "Domestic", "2025-07-12", "2025-07-12", 160, 5100));
        addFlight(new Flight("VJ303", "Vistara", "Delhi", "Kolkata", "Domestic", "2025-07-15", "2025-07-15", 160, 4900));
        addFlight(new Flight("AI118", "Air India", "Chennai", "Mumbai", "Domestic", "2025-07-20", "2025-07-20", 150, 5200));
        addFlight(new Flight("BA407", "British Airways", "Delhi", "London", "International", "2025-07-25", "2025-07-25", 260, 21000));
        addFlight(new Flight("SQ507", "Singapore Airlines", "Mumbai", "Singapore", "International", "2025-07-30", "2025-07-30", 220, 18000));
        addFlight(new Flight("QR406", "Qatar Airways", "Delhi", "Doha", "International", "2025-08-02", "2025-08-02", 210, 15500));
        addFlight(new Flight("EK308", "Emirates", "Bangalore", "Dubai", "International", "2025-08-05", "2025-08-05", 230, 16000));
        addFlight(new Flight("AI119", "Air India", "Mumbai", "Chennai", "Domestic", "2025-08-10", "2025-08-10", 150, 5000));
        addFlight(new Flight("SG214", "SpiceJet", "Delhi", "Goa", "Domestic", "2025-08-15", "2025-08-15", 160, 5300));
        addFlight(new Flight("AI120", "Air India", "Kolkata", "Delhi", "Domestic", "2025-08-20", "2025-08-20", 180, 5500));
        addFlight(new Flight("SQ508", "Singapore Airlines", "Chennai", "Singapore", "International", "2025-08-25", "2025-08-25", 220, 17500));
        addFlight(new Flight("BA408", "British Airways", "Delhi", "London", "International", "2025-08-30", "2025-08-30", 250, 20000));
        addFlight(new Flight("QR407", "Qatar Airways", "Bangalore", "Doha", "International", "2025-09-05", "2025-09-05", 210, 15500));
        addFlight(new Flight("EK309", "Emirates", "Delhi", "Dubai", "International", "2025-09-10", "2025-09-10", 220, 15000));
        addFlight(new Flight("AI121", "Air India", "Delhi", "Mumbai", "Domestic", "2025-09-15", "2025-09-15", 150, 5000));
        addFlight(new Flight("SG215", "SpiceJet", "Mumbai", "Bangalore", "Domestic", "2025-09-20", "2025-09-20", 160, 5100));
        addFlight(new Flight("VJ304", "Vistara", "Delhi", "Chennai", "Domestic", "2025-09-25", "2025-09-25", 160, 4900));
        addFlight(new Flight("UA105", "United Airlines", "Mumbai", "New York", "International", "2025-09-30", "2025-09-30", 270, 46000));
        addFlight(new Flight("AI122", "Air India", "Chennai", "Delhi", "Domestic", "2025-10-05", "2025-10-05", 150, 5200));
        addFlight(new Flight("SG216", "SpiceJet", "Kolkata", "Chennai", "Domestic", "2025-10-10", "2025-10-10", 120, 4000));
        addFlight(new Flight("AI123", "Air India", "Delhi", "Mumbai", "Domestic", "2025-10-15", "2025-10-15", 150, 5000));
        addFlight(new Flight("BA409", "British Airways", "Mumbai", "London", "International", "2025-10-20", "2025-10-20", 250, 20000));
        addFlight(new Flight("SQ509", "Singapore Airlines", "Bangalore", "Singapore", "International", "2025-10-25", "2025-10-25", 220, 18000));
        addFlight(new Flight("AI124", "Air India", "Delhi", "Goa", "Domestic", "2025-10-30", "2025-10-30", 160, 5300));
        addFlight(new Flight("QR408", "Qatar Airways", "Mumbai", "Doha", "International", "2025-11-02", "2025-11-02", 210, 15000));
        addFlight(new Flight("EK310", "Emirates", "Delhi", "Dubai", "International", "2025-11-05", "2025-11-05", 220, 15000));
        addFlight(new Flight("AI125", "Air India", "Delhi", "Chennai", "Domestic", "2025-11-10", "2025-11-10", 150, 5200));
        addFlight(new Flight("SG217", "SpiceJet", "Mumbai", "Delhi", "Domestic", "2025-11-12", "2025-11-12", 150, 4900));
        addFlight(new Flight("AI126", "Air India", "Bangalore", "Delhi", "Domestic", "2025-11-15", "2025-11-15", 180, 5500));
        addFlight(new Flight("SQ510", "Singapore Airlines", "Delhi", "Singapore", "International", "2025-11-20", "2025-11-20", 220, 17500));

    }

    public Flight[] searchFlights(String source, String destination, LocalDate departureDate) {
        int matchCount = 0;
        int similarCount =0;
        for (Flight flight : flights) {
            if (flight != null 
                && flight.source.equalsIgnoreCase(source) 
                && flight.destination.equalsIgnoreCase(destination) ) {
                    if(flight.departureDate.equals(departureDate)){
                        matchCount++;
                    }
                    else{
                        similarCount++;
                    }
            }
        }
        
        Flight[] matchingFlights = new Flight[matchCount];
        Flight[] similarFlights = new Flight[similarCount];
        int mindex = 0;
        int sindex=0;
        for (Flight flight : flights) {
            if (flight != null 
                && flight.source.equalsIgnoreCase(source) 
                && flight.destination.equalsIgnoreCase(destination) 
                && flight.departureDate.equals(departureDate)) {
                matchingFlights[mindex++] = flight;
                return matchingFlights;
            }
        }
        for (Flight flight : flights) {
            if (flight != null 
                && flight.source.equalsIgnoreCase(source) 
                && flight.destination.equalsIgnoreCase(destination) ) {
                similarFlights[sindex++] = flight;
            }
        }
        
        return similarFlights;
    }
    
    

    public final Object invoiceLock = new Object();

    public void bookFlight(Flight flight, String travellerName, int ticketsToBook, boolean isCateringSelected) {
        LocalDate bookingDate = LocalDate.now();
        int cateringPrice = isCateringSelected ? 1000 : 0; 
        double finalPrice;

        synchronized (flight) {
            if (flight.seatsFilled + ticketsToBook <= flight.maxCapacity) {
                finalPrice = (calculateDynamicPrice(ticketsToBook, bookingDate, flight) * ticketsToBook) + (cateringPrice * ticketsToBook);
                flight.seatsFilled += ticketsToBook; 

                synchronized (invoiceLock) {
                    invoiceDetails += "Flight: " + flight.flightNo + ", Airline: " + flight.airline + ", Tickets: " + ticketsToBook + "\n";
                    invoiceDetails += "Source: " + flight.source + ", Destination: " + flight.destination+ ", Date: " + flight.departureDate+"\n";
                    invoiceDetails += "Catering: " + (isCateringSelected ? "Yes" : "No") + "\n";
                    invoiceDetails += "Flight Cost: ₹" + finalPrice + "\n";
                }

                synchronized (this) {
                    userTotal += finalPrice;
                }

                Platform.runLater(() -> showAlert("Flight booked successfully!"));
            } else {
                Platform.runLater(() -> showAlert("Not enough seats available for flight " + flight.flightNo + "."));
            }
        }
    }

    

    public double calculateDynamicPrice(int ticketsToBook, LocalDate bookingDate, Flight flight) {
        double price = flight.basePrice;
        long daysUntilDeparture = ChronoUnit.DAYS.between(bookingDate, flight.departureDate);

        if (daysUntilDeparture < 5) {
            price *= 1.3;
        } else if (daysUntilDeparture < 10) {
            price *= 1.2;
        } else if (daysUntilDeparture < 30) {
            price *= 1.1;
        }

        if (flight.seatsFilled > (flight.maxCapacity * 0.7)) {
            price *= 1.2;
        }

        return price;
    }

    public void addFlight() {
        Stage addFlightStage = new Stage();
        GridPane addFlightLayout = new GridPane();
        addFlightLayout.setHgap(10); addFlightLayout.setVgap(10);
        Scene addFlightScene = new Scene(addFlightLayout, 400, 400);
        addFlightStage.setScene(addFlightScene);
        addFlightStage.setTitle("Add Flight");

        Label flightNoLabel = new Label("Flight No:");
        TextField flightNoField = new TextField();
        Label airlineLabel = new Label("Airline:");
        TextField airlineField = new TextField();
        Label sourceLabel = new Label("Source:");
        TextField sourceField = new TextField();
        Label destinationLabel = new Label("Destination:");
        TextField destinationField = new TextField();
        Label flightTypeLabel = new Label("Flight Type:");
        TextField flightTypeField = new TextField();
        Label departureDateLabel = new Label("Departure Date (yyyy-mm-dd):");
        TextField departureDateField = new TextField();
        Label arrivalDateLabel = new Label("Arrival Date (yyyy-mm-dd):");
        TextField arrivalDateField = new TextField();
        Label maxCapacityLabel = new Label("Max Capacity:");
        TextField maxCapacityField = new TextField();
        Label basePriceLabel = new Label("Base Price:");
        TextField basePriceField = new TextField();
        Button addButton = new Button("Add");
        Button backButton = new Button("Back");

        addFlightLayout.add(flightNoLabel, 1, 1);
        addFlightLayout.add(flightNoField, 2, 1);
        addFlightLayout.add(airlineLabel, 1, 2);
        addFlightLayout.add(airlineField, 2, 2);
        addFlightLayout.add(sourceLabel, 1, 3);
        addFlightLayout.add(sourceField, 2, 3);
        addFlightLayout.add(destinationLabel, 1, 4);
        addFlightLayout.add(destinationField, 2, 4);
        addFlightLayout.add(flightTypeLabel, 1, 5);
        addFlightLayout.add(flightTypeField, 2, 5);
        addFlightLayout.add(departureDateLabel, 1, 6);
        addFlightLayout.add(departureDateField, 2, 6);
        addFlightLayout.add(arrivalDateLabel, 1, 7);
        addFlightLayout.add(arrivalDateField, 2, 7);
        addFlightLayout.add(maxCapacityLabel, 1, 8);
        addFlightLayout.add(maxCapacityField, 2, 8);
        addFlightLayout.add(basePriceLabel, 1, 9);
        addFlightLayout.add(basePriceField, 2 , 9);
        addFlightLayout.add(addButton , 1, 10);
        addFlightLayout.add(backButton, 2, 10);

        addButton.setOnAction(e -> {
            String flightNo = flightNoField.getText().toUpperCase();
            String airline = airlineField.getText().substring(0,1).toUpperCase() + airlineField.getText().substring(1,airlineField.getText().length()).toLowerCase();
            String source = sourceField.getText();
            String destination = destinationField.getText();
            String flightType = flightTypeField.getText().substring(0,1).toUpperCase() + flightTypeField.getText().substring(1,flightTypeField.getText().length()).toLowerCase();
            LocalDate departureDate = null;
            try{
                departureDate = getDate(departureDateField.getText());
            } catch(InvalidDateFormatException exc){
                showAlert(exc.getMessage());
            }
            LocalDate arrivalDate = null;
            try{
                arrivalDate = getDate(arrivalDateField.getText());
            } catch(InvalidDateFormatException exc){
                showAlert(exc.getMessage());
            }
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());
            double basePrice = Double.parseDouble(basePriceField.getText());

            addFlight(new Flight(flightNo, airline, source, destination, flightType, departureDate.toString(), arrivalDate.toString(), maxCapacity, basePrice));
            showAlert("Flight added successfully!");
    
        });

        backButton.setOnAction(e -> addFlightStage.close());

        addFlightStage.show();
    }

    public void updateFlight() {
        Stage updateFlightStage = new Stage();
        GridPane updateFlightLayout = new GridPane();
        updateFlightLayout.setHgap(10); updateFlightLayout.setVgap(10);
        Scene updateFlightScene = new Scene(updateFlightLayout, 400, 400);
        updateFlightStage.setScene(updateFlightScene);
        updateFlightStage.setTitle("Update Flight");

        Label flightNoLabel = new Label("Flight No:");
        TextField flightNoField = new TextField();
        Button searchButton = new Button("Search");
        Label airlineLabel = new Label("Airline:");
        TextField airlineField = new TextField();
        Label sourceLabel = new Label("Source:");
        TextField sourceField = new TextField();
        Label destinationLabel = new Label("Destination:");
        TextField destinationField = new TextField();
        Label departureDateLabel = new Label("Departure Date (yyyy-mm-dd):");
        TextField departureDateField = new TextField();
        Label arrivalDateLabel = new Label("Arrival Date (yyyy-mm-dd):");
        TextField arrivalDateField = new TextField();
        Label maxCapacityLabel = new Label("Max Capacity:");
        TextField maxCapacityField = new TextField();
        Label basePriceLabel = new Label("Base Price:");
        TextField basePriceField = new TextField();
        Button updateButton = new Button("Update");
        Button backButton = new Button("Back");

        updateFlightLayout.add(flightNoLabel, 1, 1);
        updateFlightLayout.add(flightNoField, 2, 1);
        updateFlightLayout.add(searchButton, 1, 2, 2, 1);
        updateFlightLayout.add(airlineLabel, 1, 3);
        updateFlightLayout.add(airlineField, 2, 3);
        updateFlightLayout.add(sourceLabel, 1, 4);
        updateFlightLayout.add(sourceField, 2, 4);
        updateFlightLayout.add(destinationLabel, 1, 5);
        updateFlightLayout.add(destinationField, 2, 5);
        updateFlightLayout.add(departureDateLabel, 1, 6);
        updateFlightLayout.add(departureDateField, 2, 6);
        updateFlightLayout.add(arrivalDateLabel, 1, 7);
        updateFlightLayout.add(arrivalDateField, 2, 7);
        updateFlightLayout.add(maxCapacityLabel, 1, 8);
        updateFlightLayout.add(maxCapacityField, 2, 8);
        updateFlightLayout.add(basePriceLabel, 1, 9);
        updateFlightLayout.add(basePriceField, 2, 9);
        updateFlightLayout.add(updateButton, 1, 10);
        updateFlightLayout.add(backButton, 2, 10);

        searchButton.setOnAction(e -> {
            String flightNo = flightNoField.getText().toUpperCase();
            for (Flight flight : flights) {
                if (flight != null && flight.flightNo.equals(flightNo)) {
                    airlineField.setText(flight.airline);
                    sourceField.setText(flight.source);
                    destinationField.setText(flight.destination);
                    departureDateField.setText(flight.departureDate.toString());
                    arrivalDateField.setText(flight.arrivalDate.toString());
                    maxCapacityField.setText(String.valueOf(flight.maxCapacity));
                    basePriceField.setText(String.valueOf(flight.basePrice));
                    return;
                }
            }

            showAlert("Flight not found.");
        });

        updateButton.setOnAction(e -> {
            String flightNo = flightNoField.getText().toUpperCase();
            boolean errorOccurred = false;
            for (Flight flight : flights) {
                if (flight != null && flight.flightNo.equals(flightNo)) {
                    flight.airline = airlineField.getText().substring(0,1).toUpperCase() + airlineField.getText().substring(1,airlineField.getText().length()).toLowerCase();;
                    flight.source = sourceField.getText();
                    flight.destination = destinationField.getText();
                    flight.departureDate = null;
                    try{
                        flight.departureDate = getDate(departureDateField.getText());
                    } catch(InvalidDateFormatException exc){
                        showAlert(exc.getMessage());
                        errorOccurred = true;
                    }
                    flight.arrivalDate = null;
                    try{
                        flight.arrivalDate = getDate(arrivalDateField.getText());
                    } catch(InvalidDateFormatException exc){
                        showAlert(exc.getMessage());
                        errorOccurred = true;
                    }
                    if(!errorOccurred){
                        flight.maxCapacity = Integer.parseInt(maxCapacityField.getText());
                        flight.basePrice = Double.parseDouble(basePriceField.getText());
                        showAlert("Flight updated successfully!");
                    }
                    return;
                }
            }

            showAlert("Flight not found.");
        });

        backButton.setOnAction(e -> updateFlightStage.close());

        updateFlightStage.show();
    }

    public void generateReport() {
		Stage reportStage = new Stage();
		GridPane reportLayout = new GridPane();
        reportLayout.setHgap(10); reportLayout.setVgap(10);
		Scene reportScene = new Scene(reportLayout, 400, 300);
		reportStage.setScene(reportScene);
		reportStage.setTitle("Report");
	
		Label reportLabel = new Label("Report:");
		TextArea reportTextArea = new TextArea();
        reportTextArea.setEditable(false);
		Button backButton = new Button("Back");
	
		reportLayout.add(reportLabel, 1, 1, 2, 1);
		reportLayout.add(reportTextArea, 1, 2, 1, 1);
		reportLayout.add(backButton, 1, 4, 2, 1);
	
		String report = generateReportContent();
		reportTextArea.setText(report);
	
		backButton.setOnAction(e -> reportStage.close());
	
		reportStage.show();
	}
	
	public String generateReportContent() {
        String report = "---Fully Booked Flights---\n";
        boolean anyFullyBooked = false;
        for (Flight flight : flights) {
            if (flight != null && flight.seatsFilled == flight.maxCapacity) {
                report += flight.flightNo + " (" + flight.airline + ") from " +
                          flight.source + " to " + flight.destination + " is fully booked.\n";
                anyFullyBooked = true;
            }
        }
    
        if (!anyFullyBooked) {
            report += "None of the flights are fully booked.\n";
        }
        
        report += "---Most Frequent Route---\n";
        String mostFrequentRoute = findMostFrequentRoute();
        report += mostFrequentRoute + "\n";
    
        report += "---Most Frequent Date---\n";
        LocalDate mostFrequentDate = findMostFrequentDate();
        report += (mostFrequentDate != null ? mostFrequentDate.toString() : "No data") + "\n";
        
        report += "---Most Frequent Destination ---\n";
        String mostFrequentDestination = findMostFrequentDestination();
        report += mostFrequentDestination + "\n";
        
        return report;
    }
    
	
	public String findMostFrequentRoute() {
		String mostFrequentRoute = "";
		int maxCount = 0;
	
		for (Flight flight : flights) {
			if (flight != null) {
				int count = 0;
				for (Flight innerFlight : flights) {
					if (innerFlight != null && flight.source.equals(innerFlight.source) && flight.destination.equals(innerFlight.destination)) {
						count++;
					}
				}
				if (count > maxCount) {
					maxCount = count;
					mostFrequentRoute = flight.source + " to " + flight.destination;
				}
			}
		}
		return mostFrequentRoute;
	}
	
	public LocalDate findMostFrequentDate() {
		LocalDate mostFrequentDate = null;
		int maxCount = 0;
	
		for (Flight flight : flights) {
			if (flight != null) {
				int count = 0;
				for (Flight innerFlight : flights) {
					if (innerFlight != null && flight.departureDate.equals(innerFlight.departureDate)) {
						count++;
					}
				}
				if (count > maxCount) {
					maxCount = count;
					mostFrequentDate = flight.departureDate;
				}
			}
		}
		return mostFrequentDate;
	}
	
	public String findMostFrequentDestination() {
		String mostFrequentDestination = "";
		int maxCount = 0;
	
		for (Flight flight : flights) {
			if (flight != null) {
				int count = 0;
				for (Flight innerFlight : flights) {
					if (innerFlight != null && flight.destination.equals(innerFlight.destination)) {
						count++;
					}
				}
				if (count > maxCount) {
					maxCount = count;
					mostFrequentDestination = flight.destination;
				}
			}
		}
		return mostFrequentDestination;
	}

}
