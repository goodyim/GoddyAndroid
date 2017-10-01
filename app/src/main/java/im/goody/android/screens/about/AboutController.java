package im.goody.android.screens.about;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class AboutController extends BaseController<AboutView> {

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusAbout();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(false)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_about;
    }

    void onBackPressed() {
        getRouter().handleBack();
    }

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(AboutController.class)
    public interface Component {
        void inject(AboutController controller);
    }

    // endregion
}
