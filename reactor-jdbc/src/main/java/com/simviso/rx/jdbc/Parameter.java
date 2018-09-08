package com.simviso.rx.jdbc;

/**
 * Encapsulates a query parameter.
 *
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2018/9/8 22:31.
 */
public final class Parameter {

    private final String name;
    /**
     * Actual query parameter value to be encapsulated.
     */
    private final Object value;

    /**
     * Constructor.
     *
     * @param value
     */
    Parameter(Object value) {
        this(null, value);
    }

    Parameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the parameter value.
     *
     * @return Object
     */
    Object value() {
        return value;
    }

    boolean hasName() {
        return name != null;
    }

    String name() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (value instanceof String)
            builder.append("'");
        builder.append(name);
        builder.append(" - ");
        builder.append(value);
        builder.append("'");
        return builder.toString();
    }
}
