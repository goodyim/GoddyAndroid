package im.goody.android.screens.new_post;

import android.Manifest.permission;
import android.app.Activity;
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

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.ChooseImageOptionsDialog;
import im.goody.android.ui.helpers.OptionsDialog;
import im.goody.android.utils.UIUtils;

import static android.app.Activity.RESULT_OK;

public class NewPostController extends BaseController<NewPostView> {
    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int CAMERA_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST = 2;
    private static final String[] LOCATION_PERMISSIONS = {permission.ACCESS_FINE_LOCATION};
    private static final String[] STORAGE_PERMISSIONS = {permission.READ_EXTERNAL_STORAGE};
    private NewPostViewModel viewModel = new NewPostViewModel();
    private OptionsDialog dialog = new ChooseImageOptionsDialog();

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
                .setBackArrow(true)
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

    void chooseLocation() {
        if (isPermissionGranted(permission.ACCESS_FINE_LOCATION)) {
            makePlacePickerRequest();
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }
    }

    void clearLocation() {
        viewModel.setLocation(null);
    }

    void choosePhoto() {
        if (isPermissionGranted(permission.READ_EXTERNAL_STORAGE)) {
            showDialog();
        } else {
            requestPermissions(STORAGE_PERMISSIONS, STORAGE_PERMISSION_REQUEST);
        }
    }

    void clearPhoto() {
        viewModel.setImage(null);
    }

    void createPost() {
        UIUtils.hideKeyboard(getActivity());
        rootPresenter.showProgress(R.string.create_post_progress);
        disposable = repository.createPost(viewModel.body(), viewModel.getImageUri())
                .subscribe(
                        result -> {
                            rootPresenter.hideProgress();
                            rootPresenter.showMainScreen(true);
                        },
                        error -> {
                            rootPresenter.hideProgress();
                            view().showMessage(error.getMessage());
                        }
                );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK && getActivity() != null && data != null) {
                    Place place = PlacePicker.getPlace(getActivity(), data);
                    viewModel.setLocation(place);
                }
                break;
            case IMAGE_PICK_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();

                        viewModel.setImageUri(uri);
                        viewModel.setImage(uri, false);
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = viewModel.getImageUri();
                    viewModel.setImage(uri, true);
                }
        }
    }

    // endregion

    // ======= region Permissions =======

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePlacePickerRequest();
                } else {
                    view().showMessage(R.string.location_permission_denied);
                }
                break;
            case STORAGE_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeGalleryRequest();
                } else {
                    view().showMessage(R.string.read_permission_denied);
                }
        }
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

    private void makePlacePickerRequest() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            if (getActivity() != null)
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        viewModel.setImageUri(Uri.fromFile(photo));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    // endregion
}
