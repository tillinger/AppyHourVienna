package is.ru.happyhour.db;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.EnumSet;
        import java.util.List;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import is.ru.happyhour.HappyListFragment;
        import is.ru.happyhour.model.Address;
        import is.ru.happyhour.model.DayOfWeek;
        import is.ru.happyhour.model.HappyHour;
        import is.ru.happyhour.model.HappyHourType;

public class HappyDataSource {

    // Database fields
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public HappyDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private void insertHappyHour (HappyHour happyHour) {
        ContentValues values = new ContentValues();
        values.put(HappyHourTable.COLUMN_ID, happyHour.getId());
        values.put(HappyHourTable.COLUMN_TYPE, happyHour.getType().name());
        values.put(HappyHourTable.COLUMN_BARNAME, happyHour.getBarName());
        //Address
        values.put(HappyHourTable.COLUMN_ADDRESS, happyHour.getAddress().getAddress());
        values.put(HappyHourTable.COLUMN_POSTCODE, happyHour.getAddress().getPostcode());
        values.put(HappyHourTable.COLUMN_LATITUDE, happyHour.getAddress().getLatitude());
        values.put(HappyHourTable.COLUMN_LONGITUDE, happyHour.getAddress().getLongitude());

        values.put(HappyHourTable.COLUMN_STARTDATE, happyHour.getStartDate());
        values.put(HappyHourTable.COLUMN_ENDDATE, happyHour.getEndDate());
        values.put(HappyHourTable.COLUMN_STARTTIME, happyHour.getStartTime());
        values.put(HappyHourTable.COLUMN_ENDTIME, happyHour.getEndTime());
        values.put(HappyHourTable.COLUMN_DAYS, calcDaysString(happyHour.getDaysOfWeek()));
        values.put(HappyHourTable.COLUMN_PRICE, happyHour.getPrice());

        values.put(HappyHourTable.COLUMN_DESCR_HAPPY, happyHour.getDescriptionHappy());
        values.put(HappyHourTable.COLUMN_DESCR_BAR, happyHour.getDescriptionBar());

        String alreadyInserted = "SELECT " + HappyHourTable.COLUMN_ID + " FROM " +
                HappyHourTable.TABLE_HAPPYHOURS + " WHERE " + HappyHourTable.COLUMN_ID + "=?";
        Cursor c = db.rawQuery(alreadyInserted, new String[]{happyHour.getId() + ""});
        long value;
        if (c.moveToFirst()) {
            db.update(HappyHourTable.TABLE_HAPPYHOURS, values, HappyHourTable.COLUMN_ID + "='" + happyHour.getId() + "'", null);
            System.out.println("updated: " + happyHour.getId());
        } else {
            db.insert(HappyHourTable.TABLE_HAPPYHOURS, null, values);
            System.out.println("inserted: " + happyHour.getId());
        }

    }

    private String calcDaysString(EnumSet<DayOfWeek> days) {
        String result = "";
        for (DayOfWeek day : DayOfWeek.values()) {
            if(days.contains(day)) {
                result += "1";
            } else {
                result += "0";
            }
        }
        System.out.println(result);
        return result;
    }

    public void insertHappyHours (List<HappyHour> happyHours) {
        this.open();
        for (HappyHour happyHour : happyHours) {
            this.insertHappyHour(happyHour);
        }
        this.close();
    }

    public ArrayList<HappyHour> getHappiesFilter(int nowOrAll, int orderBy) {
        this.open();
        ArrayList<HappyHour> happies = new ArrayList<HappyHour>();

        //noworall filter
        String whereString = "";
        if(nowOrAll == HappyListFragment.POSITION_NOW) {
            long secondsOfDay = getSecondsOfDay();
            whereString = HappyHourTable.COLUMN_STARTTIME + " < " + secondsOfDay + " AND " +
                    HappyHourTable.COLUMN_ENDTIME + " > " + secondsOfDay;

            Calendar calendar = Calendar.getInstance(); //get the character of the current day and check if it is '1'
            whereString += " AND substr(" + HappyHourTable.COLUMN_DAYS + "," +
                    (calendar.get(Calendar.DAY_OF_WEEK)) + ",1) LIKE '1'" ;
        }

        //ordering filter
        String orderString = "";
        if(orderBy == HappyListFragment.ORDERBYPRICE) {
            orderString = HappyHourTable.COLUMN_PRICE + " ASC";
        } else if (orderBy == HappyListFragment.ORDERBYPRICE) {

        }

        Cursor cursor = db.query(HappyHourTable.TABLE_HAPPYHOURS,
                HappyHourTable.allHappyHourColumns, whereString, null, null, null, orderString);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HappyHour happy = cursorToHappyHour(cursor);
            happies.add(happy);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        this.close();

        return happies;
    }

    private long getSecondsOfDay() {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return  (now - c.getTimeInMillis()) / 1000;
    }

    private HappyHour cursorToHappyHour(Cursor cursor) {
        HappyHour happy = new HappyHour();
        happy.setId(cursor.getLong(0));
        happy.setType(HappyHourType.valueOf(cursor.getString(1)));
        happy.setBarName(cursor.getString(2));

        //Address
        Address address = new Address();
        address.setAddress(cursor.getString(3));
        address.setPostcode(cursor.getInt(4));
        address.setLatitude(cursor.getDouble(5));
        address.setLongitude(cursor.getDouble(6));
        happy.setAddress(address);

        happy.setStartDate(cursor.getLong(7));
        happy.setEndDate(cursor.getLong(8));
        happy.setStartTime(cursor.getLong(9));
        happy.setEndTime(cursor.getLong(10));
        happy.setDaysOfWeek(calcDaysEnumSet(cursor.getString(11)));
        happy.setPrice(cursor.getDouble(12));

        happy.setDescriptionHappy(cursor.getString(13));
        happy.setDescriptionBar(cursor.getString(14));

        return happy;
    }

    private EnumSet<DayOfWeek> calcDaysEnumSet(String dayString) {
        EnumSet<DayOfWeek> daysOfWeek = EnumSet.noneOf(DayOfWeek.class);

        DayOfWeek[] allDays = DayOfWeek.values();
        for (int i = 0; i < allDays.length; i++) {
            if(dayString.charAt(i) == '1') {
                daysOfWeek.add(allDays[i]);
            }
        }

        return daysOfWeek;
    }
}
