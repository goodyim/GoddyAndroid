package im.goody.android.screens.common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.core.BaseView;
import im.goody.android.ui.dialogs.ChooseImageOptionsDialog;
import im.goody.android.ui.dialogs.OptionsDialog;
import im.goody.android.utils.FileUtils;

import static android.app.Activity.RESULT_OK;

public abstract class NewController<V extends BaseView> extends BaseController<V> {
    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int CAMERA_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    protected static final int CACHE_IMAGE_REQUEST = 3;

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST = 2;

    private static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    protected static final String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private OptionsDialog dialog = new ChooseImageOptionsDialog();

    protected String tempImageUrl;

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
                break;
            case CACHE_IMAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage(tempImageUrl);
                } else {
                    view().showMessage(R.string.cache_image_permission_denied);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK && getActivity() != null && data != null) {
                    Place place = PlacePicker.getPlace(getActivity(), data);
                    placeChanged(place);
                }
                break;
            case IMAGE_PICK_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();

                        imageUriChanged(uri);
                        imageChanged(uri);
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = getImageUri();
                    imageChanged(uri);
                }
        }
    }

    public void chooseLocation() {
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            makePlacePickerRequest();
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }
    }

    public void choosePhoto() {
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showDialog();
        } else {
            requestPermissions(STORAGE_PERMISSIONS, STORAGE_PERMISSION_REQUEST);
        }
    }

    public void clearPhoto() {
        imageChanged(null);
        imageUriChanged(null);
    }

    protected void loadImage(String url) {
        repository.cacheWebImage(url)
                .subscribe(uri -> {
                    tempImageUrl = null;
                    imageChanged(uri);
                    imageUriChanged(uri);
                });
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

        Uri uri = FileUtils.uriFromFile(photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                uri);

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        imageUriChanged(uri);

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    // end

    protected abstract Uri getImageUri();

    protected abstract void imageUriChanged(Uri uri);

    protected abstract void imageChanged(Uri uri);

    protected abstract void placeChanged(Place place);
}
