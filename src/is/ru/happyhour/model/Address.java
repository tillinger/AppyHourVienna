package is.ru.happyhour.model;

import java.io.Serializable;

public class Address implements Serializable {

    private String address;
    private int postcode;
    private double latitude;
    private double longitude;

    public Address(String address, int postcode) {
        this.address = address;
        this.postcode = postcode;
    }

    public Address(String address, int postcode, double latitude, double longitude) {
        this(address, postcode);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostcode() {
        return postcode;
    }
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddressWithPostCode() {
        return this.postcode + ", " + this.address;
    }
}
