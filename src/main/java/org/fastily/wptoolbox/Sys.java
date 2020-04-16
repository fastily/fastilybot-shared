package org.fastily.wptoolbox;

/**
 * System properties and static error handling methods.
 * 
 * @author Fastily
 * 
 */
public final class Sys
{
	/**
	 * All static methods, no constructors allowed.
	 */
	private Sys()
	{

	}

	/**
	 * Prints an error message to std err and exits with status 1.
	 * 
	 * @param s Error message to print
	 */
	public static void errAndExit(String s)
	{
		if (s != null)
			System.err.println(s);
		System.exit(1);
	}

	/**
	 * Prints stack trace from specified error and exit.
	 * 
	 * @param e The error object.
	 * @param s Additional error message. Disable with null.
	 */
	public static void errAndExit(Throwable e, String s)
	{
		e.printStackTrace();
		errAndExit(s);
	}
}