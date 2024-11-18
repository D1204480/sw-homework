package org.remast.baralga;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.remast.text.TimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/** Utility class for formatting. */
public enum FormatUtils {

    INSTANCE;

    /** Hide constructor in utility class. */
    private FormatUtils() { }

    // ------------------------------------------------
    // Date Formats
    // ------------------------------------------------
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern(TimeFormat.HHMM_FORMAT);

    /** Format for one in report. */
	public final DateFormat DAY_FORMAT = new SimpleDateFormat(DateTimeFormat.patternForStyle("S-", Locale.getDefault()) + " EE");

    /**
     * Format the given date as time.
     * @param date the date to format
     * @return the formatted time
     */
    public static String formatTime(final DateTime date) {
    	if (date == null) {
    		return null;
    	}
    	
        return TIME_FORMATTER.print(date);
    }
    
    /**
     * Format the given date as date.
     * @param date the date to format
     * @return the formatted date
     */
    public static String formatDate(final DateTime date) {
    	if (date == null) {
    		return null;
    	}
    	
        return DateFormat.getDateInstance().format(date.toDate());
    }
    
    /**
     * Format the given date as day.
     * @param date the date to format
     * @return the formatted date
     */
    public String formatDay(final DateTime date) {
    	if (date == null) {
    		return null;
    	}
    	
        return DAY_FORMAT.format(date.toDate());
    }

}
