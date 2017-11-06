package im.goody.android.screens.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

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
    public final ObservableBoolean active = new ObservableBoolean(false);

    MainItemViewModel(Deal deal) {
        this.deal = deal;

        expanded = false;
        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());

        if (deal.getEvent() != null)
            active.set(deal.getEvent().isActive());
    }

    public boolean isExpanded() {
        return expanded;
    }

    void changeEventState(EventStateRes stateRes) {
        active.set(stateRes.isActive());
        deal.getEvent().setActive(stateRes.isActive());
    }

    void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    public Deal getDeal() {
        return deal;
    }
}
