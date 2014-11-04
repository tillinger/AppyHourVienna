package is.ru.happyhour.model;

import java.io.Serializable;
import java.util.EnumSet;

public class HappyHour implements Serializable {

    private long id;

    private HappyHourType type;
    private String name;
    private Address address;

    private long startDate; //date in millis when this happy hours starts and ends
    private long endDate;

    private long startTime; //time seconds of that day when the happy hour starts (i.e. 3600 if it starts at 01:00)
    private long endTime;
    private EnumSet<DayOfWeek> daysOfWeek;

    private double price;

    private String descriptionHappy;
    private String descriptionBar;

    public HappyHour() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public HappyHourType getType() {
        return type;
    }
    public void setType(HappyHourType type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    //dates
    public long getStartDate() {
        return startDate;
    }
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }
    public long getEndDate() {
        return endDate;
    }
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    //times
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public String getTimeString() {
        return getNumberTwoDigit(this.startTime/3600) + ":" + getNumberTwoDigit((this.startTime%3600)/60) + " - " +
               getNumberTwoDigit(this.endTime/3600) + ":" + getNumberTwoDigit((this.endTime%3600)/60);
    }
    private String getNumberTwoDigit(long number) {
        return (number >= 10) ? "" + number : "0" + number;
    }

    //days
    public EnumSet<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }
    public void setDaysOfWeek(EnumSet<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
    public String getDaysAsString() {
        String result = "";
        for (DayOfWeek dayOfWeek : daysOfWeek) {
            result += dayOfWeek.name();
            result += " ";
        }
        return result;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescriptionHappy() {
        return descriptionHappy;
    }
    public void setDescriptionHappy(String descriptionHappy) {
        this.descriptionHappy = descriptionHappy;
    }

    public String getDescriptionBar() {
        return descriptionBar;
    }
    public void setDescriptionBar(String descriptionBar) {
        this.descriptionBar = descriptionBar;
    }
}
