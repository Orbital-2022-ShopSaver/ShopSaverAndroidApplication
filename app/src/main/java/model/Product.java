package model;

// This class represents what an Item will look like
// TODO: Probably can add more fields like images
// For now will just be the name, price and url
public class Product {

    private String name;
    private double price;
    private String url;
    private String platform;
    private String image;
    private double priceExpectation;

    // Subsequent fields is to get hold of the user details if user wants to add
    private String userId;
    private String userName;
    private String userEmail;

    // Document ID from the Firebase
    private String documentId;


    // Must have an empty constructor for Firestore to work
    public Product(){}


    // This constructor is just to show the item info
    public Product(String name, double price, String url, String platform, String image) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.platform = platform;
        this.image = image;
    }

    // This constructor is to be used when user adds an item to be tracked
    public Product(String name, double price, String url, String platform, String image,
                   String userId, String userName, String userEmail, double priceExpectation) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.platform = platform;
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.priceExpectation = priceExpectation;
    }

    // This constructor is to be used when item tracked fetch from database
    // It will aid in deletion

    public Product(String name, double price, String url, String platform,
                   String image, double priceExpectation, String userId,
                   String userName, String userEmail, String documentId) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.platform = platform;
        this.image = image;
        this.priceExpectation = priceExpectation;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.documentId = documentId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPriceExpectation() {
        return priceExpectation;
    }

    public void setPriceExpectation(double priceExpectation) {
        this.priceExpectation = priceExpectation;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
