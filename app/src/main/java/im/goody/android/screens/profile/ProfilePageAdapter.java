package im.goody.android.screens.profile;

import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.dto.Event;
import im.goody.android.screens.profile.events.ProfileEventsController;

public class ProfilePageAdapter extends RouterPagerAdapter {
    private String userId;

    private static final int[] TABS_TITLES = {
            R.string.active_events, R.string.progress_events, R.string.finished_events
    };

    private static final String[] EVENT_TYPES = {
            Event.ACTIVE, Event.IN_PROGRESS, Event.CLOSED
    };

    private static final int TABS_COUNT = 3;

    public ProfilePageAdapter(@NonNull Controller host, String userId) {
        super(host);
        this.userId = userId;
    }

    @Override
    public void configureRouter(@NonNull Router router, int position) {
        if (!router.hasRootController()) {
            Controller controller = new ProfileEventsController(userId, EVENT_TYPES[position]);
            router.setRoot(RouterTransaction.with(controller));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return App.getAppContext().getString(TABS_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }
}
