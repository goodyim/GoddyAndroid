package im.goody.android.screens.new_post;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

import static android.app.Activity.RESULT_OK;

public class NewPostController extends BaseController<NewPostView> {
    private NewPostViewModel viewModel = new NewPostViewModel();

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int IMAGE_PICKER_REQUEST = 2;

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST = 2;

    private static final String[] LOCATION_PERMISSIONS = {permission.ACCESS_FINE_LOCATION};
    private static final String[] STORAGE_PERMISSIONS = {permission.READ_EXTERNAL_STORAGE};

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
        attachedView.setData(viewModel);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_post_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_create_post) {
            createPost();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void chooseLocation() {
        if (isPermissionGranted(permission.ACCESS_FINE_LOCATION)) {
            makePlacePickerRequest();
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }
    }

    void choosePhoto() {
        if (isPermissionGranted(permission.READ_EXTERNAL_STORAGE)){
            makeGalleryRequest();
        } else {
            requestPermissions(STORAGE_PERMISSIONS, STORAGE_PERMISSION_REQUEST);
        }
    }

    void clearLocation() {
        viewModel.setLocation(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(getActivity(), data);
                    viewModel.setLocation(place);
                }
                break;
            case IMAGE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        viewModel.setImage(uri);
                    }
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
                    attachedView.showSnackbarMessage(R.string.location_permission_denied);
                }
                break;
            case STORAGE_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeGalleryRequest();
                } else {
                    attachedView.showSnackbarMessage(R.string.read_permission_denied);
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

    private void createPost() {
        rootPresenter.showProgress(R.string.create_post_progress);
        disposable = repository.createPost(viewModel.body()).subscribe(
                result -> {
                    rootPresenter.hideProgress();
                    rootPresenter.showMainScreen();
                },
                error -> {
                    rootPresenter.hideProgress();
                    attachedView.showSnackbarMessage(error.getMessage());
                }
        );
    }

    private void makePlacePickerRequest() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void makeGalleryRequest() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/**");
        startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }

    // endregion
}
