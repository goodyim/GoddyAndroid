package im.goody.android.screens.common;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import im.goody.android.data.dto.Deal;

public class ActionPanelViewModel {
    public final ObservableBoolean isLiked = new ObservableBoolean();
    public final ObservableInt likedCount = new ObservableInt();
    public final ObservableInt commentsCount = new ObservableInt();

    public ActionPanelViewModel(Deal deal) {
        isLiked.set(deal.isLiked());
        likedCount.set(deal.getLikesCount());
        commentsCount.set(deal.getCommentsCount());
    }
}
