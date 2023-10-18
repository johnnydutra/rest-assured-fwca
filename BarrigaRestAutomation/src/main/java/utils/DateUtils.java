package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getDateWithDaysDifference(Integer days) {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DAY_OF_MONTH, days);
        return getFormattedDate(calender.getTime());
    }

    public static String getFormattedDate(Date date) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
}
