package im.goody.android.screens.greet;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;

public class GreetController extends BaseController<GreetView> {
    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusGreet();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setHomeState(BarBuilder.HOME_GONE)
                .setStatusBarVisible(false)
                .setToolbarVisible(false)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_greet;
    }

    public void next() {
        rootPresenter.showLoginScreen();
    }

    //endregion


    //region ================= DI =================

    @dagger.Subcomponent()
    @DaggerScope(GreetController.class)
    public interface Component {
        void inject(GreetController controller);
    }

    //endregion
}
