package im.goody.android.screens.profile.events;

import java.util.List;

public class ProfileEventsViewModel {
    private List<ProfileEventItemViewModel> events;

    public List<ProfileEventItemViewModel> getEvents() {
        return events;
    }

    public void setEvents(List<ProfileEventItemViewModel> events) {
        this.events = events;
    }
}
