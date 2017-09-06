package im.goody.android.screens.detail_post;

import im.goody.android.data.dto.Comment;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.NewCommentReq;

public class DetailPostViewModel {
    private Deal deal;
    private int position;
    private long id;
    private String commentBody;

    DetailPostViewModel() {
        position = 0;
    }

    // ======= region getters =======

    public String getCommentBody() {
        return commentBody;
    }

    NewCommentReq getCommentObject() {
        return new NewCommentReq().setContent(commentBody);
    }

    public Deal getDeal() {
        return deal;
    }

    int getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    // endregion

    // ======= region setters =======

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public void setId(long id) {
        this.id = id;
    }

    void addComment(Comment comment) {
        deal.addComment(comment);
    }

    // endregion
}
