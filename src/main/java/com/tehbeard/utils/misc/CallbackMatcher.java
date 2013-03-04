package com.tehbeard.utils.misc;
import java.util.regex.*;

public class CallbackMatcher
{
    public static interface Callback
    {
        public String foundMatch(MatchResult matchResult);
    }

    private final Pattern pattern;

    public CallbackMatcher(String regex)
    {
        this.pattern = Pattern.compile(regex);
    }

    public String replaceMatches(String string, Callback callback)
    {
        final Matcher matcher = this.pattern.matcher(string);
        while(matcher.find())
        {
            final MatchResult matchResult = matcher.toMatchResult();
            final String replacement = callback.foundMatch(matchResult);
            string = string.substring(0, matchResult.start()) +
                     replacement + string.substring(matchResult.end());
            matcher.reset(string);
        }
		return string;
    }
}