package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import im.goody.android.data.dto.Comment;

public class CommentRes {
    private Comment comment;

    @JsonProperty("comments_count")
    private int commentsCount;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
