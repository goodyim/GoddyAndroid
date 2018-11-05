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
import io.reactivex.disposables.Disposable;

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
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.profile)
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

        if (viewModel == null) loadData();
        else view().setData(viewModel);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
    }

    void loadData() {
        Disposable d = repository.getUserProfile(getId()).subscribe(user -> {
            viewModel = new ProfileViewModel(user, isMineProfile());
            view().setData(viewModel);

            if (getActivity() != null)
                getActivity().setTitle(user.getName());
        }, error -> {
            view().finishRefresh();
            view().showErrorWithRetry(getErrorMessage(error), v -> {
                view().startRefresh();
                loadData();
            });
        });
        compositeDisposable.add(d);
    }

    void follow() {
        Disposable d = repository.changeFollowState(getId())
                .subscribe(
                        result ->
                                viewModel.updateFollowState(result),
                        error ->
                                view().showErrorWithRetry(
                                        getErrorMessage(error),
                                        v -> follow()
                                )
                );
        compositeDisposable.add(d);
    }


    // end

    // ======= region private methods =======

    private String getId() {
        if (viewModel != null)
            return viewModel.getId();
        return getArgs().getString(ID_KEY);
    }


    void showAvatar() {
        rootPresenter.showPhotoScreen(viewModel.getAvatarUrl());
    }


    private boolean isMineProfile() {
        return String.valueOf(manager.getUserId()).equals(getId()) || manager.getUserName().equals(getId());
    }

    public void showDeals() {
        rootPresenter.showUserPosts(viewModel.getId());
    }

    public void showFollowers() {
        if (viewModel != null)
            rootPresenter.showFollowers(getId());
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
