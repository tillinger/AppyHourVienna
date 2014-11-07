package is.ru.happyhour.db;

/**
 * Created by Peter on 06.11.2014.
 */
public class HappyHourTable {

    public static final String TABLE_HAPPYHOURS = "happyhours";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_BARNAME = "barname";

    //Address
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_POSTCODE = "postcode";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String COLUMN_STARTDATE = "start_date";
    public static final String COLUMN_ENDDATE = "end_date";
    public static final String COLUMN_STARTTIME = "start_time";
    public static final String COLUMN_ENDTIME = "end_time";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_PRICE = "price";

    public static final String COLUMN_DESCR_HAPPY = "descr_happy";
    public static final String COLUMN_DESCR_BAR = "descr_bar";

    public static String[] allHappyHourColumns = { COLUMN_ID, COLUMN_TYPE, COLUMN_BARNAME,
            COLUMN_ADDRESS, COLUMN_POSTCODE, COLUMN_LATITUDE, COLUMN_LONGITUDE,
            COLUMN_STARTDATE, COLUMN_ENDDATE, COLUMN_STARTTIME, COLUMN_ENDTIME, COLUMN_DAYS, COLUMN_PRICE,
            COLUMN_DESCR_HAPPY, COLUMN_DESCR_BAR};



    public static final String HAPPYHOUR_CREATE = "create table "
            + TABLE_HAPPYHOURS + "(" + COLUMN_ID + " integer primary key, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_BARNAME + " text not null, "

            + COLUMN_ADDRESS + " text not null, "
            + COLUMN_POSTCODE + " integer not null, "
            + COLUMN_LATITUDE + " real, "
            + COLUMN_LONGITUDE + " real, "

            + COLUMN_STARTDATE + " long not null, "
            + COLUMN_ENDDATE + " long not null, "
            + COLUMN_STARTTIME + " long not null, "
            + COLUMN_ENDTIME + " long not null, "
            + COLUMN_DAYS + " text not null, "
            + COLUMN_PRICE + " real not null, "

            + COLUMN_DESCR_HAPPY + " text, "
            + COLUMN_DESCR_BAR + " text);";
}
