package im.goody.android.screens.intro.location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Location;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;

import static android.app.Activity.RESULT_OK;

public class LocationNotificationsController extends BaseController<LocationNotificationsView> {
    private ChooseHelpViewModel viewModel;

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 2;

    private static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    public LocationNotificationsController(ChooseHelpViewModel viewModel) {
        super();
        this.viewModel = viewModel;
    }

    public LocationNotificationsController(Bundle args) {
        super(args);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (viewModel != null) {
            view().setData(viewModel);
        }
    }

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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK && getActivity() != null && data != null) {
                    Place place = PlacePicker.getPlace(getActivity(), data);

                    viewModel.place.set(new Location(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            place.getAddress().toString()));
                }
                break;
        }
    }

    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusIntroLocation();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        // do nothing, we're in child controller
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.intro_location;
    }

    //endregion

    void chooseLocation() {
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            makePlacePickerRequest();
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }
    }

    //region ================= private methods =================

    private void makePlacePickerRequest() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            if (getActivity() != null)
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //

    //region ================= DI =================
    @dagger.Subcomponent()
    @DaggerScope(LocationNotificationsController.class)
    public interface Component {

        void inject(LocationNotificationsController controller);

    }
    //endregion
}
