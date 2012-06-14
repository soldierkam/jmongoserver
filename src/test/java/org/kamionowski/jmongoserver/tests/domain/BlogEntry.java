package org.kamionowski.jmongoserver.tests.domain;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * User: soldier
 * Date: 12.06.12
 * Time: 19:24
 */
@Entity
public class BlogEntry implements Serializable {

    @Id
    private ObjectId id;
    private String title;
    private String lead;
    private String content;
    private Author author;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
