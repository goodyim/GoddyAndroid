package im.goody.android.data.dto;

public class Deal {
    private Author author;
    private String date;
    private String title;
    private String description;
    private String imageUrl;

    private int likesCount;
    private int commentsCount;

    public static Deal getFake() {
        Deal deal = new Deal();
        deal.setAuthor(new Author().setName("User name"))
                .setCommentsCount(0)
                .setLikesCount(0)
                .setDate("Date")
                .setTitle("Fake item")
                .setDescription("Fake item descuption");
        return deal;
    }

    public static class Author {
        private String name;
        private String avatarUrl;

        public String getName() {
            return name;
        }

        public Author setName(String name) {
            this.name = name;
            return this;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public Author setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }
    }

    public Author getAuthor() {
        return author;
    }

    public Deal setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Deal setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Deal setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Deal setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public int getCommentsCount() {
        return commentsCount;
    }

    public Deal setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
        return this;
    }
}
