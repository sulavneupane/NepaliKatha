package com.nepalicoders.nepalikatha.objects;

import java.io.Serializable;

/**
 * Created by sulav on 1/2/16.
 */
public class Poem implements Serializable {
    long id;
    String title;
    String writer;
    String content;
    String category;
    String publishedOn;
    String submittedBy;
    String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.replace("\n", "<br />");
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer.replace("\n", "<br />");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.replace("\n", "<br />");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy.replace("\n", "<br />");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.replace("\n", "<br />");
    }

    @Override
    public String toString() {
        return "Poem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", publishedOn='" + publishedOn + '\'' +
                ", submittedBy='" + submittedBy + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
