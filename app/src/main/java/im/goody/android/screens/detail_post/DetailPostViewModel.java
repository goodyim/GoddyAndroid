package im.goody.android.screens.detail_post;

import android.databinding.ObservableField;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.NewCommentReq;
import im.goody.android.data.network.res.CommentRes;
import im.goody.android.screens.common.ActionPanelViewModel;

public class DetailPostViewModel {
    private int position;
    private long id;
    public final ObservableField<String> commentBody = new ObservableField<>("");
    private DetailPostBodyViewModel bodyViewModel;

    DetailPostViewModel() {
        position = 0;
    }

    // ======= region getters =======

    NewCommentReq getCommentObject() {
        return new NewCommentReq().setContent(commentBody.get());
    }

    public DetailPostBodyViewModel getBody() {
        return bodyViewModel;
    }

    int getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    // endregion

    // ======= region setters =======

    public void setBody(Deal deal) {
        bodyViewModel = new DetailPostBodyViewModel(deal);
    }

    void setPosition(int position) {
        this.position = position;
    }

    public void setId(long id) {
        this.id = id;
    }

    void addComment(CommentRes comment) {
        bodyViewModel.getDeal().addComment(comment.getComment());
        bodyViewModel.getDeal().setCommentsCount(comment.getCommentsCount());

        bodyViewModel.panelViewModel.commentsCount.set(comment.getCommentsCount());
    }

    // endregion
}
