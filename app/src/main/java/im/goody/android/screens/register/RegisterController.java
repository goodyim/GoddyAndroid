package im.goody.android.screens.register;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.dialogs.ChooseImageOptionsDialog;
import im.goody.android.ui.dialogs.OptionsDialog;
import im.goody.android.ui.helpers.BarBuilder;

import static android.app.Activity.RESULT_OK;

public class RegisterController extends BaseController<RegisterView> {
    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int CAMERA_REQUEST = 1;

    private static final int PERMISSION_CODE = 200;
    private static final String[] PERMISSIONS = {permission.READ_EXTERNAL_STORAGE};

    private OptionsDialog dialog = new ChooseImageOptionsDialog();
    private RegisterViewModel viewModel = new RegisterViewModel();

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusRegister();
        component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setHomeState(BarBuilder.HOME_ARROW)
                .setToolbarVisible(true)
                .setTitleRes(R.string.register_title)
                .build();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_register, container, false);
    }

    //endregion

    // ======= region RegisterController =======

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setData(viewModel);
    }

    void chooseAvatar() {
        if (isPermissionGranted(permission.READ_EXTERNAL_STORAGE)){
            showDialog();
        } else {
            requestPermissions(PERMISSIONS, PERMISSION_CODE);
        }
    }

    void register() {
        if (viewModel.isValid()) {
            rootPresenter.showProgress(R.string.register_progress_title);
            disposable = repository.register(viewModel.body(), viewModel.getAvatarUri())
                    .subscribe(
                            result -> {
                                rootPresenter.hideProgress();
                                rootPresenter.showNews();
                            },
                            error -> {
                                rootPresenter.hideProgress();
                                showError(error);
                            }
            );
        } else {
            view().showMessage(R.string.invalid_fields_message);
        }
    }

    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();

                        viewModel.setAvatarUri(uri);
                        viewModel.setAvatar(uri, false);
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = viewModel.getAvatarUri();
                    viewModel.setAvatar(uri, true);
                }
        }
    }

    // ======= region private methods =======

    private void showDialog() {
        disposable = dialog.show(getActivity()).subscribe(index -> {
            switch (index) {
                case IMAGE_PICK_REQUEST:
                    makeGalleryRequest();
                    break;
                case CAMERA_REQUEST:
                    takePhoto();
            }
        });
    }

    private void makeGalleryRequest() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/**");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),
                "photo_" + System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        viewModel.setAvatarUri(Uri.fromFile(photo));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    // endregion

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(RegisterController.class)
    public interface Component {
        void inject(RegisterController controller);
    }

    //endregion
}
