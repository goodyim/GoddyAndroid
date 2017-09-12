package im.goody.android.screens.detail_post;

import im.goody.android.data.dto.Deal;
import im.goody.android.screens.common.ActionPanelViewModel;

public class DetailPostBodyViewModel {
    private Deal deal;
    public ActionPanelViewModel panelViewModel;

    public DetailPostBodyViewModel(Deal deal) {
        this.deal = deal;

        panelViewModel = new ActionPanelViewModel(deal);
    }

    public Deal getDeal() {
        return deal;
    }
}
