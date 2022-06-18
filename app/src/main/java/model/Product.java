package model;

// This class represents what an Item will look like
// TODO: Probably can add more fields like images
// For now will just be the name, price and url
public class Product {

    private String name;
    private String price;
    private String url;

    // Subsequent fields is to get hold of the user details if user wants to add
    private String userId;
    private String userName;
    private String userEmail;


    // Must have an empty constructor for Firestore to work
    public Product(){}

    // This constructor is just to show the item info
    public Product(String name, String price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
    }

    // This constructor is to be used when user adds an item to be tracked
    public Product(String name, String price, String url, String userId, String userName, String userEmail) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    // Getters and Setters


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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
