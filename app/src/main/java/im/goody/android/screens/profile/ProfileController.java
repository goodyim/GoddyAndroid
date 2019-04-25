package im.goody.android.screens.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.File;

import javax.inject.Inject;

import dagger.Subcomponent;
import im.goody.android.Constants;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.dialogs.ChooseImageOptionsDialog;
import im.goody.android.ui.dialogs.OptionsDialog;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.BundleBuilder;
import im.goody.android.utils.FileUtils;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

public class ProfileController extends BaseController<ProfileView> {
    private static final String ID_KEY = "ProfileController.id";

    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int CAMERA_REQUEST = 1;

    private static final int STORAGE_PERMISSION_REQUEST = 2;

    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private OptionsDialog dialog = new ChooseImageOptionsDialog();


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeGalleryRequest();
                } else {
                    view().showMessage(R.string.read_permission_denied);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case IMAGE_PICK_REQUEST:
                if (resultCode == RESULT_OK && data != null) {

                    Uri uri = data.getData();

                    uploadAvatar(uri);
                }

                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = viewModel.getTempImage();

                    uploadAvatar(uri);
                }
        }
    }

    private void uploadAvatar(Uri uri) {
        view().startRefresh();

        Disposable disposable = repository.changeAvatar(getId(), uri)
                .subscribe(user -> {
                    viewModel = new ProfileViewModel(user, isMineProfile());
                    view().setData(viewModel);

                    if (getActivity() != null)
                        getActivity().setTitle(user.getName());

                    if (view() != null) view().finishRefresh();
                }, error -> {
                    if (view() != null) view().finishRefresh();

                    showError(error);
                });

        compositeDisposable.add(disposable);
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


    void avatarClicked() {
        if (isMineProfile()) {
            tryOpenPhotoChooser();
        } else {
            rootPresenter.showPhotoScreen(viewModel.getAvatarUrl());
        }
    }

    // end

    // ======= region private methods =======


    private void makeGalleryRequest() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/**");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(FileUtils.getCacheFolder(), Constants.CACHE_FILE_NAME);

        Uri uri = FileUtils.uriFromFile(photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                uri);

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        viewModel.setTempImage(uri);

        startActivityForResult(intent, CAMERA_REQUEST);
    }


    private String getId() {
        if (viewModel != null)
            return viewModel.getId();
        return getArgs().getString(ID_KEY);
    }


    private void tryOpenPhotoChooser() {
        if (isPermissionGranted()) {
            showPhotoChooser();
        } else {
            requestPermissions(STORAGE_PERMISSIONS, STORAGE_PERMISSION_REQUEST);
        }
    }

    private void showPhotoChooser() {
        Disposable disposable = dialog.show(getActivity()).subscribe(index -> {
            switch (index) {
                case IMAGE_PICK_REQUEST:
                    makeGalleryRequest();
                    break;
                case CAMERA_REQUEST:
                    takePhoto();
            }
        });

        compositeDisposable.add(disposable);
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
