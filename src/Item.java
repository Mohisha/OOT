package src;

public class Item {
    private int itemId;
    private String name;
    private String description;
    private double unitPrice;
    
    public Item(int itemId, String name, String description, double unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    @Override
    public String toString() {
        return name + " - Rs " + unitPrice;
    }
}
