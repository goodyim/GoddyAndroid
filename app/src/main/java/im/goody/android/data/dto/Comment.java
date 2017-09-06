package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // endregion
}
