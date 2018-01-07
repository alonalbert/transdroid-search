package org.transdroid.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Convert from popular date formats
 */
public class DateUploadedHelper {

  private static final Pattern DATE_PATTERN = Pattern.compile("^(\\d+) ([^\\d]+)((\\d+) (.*))?");

  public static Date convertFromWordTimeSpan(String text) {
    try {
      final Matcher matcher = DATE_PATTERN.matcher(text);
      if (!matcher.matches()) {
        return null;
      }
      final Calendar calendar = Calendar.getInstance();
      final int n1 = Integer.valueOf(matcher.group(1));
      final int u1 = parseUnit(matcher.group(2));
      //noinspection MagicConstant
      calendar.add(u1, -n1);

      final String g4 = matcher.group(4);
      if (g4 != null) {
        final int n2 = Integer.valueOf(g4);
        final int u2 = parseUnit(matcher.group(5));
        //noinspection MagicConstant
        calendar.add(u2, -n2);
      }
      return calendar.getTime();
    } catch (RuntimeException e) {
      return null;
    }
  }

  private static int parseUnit(String str) {
    if (str.startsWith("second")) {
      return Calendar.SECOND;
    }
    if (str.startsWith("minute")) {
      return Calendar.MINUTE;
    }
    if (str.startsWith("hour")) {
      return Calendar.HOUR;
    }
    if (str.startsWith("day")) {
      return Calendar.DAY_OF_MONTH;
    }
    if (str.startsWith("week")) {
      return Calendar.WEEK_OF_YEAR;
    }
    if (str.startsWith("month")) {
      return Calendar.MONTH;
    }
    if (str.startsWith("year")) {
      return Calendar.YEAR;
    }
    throw new RuntimeException("Invalid unit: " + str);
  }
}
