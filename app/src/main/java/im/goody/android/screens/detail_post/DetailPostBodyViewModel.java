package im.goody.android.screens.detail_post;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import im.goody.android.data.dto.Deal;
import im.goody.android.screens.common.ActionPanelViewModel;

public class DetailPostBodyViewModel {
    private Deal deal;
    public ActionPanelViewModel panelViewModel;

    public final ObservableBoolean participates;
    public ObservableField<String> state;

    public DetailPostBodyViewModel(Deal deal) {
        this.deal = deal;

        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());

        if (deal.getEvent() != null)
            state = new ObservableField<>(deal.getEvent().getState());
    }

    public Deal getDeal() {
        return deal;
    }
}
