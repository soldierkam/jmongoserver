package org.kamionowski.jmongoserver.tests.domain;

import com.google.code.morphia.annotations.Entity;

/**
 * User: soldier
 * Date: 13.06.12
 * Time: 22:34
 */
@Entity
public class Author {
    private String name;
    private String www;

    public Author(String name, String www) {
        this.name = name;
        this.www = www;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }
}
