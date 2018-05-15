package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Deal {
    private long id;
    private String description;

    private boolean participates;

    @JsonProperty("likes_count")
    private int likesCount;

    private boolean liked;
    @JsonProperty("comments_count")
    private int commentsCount;

    @JsonProperty("created_at")
    private String date;

    @JsonProperty("name")
    private String title;

    @JsonProperty("image_file_name")
    private String imageUrl;
    private Author author;

    private Location location;

    private Event event;

    @JsonProperty("is_author")
    private boolean isOwner;

    private List<Comment> comments;

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public boolean isLiked() {
        return liked;
    }
    public Event getEvent() {
        return event;
    }

    public Location getLocation() {
        return location;
    }

    // ======= region getters =======

    public boolean isOwner() {
        return isOwner;
    }

    public boolean isParticipates() {
        return participates;
    }

    public long getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    // endregion

    // ======= region setters =======

    public Deal isOwner(boolean isOwner) {
        this.isOwner = isOwner;
        return this;
    }

    public Deal setParticipates(boolean participates) {
        this.participates = participates;
        return this;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Deal setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Deal setDate(String date) {
        this.date = date;
        return this;
    }

    public Deal setTitle(String title) {
        this.title = title;
        return this;
    }

    public Deal setDescription(String description) {
        this.description = description;
        return this;
    }

    public Deal setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public Deal setLikesCount(int likesCount) {
        this.likesCount = likesCount;
        return this;
    }

    public Deal setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
        return this;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // endregion

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Event {
        public static final String ACTIVE = "active";
        public static final String IN_PROGRESS = "in_process";
        public static final String CLOSED = "finished";

        private String resources;
        private String date;

        private boolean immediately = false;

        @JsonProperty("aasm_state")
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setImmediately(boolean immediately) {
            this.immediately = immediately;
        }

        public String getResources() {
            return resources;
        }

        public String getDate() {
            return date;
        }


        public void setResources(String resources) {
            this.resources = resources;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isImmediately() {
            return immediately;
        }

        public boolean isOpen() {
            return !state.equals(CLOSED);
        }
    }
}
