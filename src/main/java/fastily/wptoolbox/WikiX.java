package fastily.wptoolbox;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fastily.jwiki.core.MQuery;
import fastily.jwiki.core.NS;
import fastily.jwiki.core.Wiki;
import fastily.jwiki.util.FL;
import fastily.jwiki.util.Tuple;

/**
 * Useful extensions to jwiki's Wiki
 * 
 * @author Fastily
 *
 */
public class WikiX
{
	/**
	 * First pass regex matching redirects in text. This extracts the redirect as a link, and there may be leading
	 * spaces.
	 */
	private static Pattern firstPassRedirectRegex = Pattern.compile("(?i)(?<=\\#REDIRECT)\\s*\\[\\[.+?\\]\\]");

	/**
	 * Constructors disallowed
	 */
	private WikiX()
	{

	}

	/**
	 * Gets the specified WikiGen user at en.wikipedia.org.
	 * 
	 * @param user The user to get a Wiki object for
	 * @return A Wiki object, or null on error
	 */
	public static Wiki getUserWP(String user)
	{
		return WGen.get(user, "en.wikipedia.org");
	}

	/**
	 * Derives a Wiki from {@code wiki} with the domain set to {@code commons.wikimedia.org}.
	 * 
	 * @param wiki The Wiki object to derive a new Commons Wiki from.
	 * @return A Wiki pointing to Commons, or null on error.
	 */
	public static Wiki getCommons(Wiki wiki)
	{
		return wiki.getWiki("commons.wikimedia.org");
	}

	/**
	 * Determine if a set of link(s) has existed on a page over a given time period.
	 * 
	 * @param wiki The Wiki object to use
	 * @param title The title to query
	 * @param l The list of link(s) to look for in the history of {@code title}.
	 * @param start The time to start looking at (inclusive). Optional - set null to disable.
	 * @param end The time to stop the search at (exclusive). Optional - set null to disable.
	 * @return A list of link(s) that were found at some point in the page's history.
	 */
	public static ArrayList<String> detLinksInHist(Wiki wiki, String title, ArrayList<String> l, Instant start, Instant end)
	{
		ArrayList<String> texts = FL.toAL(wiki.getRevisions(title, -1, false, start, end).stream().map(r -> r.text));
		return FL.toAL(l.stream().filter(s -> texts.stream().noneMatch(t -> t.matches("(?si).*?\\[\\[:??(\\Q" + s + "\\E)\\]\\].*?"))));
	}

	/**
	 * Recursively searches a category for members.
	 * 
	 * @param wiki The Wiki object to use
	 * @param root The root/parent category to start searching in
	 * @return A Tuple in the form: ( categories visited, members found )
	 */
	public static Tuple<HashSet<String>, HashSet<String>> getCategoryMembersR(Wiki wiki, String root)
	{
		HashSet<String> seen = new HashSet<>(), l = new HashSet<>();
		getCategoryMembersR(wiki, root, seen, l);

		return new Tuple<>(seen, l);
	}

	/**
	 * Recursively searches a category for members.
	 * 
	 * @param wiki The Wiki object to use
	 * @param root The root/parent category to start searching in
	 * @param seen Lists the categories visited. Tracking this avoids circular self-categorizing categories.
	 * @param l Lists the category members encountered.
	 */
	private static void getCategoryMembersR(Wiki wiki, String root, HashSet<String> seen, HashSet<String> l)
	{
		seen.add(root);

		ArrayList<String> results = wiki.getCategoryMembers(root);
		ArrayList<String> cats = wiki.filterByNS(results, NS.CATEGORY);

		results.removeAll(cats); // cats go in seen
		l.addAll(results);

		for (String s : cats)
		{
			if (seen.contains(s))
				continue;

			getCategoryMembersR(wiki, s, seen, l);
		}
	}

	/**
	 * Extracts a template from text.
	 * 
	 * @param p The template's Regex
	 * @param text The text to look for {@code p} in
	 * @return The template, or the empty string if nothing was found.
	 */
	public static String extractTemplate(Pattern p, String text) // TODO: Bad form, fix me.
	{
		Matcher m = p.matcher(text);
		return m.find() ? m.group() : "";
	}

	/**
	 * Gets the first shared (non-local) duplicate for each file with a duplicate. Filters out files which do not have
	 * duplicates.
	 * 
	 * @param wiki The Wiki object to use
	 * @param titles The titles to get duplicates for
	 * @return A Map where each key is the original, and each value is the first duplicate found.
	 */
	public static HashMap<String, String> getFirstOnlySharedDuplicate(Wiki wiki, Collection<String> titles)
	{
		HashMap<String, String> l = new HashMap<>();
		MQuery.getSharedDuplicatesOf(wiki, titles).forEach((k, v) -> {
			if (!v.isEmpty())
				l.put(k, v.get(0));
		});

		return l;
	}

	/**
	 * Extracts the target of a redirect from wikitext.
	 * 
	 * @param s The wikitext to look for a redirect target.
	 * @return The target of the redirect, or null if no redirect incantation was found.
	 */
	public static String extractRedirectTarget(String s)
	{
		Matcher m = firstPassRedirectRegex.matcher(s);
		return m.find() ? m.group().replaceAll("(^\\s*\\[\\[|\\]\\])", "").trim() : null;
	}
}