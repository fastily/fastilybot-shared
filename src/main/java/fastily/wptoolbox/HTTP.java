package fastily.wptoolbox;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HTTP
{
	/**
	 * Generic http client for miscellaneous use.
	 */
	private static OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).build();

	/**
	 * Constructors disallowed
	 */
	private HTTP()
	{
		
	}

	/**
	 * Performs a GET request on the specified URL and returns the response body as a String. Returns null if something
	 * went wrong.
	 * 
	 * @param url The URL to send a GET request to
	 * @return The response body as a String
	 */
	public static String get(String url)
	{
		return get(HttpUrl.parse(url));
	}

	/**
	 * Performs a GET request on the specified HttpUrl and returns the response body as a String. Returns null if
	 * something went wrong.
	 * 
	 * @param url The HttpUrl to send a GET request to
	 * @return The response body as a String
	 */
	public static String get(HttpUrl url)
	{
		try
		{
			return HTTP.httpClient.newCall(new Request.Builder().url(url).get().build()).execute().body().string();
		}
		catch (Throwable e)
		{
			return null;
		}
	}
}