package im.goody.android.screens.detail_post;

import android.databinding.ObservableField;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.NewCommentReq;
import im.goody.android.data.network.res.CommentRes;

public class DetailPostViewModel {
    private Deal deal;
    private int position;
    private long id;
    public final ObservableField<String> commentBody = new ObservableField<>("");

    DetailPostViewModel() {
        position = 0;
    }

    // ======= region getters =======

    NewCommentReq getCommentObject() {
        return new NewCommentReq().setContent(commentBody.get());
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

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public void setId(long id) {
        this.id = id;
    }

    void addComment(CommentRes comment) {
        deal.addComment(comment.getComment());
        deal.setCommentsCount(comment.getCommentsCount());
    }

    // endregion
}
