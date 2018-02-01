package im.goody.android.screens.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

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
    private static final String ID_KEY = "ProfileController.id";

    @Inject
    PreferencesManager manager;

    private ProfileViewModel viewModel;

    public ProfileController(String id) {
        super(new BundleBuilder()
                .putString(ID_KEY, id)
                .build());
    }

    public ProfileController(Bundle args) {
        super(args);
    }

    // ======= region BaseController =======

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
                .setToolbarVisible(false)
                .setHomeState(BarBuilder.HOME_GONE)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_profile;
    }

    // end

    // ======= region ProfileController =======

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        view().takeActivity(getActivity());

        if (String.valueOf(manager.getUserId()).equals(getId()))
            view().hideFollowButton();

        if (viewModel == null) loadData();
        else view().setData(viewModel);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        view().takeActivity(null);

        super.onDetach(view);
    }

    void loadData() {
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

    void follow() {
        repository.changeFollowState(getId())
                .subscribe(
                        result ->
                                viewModel.updateFollowState(result),
                        error ->
                                view().showErrorWithRetry(
                                        getErrorMessage(error),
                                        v -> follow()
                                )
                );
    }

    // end

    // ======= region private methods =======

    private String getId() {
        if (viewModel != null)
            return viewModel.getId();
        return getArgs().getString(ID_KEY);
    }

    void showPosts() {
        rootPresenter.showUserPosts(getId());
    }

    void showAvatar() {
        rootPresenter.showPhotoScreen(viewModel.getAvatarUrl());
    }

    void backClicked() {
        if (getActivity() != null)
            getActivity().onBackPressed();
    }

    // end

    // ======= region DI =======

    @Subcomponent
    @DaggerScope(ProfileController.class)
    public interface Component {
        void inject(ProfileController controller);
    }

    // end
}
