package org.chodavarapu.datamill.http.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ravi Chodavarapu (rchodava@gmail.com)
 */
public class UriTemplateRegion {
    private final String variable;
    private final String content;
    private final Pattern pattern;

    public UriTemplateRegion(String content) {
        this.variable = null;
        this.content = content;
        this.pattern = null;
    }

    public UriTemplateRegion(String variable, String content) {
        this.variable = variable;
        this.content = content;

        if (content != null) {
            this.pattern = Pattern.compile(content);
        } else {
            this.pattern = null;
        }
    }

    public String getVariable() {
        return variable;
    }

    public boolean isDefaultPattern() {
        return content == null;
    }

    public boolean isFixedContent() {
        return variable == null;
    }

    public int match(String uri, int start) {
        if (isFixedContent()) {
            return matchFixedContent(uri, start);
        } else {
            if (isDefaultPattern()) {
                return matchDefaultPatternContent(uri, start);
            } else {
                return matchCustomPatternContent(uri, start);
            }
        }
    }

    private int matchCustomPatternContent(String uri, int start) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find(start) && matcher.start() == start) {
            return matcher.end();
        }

        return -1;
    }

    private int matchDefaultPatternContent(String uri, int start) {
        int position = start;
        int uriLength = uri.length();
        while (position < uriLength && uri.charAt(position) != '/') {
            position++;
        }

        return position;
    }

    private int matchFixedContent(String uri, int start) {
        if (start + content.length() > uri.length()) {
            return -1;
        }

        if (uri.substring(start, start + content.length()).equals(content)) {
            return start + content.length();
        } else {
            return -1;
        }
    }
}
