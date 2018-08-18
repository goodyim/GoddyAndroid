package im.goody.android.screens.choose_help;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import dagger.Subcomponent;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Location;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.dialogs.EditTextDialog;
import im.goody.android.ui.dialogs.ErrorDialog;
import im.goody.android.ui.dialogs.InfoDialog;
import im.goody.android.ui.dialogs.OptionsDialog;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.BundleBuilder;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

public class ChooseHelpController extends BaseController<ChooseHelpView> {
    public static final int MODE_SETUP = 0;
    public static final int MODE_EDIT = 1;
    private static final String MODE_KEY = "ChooseHelpController.mode";

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 2;

    private static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private ChooseHelpViewModel viewModel;

    private ErrorDialog errorDialog = new ErrorDialog(R.string.help_info_load_error);


    public ChooseHelpController(Integer mode) {
        this(new BundleBuilder()
                .putInt(MODE_KEY, mode)
                .build());
    }

    public ChooseHelpController(Bundle args) {
        super(args);
    }

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusChooseHelp();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(false)
                .setStatusBarVisible(false)
                .setHomeState(BarBuilder.HOME_GONE)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_choose_help;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (viewModel == null) {
            if (getMode() == MODE_SETUP) {
                viewModel = new ChooseHelpViewModel();
                view().setData(viewModel);
            } else {
                loadInfo();
            }
        } else {
            view().setData(viewModel);
        }

        if (getMode() == MODE_EDIT) {
            view().hideSkip();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showLocationDialog();
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

    void addTag() {
        Disposable d = new EditTextDialog(R.string.choose_tag_title).show(getActivity())
                .subscribe(tags -> {
                    viewModel.addTags(tags);
                    view().setTags(viewModel.tags);
                });
        compositeDisposable.add(d);
    }

    void removeTag(String tag) {
        int position = viewModel.tags.indexOf(tag);
        viewModel.tags.remove(position);
        view().removeTag(position);
    }

    void submit() {
        rootPresenter.showProgress(R.string.help_info_updating);
        Disposable d = repository.updateHelpInfo(viewModel.body())
                .subscribe(res -> {
                    rootPresenter.hideProgress();
                    if (getMode() == MODE_SETUP) {
                        rootPresenter.showMain();
                    } else {
                        if (getActivity() != null) getActivity().onBackPressed();
                    }
                }, error -> {
                    rootPresenter.hideProgress();
                    showError(error);
                });
        compositeDisposable.add(d);
    }

    void chooseLocation() {
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationDialog();
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }
    }

    void skip() {
        rootPresenter.showMain();
    }

    private void loadInfo() {
        rootPresenter.showProgress(R.string.help_info_loading);
        Disposable d = repository.loadHelpInfo()
                .subscribe(info -> {
                    rootPresenter.hideProgress();
                    viewModel = new ChooseHelpViewModel(info);
                    view().setData(viewModel);
                }, error -> {
                    rootPresenter.hideProgress();
                    showErrorDialog();
                });
        compositeDisposable.add(d);
    }

    private void showErrorDialog() {
        errorDialog.dismiss();

        Disposable d = errorDialog.show(getActivity())
                .subscribe(button -> {
                    if (button == InfoDialog.BUTTON_CANCEL) {
                        if (getMode() == MODE_SETUP) {
                            rootPresenter.showMain();
                        } else {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                    } else {
                        loadInfo();
                    }
                });
        compositeDisposable.add(d);
    }

    private void showLocationDialog() {
        Disposable disposable = new OptionsDialog(R.array.location_options)
                .show(getActivity())
                .subscribe(item -> {
                    switch (item) {
                        case 0:
                            setLocationToCurrent();
                            break;
                        case 1:
                            makePlacePickerRequest();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setLocationToCurrent() {
        view().showLocationFindingText();

        Disposable d = repository.getLastLocation()
                .subscribe(location -> viewModel.place.set(location),
                        error -> {
                            showMessage(R.string.location_find_error);
                            viewModel.place.set(new Location());
                        });

        compositeDisposable.add(d);
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

    private int getMode() {
        return getArgs().getInt(MODE_KEY);
    }

    // ======= region DI =======

    @Subcomponent
    @DaggerScope(ChooseHelpController.class)
    public interface Component {
        void inject(ChooseHelpController controller);
    }

    //endregion
}
