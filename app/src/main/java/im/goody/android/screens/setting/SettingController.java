package im.goody.android.screens.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceController;
import android.view.View;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.root.IRootPresenter;
import im.goody.android.screens.choose_help.ChooseHelpController;
import im.goody.android.ui.helpers.BarBuilder;

public class SettingController extends PreferenceController {

    private static final String CHOOSE_HELP_KEY = "events_notifications";

    @Inject
    protected IRootPresenter rootPresenter;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);

        getPreferenceScreen().findPreference(CHOOSE_HELP_KEY)
                .setOnPreferenceClickListener(preference -> {
                    rootPresenter.showChooseHelp(ChooseHelpController.MODE_EDIT);
                    return true;
                });
    }

    @Override
    public void onAttach(@NonNull View view) {
        super.onAttach(view);
        initDagger();
        initActionBar();
    }

    private void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setTitleRes(R.string.setting_title)
                .setHomeState(BarBuilder.HOME_HAMBURGER)
                .build();
    }

    private void initDagger() {
        RootComponent rootComponent = App.getRootComponent();
        Component component = rootComponent.plusSetting();
        component.inject(this);

    }

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(SettingController.class)
    public interface Component {
        void inject(SettingController controller);
    }

    //endregion
}
