package org.fastily.wptoolbox;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Date and time utility functions.
 * 
 * @author Fastily
 *
 */
public class Dates
{
	/**
	 * A date formatter for UTC times.
	 */
	public static final DateTimeFormatter iso8601dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Formats dates as as day month year
	 * 
	 * @see #dateAsDMY(TemporalAccessor)
	 */
	public static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("d MMMM yyyy");

	/**
	 * Matches a date of the form dd-mmmm-yyyy.
	 */
	public static final String DMYRegex = "\\d{1,2}? (January|February|March|April|May|June|July|August|September|October|November|December) \\d{4}?";

	/**
	 * Formats dates as as year month day
	 * 
	 * @see #dateAsYMD(TemporalAccessor)
	 */
	public static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy MMMM d");

	/**
	 * Constructors disallowed
	 */
	private Dates()
	{

	}

	/**
	 * Generates a ZonedDateTime of the current date and time.
	 * 
	 * @return The current date and time, in UTC.
	 */
	public static ZonedDateTime getUTCofNow()
	{
		return ZonedDateTime.now(ZoneOffset.UTC);
	}

	/**
	 * Formats a TemporalAccessor as a year-month-date. ex: {@code 2017 February 6}
	 * 
	 * @param d The TemporalAccessor to format.
	 * @return A String derived from the TemporalAccessor in YMD format.
	 */
	public static String dateAsYMD(TemporalAccessor d)
	{
		return YMD.format(d);
	}

	/**
	 * Formats a TemporalAccessor as a year-month-date. ex: {@code 6 February 2017}
	 * 
	 * @param d The TemporalAccessor to format.
	 * @return A String derived from the TemporalAccessor in YMD format.
	 */
	public static String dateAsDMY(TemporalAccessor d)
	{
		return DMY.format(d);
	}
}