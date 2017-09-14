package im.goody.android.screens.detail_post;

import android.databinding.ObservableBoolean;

import im.goody.android.data.dto.Deal;
import im.goody.android.screens.common.ActionPanelViewModel;

public class DetailPostBodyViewModel {
    private Deal deal;
    public ActionPanelViewModel panelViewModel;

    public final ObservableBoolean participates;
    public ObservableBoolean active;

    public DetailPostBodyViewModel(Deal deal) {
        this.deal = deal;

        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());

        if (deal.getEvent() != null)
            active = new ObservableBoolean(deal.getEvent().isActive());
    }

    public Deal getDeal() {
        return deal;
    }
}
