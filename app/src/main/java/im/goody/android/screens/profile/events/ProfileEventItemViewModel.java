package im.goody.android.screens.profile.events;

import im.goody.android.data.dto.Deal;
import im.goody.android.screens.common.ActionPanelViewModel;

public class ProfileEventItemViewModel {
    private final Deal deal;

    private final ActionPanelViewModel panelViewModel;

    public ProfileEventItemViewModel(Deal deal) {
        this.deal = deal;

        panelViewModel = new ActionPanelViewModel(deal);
    }

    public ActionPanelViewModel getPanelViewModel() {
        return panelViewModel;
    }

    public Deal getDeal() {
        return deal;
    }
}
