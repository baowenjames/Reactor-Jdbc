package com.simviso.rx.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2018/9/8 22:43.
 */
public final class SqlWithNames {

    private final String sql;
    private final List<String> names;

    SqlWithNames(String sql, List<String> names) {
        this.sql = sql;
        this.names = names;
    }

    String sql() {
        return sql;
    }

    List<String> names() {
        return names;
    }

    static SqlWithNames parse(String namedSql) {
        // was originally using regular expressions, but they didn't work well
        // for ignoring parameter-like strings inside quotes.
        List<String> names = new ArrayList<>();
        int length = namedSql.length();
        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        for (int i = 0; i < length; i++) {
            char c = namedSql.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if (c == ':' && i + 1 < length && !isFollowedOrPrefixedByColon(namedSql, i)
                        && Character.isJavaIdentifierStart(namedSql.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length && Character.isJavaIdentifierPart(namedSql.charAt(j))) {
                        j++;
                    }
                    String name = namedSql.substring(i + 1, j);
                    c = '?'; // replace the parameter with a question mark
                    i += name.length(); // skip past the end if the parameter
                    names.add(name);
                }
            }
            parsedQuery.append(c);
        }
        return new SqlWithNames(parsedQuery.toString(), names);
    }

    // Visible for testing
    static boolean isFollowedOrPrefixedByColon(String sql, int i) {
        return ':' == sql.charAt(i + 1) || (i > 0 && ':' == sql.charAt(i - 1));
    }
}
