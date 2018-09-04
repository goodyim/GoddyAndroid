package im.goody.android.screens.detail_post;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.screens.common.ActionPanelViewModel;

public class DetailPostBodyViewModel {
    private Deal deal;
    public ActionPanelViewModel panelViewModel;

    public final ObservableBoolean participates;
    public ObservableField<String> state;

    public final ObservableField<List<String>> lastParticipants = new ObservableField<>() ;
    public final ObservableInt participantsCount = new ObservableInt();

    public DetailPostBodyViewModel(Deal deal) {
        this.deal = deal;

        panelViewModel = new ActionPanelViewModel(deal);
        participates = new ObservableBoolean(deal.isParticipates());

        if (deal.getEvent() != null) {
            state = new ObservableField<>(deal.getEvent().getState());
            lastParticipants.set(deal.getEvent().getLastParticipantsAvatars());
            participantsCount.set(deal.getEvent().getParticipantsCount());
        }
    }

    public Deal getDeal() {
        return deal;
    }
}
