package src;

public class Order {
    public int item_id;
    public int quantity;
    public double unitPrice;
    public double totalPrice;
    public String backorderstatus;

    public Order(int item_id, int quantity, double unitPrice, double totalPrice, String backorderstatus) {
        this.item_id = item_id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.backorderstatus = backorderstatus;
    }
}
