package com.my_campus.mycampus_application.ui.home;

public class HomePostingModel {
    private String itemName;
    private String category;
    private String description;
    private String price;
    private String quantity;
    private String thumbnailUrl;

    public HomePostingModel() {
        // Must be empty! Don't remove for Firebase.
    }

    public HomePostingModel(String itemName, String category, String description,
                            String price, String quantity, String thumbnailUrl) {

        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailUrl = thumbnailUrl;
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

    public String getThumbnailUrl() { return thumbnailUrl; }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
