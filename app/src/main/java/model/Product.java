package model;

// This class represents what an Item will look like
// TODO: Probably can add more fields like images
// For now will just be the name, price and url
public class Product {

    private String name;
    private String price;
    private String url;

    // Must have an empty constructor for Firestore to work
    public Product(){}

    public Product(String name, String price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
