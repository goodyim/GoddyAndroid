package im.goody.android.screens.news;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.util.List;

import im.goody.android.BR;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.res.EventStateRes;
import im.goody.android.screens.common.ActionPanelViewModel;

public class NewsItemViewModel extends BaseObservable {
    private final Deal deal;

    public final ActionPanelViewModel panelViewModel;

    @Bindable
    private boolean expanded;

    public final ObservableBoolean participates;
    public final ObservableField<String> state = new ObservableField<>("");

    public final ObservableField<List<String>> lastParticipants = new ObservableField<>() ;
    public final ObservableInt participantsCount = new ObservableInt();

    NewsItemViewModel(Deal deal) {
        this.deal = deal;

        expanded = false;
        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());

        if (deal.getEvent() != null) {
            state.set(deal.getEvent().getState());
            lastParticipants.set(deal.getEvent().getLastParticipantsAvatars());
            participantsCount.set(deal.getEvent().getParticipantsCount());
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    public Deal getDeal() {
        return deal;
    }
}
