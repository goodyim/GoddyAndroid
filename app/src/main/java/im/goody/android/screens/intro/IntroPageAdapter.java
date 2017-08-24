package im.goody.android.screens.intro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;

import im.goody.android.App;
import im.goody.android.screens.intro.page.Page;
import im.goody.android.screens.intro.page.PageController;

import static im.goody.android.Constants.Welcome.COLORS;
import static im.goody.android.Constants.Welcome.DESCRIPTIONS;
import static im.goody.android.Constants.Welcome.ICONS;
import static im.goody.android.Constants.Welcome.PAGES_COUNT;
import static im.goody.android.Constants.Welcome.TITLES;

class IntroPageAdapter extends RouterPagerAdapter {

    private Context appContext;

    IntroPageAdapter(@NonNull Controller host) {
        super(host);

        appContext = App.getAppContext();
    }

    @Override
    public void configureRouter(@NonNull Router router, int position) {
        if (!router.hasRootController()) {
            Page page = new Page.Builder()
                    .setColor(ContextCompat.getColor(appContext, COLORS[position]))
                    .setDescription(appContext.getResources().getString(DESCRIPTIONS[position]))
                    .setTitle(appContext.getResources().getString(TITLES[position]))
                    .setIcon(ICONS[position])
                    .build();
            Controller controller = new PageController(page);
            router.setRoot(RouterTransaction.with(controller));
        }
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }
}
