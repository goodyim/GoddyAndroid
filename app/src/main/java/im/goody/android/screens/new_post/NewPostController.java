package im.goody.android.screens.new_post;

import android.Manifest;
import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.location.places.Place;

import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.common.NewController;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.NetUtils;
import im.goody.android.utils.TextUtils;
import im.goody.android.utils.UIUtils;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("unused")
public class NewPostController extends NewController<NewPostView> {
    private NewPostViewModel viewModel;

    private Long id;

    public NewPostController(Deal deal) {
        id = deal.getId();
        viewModel = new NewPostViewModel(deal);

        if (!TextUtils.isEmpty(deal.getImageUrl())) {
            tempImageUrl = NetUtils.buildDealImageUrl(deal);

            if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                loadImage(tempImageUrl);
            } else {
                requestPermissions(STORAGE_PERMISSIONS, CACHE_IMAGE_REQUEST);
            }
        }
    }

    public NewPostController() {
        viewModel = new NewPostViewModel();
    }

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
                .setTitleRes(id == null ? R.string.new_post_title : R.string.edit_post_title)
                .setHomeListener(v -> {
                    Activity activity = getActivity();
                    if (activity != null) {
                        UIUtils.hideKeyboard(activity);
                        activity.onBackPressed();
                    }
                })
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_new_post;
    }

    // endregion

    // ======= region NewPostController =======

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setData(viewModel);
    }

    void clearLocation() {
        viewModel.setLocation(null);
    }

    void sendData() {
        if (id != null) editPost();
        else createPost();
    }

    // endregion

    // ======= region NewController =======

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
        viewModel.setLocation(place);
    }

    // end

    // ======= region private methods =======

    private void editPost() {
        rootPresenter.showProgress(R.string.edit_post_progress);
        Disposable disposable = repository.editPost(id, viewModel.body(), viewModel.getImageUri())
                .subscribe(
                        result -> {
                            rootPresenter.hideProgress();
                            rootPresenter.showMain();
                        },
                        error -> {
                            rootPresenter.hideProgress();
                            showError(error);
                        }
                );

        compositeDisposable.add(disposable);
    }

    private void createPost() {
        rootPresenter.showProgress(R.string.create_post_progress);
        Disposable disposable = repository.createPost(viewModel.body(), viewModel.getImageUri())
                .subscribe(
                        result -> {
                            rootPresenter.hideProgress();
                            rootPresenter.showMain();
                        },
                        error -> {
                            rootPresenter.hideProgress();
                            showError(error);
                        }
                );

        compositeDisposable.add(disposable);
    }

    // endregion

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(NewPostController.class)
    public interface Component {

        void inject(NewPostController controller);
    }

    // endregion
}
