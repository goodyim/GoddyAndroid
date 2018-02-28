package im.goody.android.data.dto;

public class Feedback {
    private String type;
    private long objectId;
    private Author author;
    private String date;

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
