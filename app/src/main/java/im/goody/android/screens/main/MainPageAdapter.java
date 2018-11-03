package im.goody.android.screens.main;

import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.screens.news.NewsController;

public class MainPageAdapter extends RouterPagerAdapter {
    private static final int[] TABS_TITLES = {
            R.string.content_feed, R.string.content_all,
            R.string.content_deals, R.string.content_events
    };

    private static final String[] CONTENT_TYPES = {
            NewsController.CONTENT_FEED, NewsController.CONTENT_All,
            NewsController.CONTENT_POSTS, NewsController.CONTENT_EVENTS
    };

    private static final int TABS_COUNT = 4;

    public MainPageAdapter(@NonNull Controller host) {
        super(host);
    }

    @Override
    public void configureRouter(@NonNull Router router, int position) {
        if (!router.hasRootController()) {
            Controller controller = new NewsController(CONTENT_TYPES[position], false);
            router.setRoot(RouterTransaction.with(controller));
        }
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return App.getAppContext().getString(TABS_TITLES[position]);
    }
}
