package im.goody.android.screens.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import im.goody.android.BR;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.res.EventStateRes;
import im.goody.android.screens.common.ActionPanelViewModel;

public class MainItemViewModel extends BaseObservable {
    private final Deal deal;

    public final ActionPanelViewModel panelViewModel;

    @Bindable
    private boolean expanded;

    public final ObservableBoolean participates;
    public final ObservableField<String> state = new ObservableField<>("");

    MainItemViewModel(Deal deal) {
        this.deal = deal;

        expanded = false;
        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());

        if (deal.getEvent() != null)
            state.set(deal.getEvent().getState());
    }

    public boolean isExpanded() {
        return expanded;
    }

    void changeEventState(EventStateRes stateRes) {
        state.set(stateRes.getState());
        deal.getEvent().setState(stateRes.getState());
    }

    void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    public Deal getDeal() {
        return deal;
    }
}
