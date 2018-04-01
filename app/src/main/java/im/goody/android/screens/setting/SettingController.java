package im.goody.android.screens.setting;

import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Settings;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.root.IRootPresenter;
import im.goody.android.ui.helpers.BarBuilder;

public class SettingController extends BaseController<SettingView> {

    @Inject
    protected IRootPresenter rootPresenter;

    private SettingViewModel viewModel;

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusSetting();
        component.inject(this);

        viewModel = new SettingViewModel(repository.getSettings());
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        view().setViewModel(viewModel);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setTitleRes(R.string.setting_title)
                .setHomeState(BarBuilder.HOME_ARROW)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_setting;
    }

    void saveSettings() {
        repository.saveSettings(new Settings(
                viewModel.isAlertFromSubscriber(),
                viewModel.isAlertFromNearby(),
                viewModel.isNotifyMentions(),
                viewModel.isNotifyMessages()
        ));
        getRouter().handleBack();
    }

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(SettingController.class)
    public interface Component {
        void inject(SettingController controller);
    }

    //endregion
}
