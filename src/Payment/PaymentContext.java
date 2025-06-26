package Payment;

public class PaymentContext {
    private PaymentStratergy strategy;

    public void setStrategy(PaymentStratergy strategy) {
        this.strategy = strategy;
    }


    public boolean  executePayment(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("No payment strategy set");
        }

        boolean result = strategy.processPayment(amount);
        return  result ;

    }
}