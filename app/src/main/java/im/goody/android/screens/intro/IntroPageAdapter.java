package im.goody.android.screens.intro;

import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;

import im.goody.android.screens.choose_help.ChooseHelpViewModel;
import im.goody.android.screens.intro.finish.IntroFinishController;
import im.goody.android.screens.intro.location.LocationNotificationsController;
import im.goody.android.screens.intro.resources.ResourcesController;

class IntroPageAdapter extends RouterPagerAdapter {
    static final int PAGE_COUNT = 3;

    private ChooseHelpViewModel viewModel;

    IntroPageAdapter(@NonNull Controller host, ChooseHelpViewModel viewModel) {
        super(host);
        this.viewModel = viewModel;
    }

    @Override
    public void configureRouter(@NonNull Router router, int position) {
        if (!router.hasRootController()) {
            Controller controller;
            switch (position) {
                case 0:
                    controller = new ResourcesController(viewModel);
                    break;
                case 1:
                    controller = new LocationNotificationsController(viewModel);
                    break;
                case 2:
                default:
                    controller = new IntroFinishController(viewModel);
                    break;
            }

            router.setRoot(RouterTransaction.with(controller));
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}