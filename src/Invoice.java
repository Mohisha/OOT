package src;
import java.util.List;
import java.util.ArrayList;

public class Invoice {
    
    private List<Order> orders; 
    private double totalAmount;
    private double transportCharge;
    private String transport_preference;

    public Invoice() {
        this.orders = new ArrayList<>();
        this.totalAmount = 0.0;
        this.transportCharge = 0.0;
    }

    public void AddOrder(Order order) {
        orders.add(order);
        totalAmount += order.totalPrice;
    }

    public void AddTransportCharge(double transport_charge){
        transportCharge = transport_charge;
        totalAmount += transport_charge;
    }

    public void AddTransportPreference(String transport){
        transport_preference = transport;
    }

    public double get_invoice_total(){
        return totalAmount;
    }

    public double get_transport_charge(){
        return transportCharge;
    }

    public double get_order_total(){
        return totalAmount - transportCharge;
    }

    public String get_transport_preference(){
        return transport_preference;
    }

    public List<Order> get_orders(){
        return orders;
    }

    public void displayInvoice() {
        System.out.println("Orders:");
        for (Order order : orders) {
            System.out.println(order);
        }
        System.out.println("Total Amount: Rs " + totalAmount);
    }
}

