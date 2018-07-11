package im.goody.android.screens.intro;

import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;
import im.goody.android.ui.helpers.BarBuilder;

public class IntroController extends BaseController<IntroView> {
    private ChooseHelpViewModel viewModel = new ChooseHelpViewModel();

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
                .setHomeState(BarBuilder.HOME_GONE)
                .setStatusBarVisible(false)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_intro;
    }

    //endregion

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        view().setData(viewModel);
    }


    //region ================= DI =================

    @dagger.Subcomponent()
    @DaggerScope(IntroController.class)
    public interface Component {
        void inject(IntroController controller);
    }

    //endregion
}
