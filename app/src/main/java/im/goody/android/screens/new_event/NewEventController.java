package im.goody.android.screens.new_event;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.Subcomponent;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.UIUtils;


public class NewEventController extends BaseController<NewEventView> {

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusNewEvent();
        if (component != null)
            component.inject(this);
    }


    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.new_event_title)
                .setHomeListener(v -> {
                    Activity activity = getActivity();
                    if (activity != null) {
                        UIUtils.hideKeyboard(activity);
                        activity.onBackPressed();
                    }
                })
                .build();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_new_event, container, false);
    }

    //endregion

    // ======= region DI =======

    @Subcomponent
    @DaggerScope(NewEventController.class)
    public interface Component {
        void inject(NewEventController controller);
    }

    //endregion
}
