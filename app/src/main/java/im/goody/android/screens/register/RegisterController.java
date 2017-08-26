package im.goody.android.screens.register;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class RegisterController extends BaseController<RegisterView> {
    private static final int GALLERY_REQUEST = 100;
    private static final int PERMISSION_CODE = 200;

    private static final String[] PERMISSIONS = {permission.READ_EXTERNAL_STORAGE};

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
                .setBackArrow(true)
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
            makeGalleryRequest();
        } else {
            requestPermissions(PERMISSIONS, PERMISSION_CODE);
        }
    }

    void register() {
        if (viewModel.isValid()) {
            rootPresenter.showProgress(R.string.register_progress_title);
            disposable = repository.register(viewModel.body()).subscribe(
                    result -> {
                        rootPresenter.hideProgress();
                        rootPresenter.showMainScreen(true);
                    },
                    error -> {
                        rootPresenter.hideProgress();
                        view().showMessage(error.getMessage());
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
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                viewModel.setAvatar(uri);
            }
        }
    }

    private void makeGalleryRequest() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/**");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(RegisterController.class)
    public interface Component {
        void inject(RegisterController controller);
    }

    //endregion
}
