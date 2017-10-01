package im.goody.android.screens.setting;

import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;

public class SettingController extends BaseController<SettingView> {

    private SettingViewModel viewModel = new SettingViewModel();

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusSetting();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setTitleRes(R.string.setting_title)
                .setHomeState(BarBuilder.HOME_HAMBURGER)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_setting;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        // TODO: 08.09.2017 set viewModel settings
        view().setViewModel(viewModel);
    }

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(SettingController.class)
    public interface Component {
        void inject(SettingController controller);
    }

    //endregion
}
