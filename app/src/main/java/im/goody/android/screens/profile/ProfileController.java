package im.goody.android.screens.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.Subcomponent;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.BundleBuilder;

public class ProfileController extends BaseController<ProfileView> {
    public static final String ID_KEY = "ProfileController.id";

    @Inject
    PreferencesManager manager;

    private ProfileViewModel viewModel;

    public ProfileController(long id) {
        super(new BundleBuilder(new Bundle())
                .putLong(ID_KEY, id)
                .build());
    }

    public ProfileController(Bundle args) {
        super(args);
    }

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusProfile();
        if (component != null) {
            component.inject(this);
        }
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.profile)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (manager.getUserId() == getId())
            view().hideFollowButton();

        if (viewModel == null) loadData();
        else view().setData(viewModel);
    }

    public void loadData() {
        repository.getUserProfile(getId()).subscribe(user -> {
            viewModel = new ProfileViewModel(user);
            view().setData(viewModel);
        }, error -> {
            view().finishRefresh();
            view().showErrorWithRetry(getErrorMessage(error), v -> {
                view().startRefresh();
                loadData();
            });
        });
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_profile, container, false);
    }

    @Subcomponent
    @DaggerScope(ProfileController.class)
    public interface Component {
        void inject(ProfileController controller);
    }

    private long getId() {
        return getArgs().getLong(ID_KEY);
    }
}
