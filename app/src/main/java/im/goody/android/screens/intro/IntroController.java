package im.goody.android.screens.intro;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class IntroController extends BaseController {

    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusIntro();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(false)
                .build();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_intro, container, false);
    }

    //endregion

    //region ================= DI =================

    @dagger.Subcomponent()
    @DaggerScope(IntroController.class)
    public interface Component {
        void inject(IntroController controller);
    }

    //endregion
}
