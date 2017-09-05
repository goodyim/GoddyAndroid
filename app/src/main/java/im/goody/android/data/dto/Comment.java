package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    private String content;
    private Author author;

    @JsonProperty("created_at")
    private String date;

    // ======= region getters =======

    public String getContent() {
        return content;
    }

    public Author getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    // endregion

    // ======= region setters =======

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // endregion
}
