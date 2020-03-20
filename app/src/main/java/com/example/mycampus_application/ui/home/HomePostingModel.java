package com.example.mycampus_application.ui.home;

public class HomePostingModel {
    private String itemName;
    private String category;
    private String description;
    private String price;
    private String quantity;

    public HomePostingModel() {
        // Must be empty! Don't remove for Firebase.
    }

    public HomePostingModel(String itemName, String category, String description,
                            String price, String quantity) {

        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

}
