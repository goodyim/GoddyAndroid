package im.goody.android.screens.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import im.goody.android.BR;
import im.goody.android.data.dto.Deal;
import im.goody.android.screens.common.ActionPanelViewModel;

public class MainItemViewModel extends BaseObservable{
    private final Deal deal;

    public final ActionPanelViewModel panelViewModel;

    @Bindable
    private boolean expanded;

    public final ObservableBoolean participates;

    MainItemViewModel(Deal deal) {
        this.deal = deal;

        expanded = false;
        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());
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
