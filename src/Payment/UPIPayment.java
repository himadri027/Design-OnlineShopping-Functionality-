package Payment;

public class UPIPayment implements PaymentStratergy {
    @Override
    public boolean processPayment(double amount) {
        // Process credit card payment
        System.out.println("Processing  UPI payment for amount: " + amount);
        return true;
    }
}
