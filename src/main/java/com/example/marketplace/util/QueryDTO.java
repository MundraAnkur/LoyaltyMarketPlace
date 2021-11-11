package com.example.marketplace.util;

/**
 * @author ankurmundra
 * November 09, 2021
 */
public class QueryDTO {
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "QueryDTO{ query='" + query + '\'' + '}';
    }
}
