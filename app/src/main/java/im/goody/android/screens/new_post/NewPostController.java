package im.goody.android.screens.new_post;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import im.goody.android.R;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.common.NewController;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.UIUtils;

public class NewPostController extends NewController<NewPostView> {
    private NewPostViewModel viewModel = new NewPostViewModel();

    // ======= region Base Controller =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusNewPost();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.new_post_title)
                .setHomeListener(v -> {
                    Activity activity = getActivity();
                    if (activity != null) {
                        UIUtils.hideKeyboard(activity);
                        activity.onBackPressed();
                    }
                })
                .build();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_new_post, container, false);
    }

    // endregion

    // ======= region NewPostController =======

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setData(viewModel);
    }

    void clearLocation() {
        viewModel.location.set(null);
    }

    void createPost() {
        rootPresenter.showProgress(R.string.create_post_progress);
        disposable = repository.createPost(viewModel.body(), viewModel.getImageUri())
                .subscribe(
                        result -> {
                            rootPresenter.hideProgress();
                            rootPresenter.showMainScreen();
                        },
                        error -> {
                            rootPresenter.hideProgress();
                            showError(error);
                        }
                );
    }

    // endregion

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(NewPostController.class)
    public interface Component {

        void inject(NewPostController controller);
    }

    // endregion

    // ======= region private methods =======

    @Override
    protected Uri getImageUri() {
        return viewModel.getImageUri();
    }

    @Override
    protected void imageUriChanged(Uri uri) {
        viewModel.setImageUri(uri);
    }

    @Override
    protected void imageChanged(Uri uri) {
        viewModel.setImageFromUri(uri);
    }

    @Override
    protected void placeChanged(Place place) {
        viewModel.location.set(place);
    }

    // endregion
}
