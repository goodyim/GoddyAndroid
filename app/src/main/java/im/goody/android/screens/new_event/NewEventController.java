package im.goody.android.screens.new_event;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.places.Place;

import dagger.Subcomponent;
import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.common.NewController;
import im.goody.android.ui.dialogs.DatePickDialog;
import im.goody.android.ui.dialogs.EditTextDialog;
import im.goody.android.ui.dialogs.TimePickDialog;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.NetUtils;
import im.goody.android.utils.TextUtils;
import im.goody.android.utils.UIUtils;


public class NewEventController extends NewController<NewEventView> {
    private NewEventViewModel viewModel;

    private Long id;

    public NewEventController(Deal deal) {
        id = deal.getId();
        viewModel = new NewEventViewModel(deal);

        if (!TextUtils.isEmpty(deal.getImageUrl())) {
            tempImageUrl = NetUtils.buildDealImageUrl(deal);

            if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                loadImage(tempImageUrl);
            } else {
                requestPermissions(STORAGE_PERMISSIONS, CACHE_IMAGE_REQUEST);
            }
        }
    }

    public NewEventController() {
         viewModel = new NewEventViewModel();
    }

    // ======= region NewEventController =======

    void chooseDate() {
        Context context = getActivity();
        DatePickDialog.with(context).show(viewModel.calendar.get())
                .subscribe(calendar -> {
                    viewModel.calendar.set(calendar);

                    TimePickDialog.with(context).show(calendar)
                            .subscribe(calendar1 -> viewModel.calendar.set(calendar1));
                });
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        setHasOptionsMenu(true);
        view().setData(viewModel);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_event_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            sendData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // end

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

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusNewEvent();
        if (component != null)
            component.inject(this);
    }


    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(id == null ? R.string.new_event_title : R.string.edit_event_title)
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
        return R.layout.screen_new_event;
    }

    void addTag() {
        new EditTextDialog(R.string.choose_tag_title).show(getActivity())
                .subscribe(tag -> {
                    if (!viewModel.tags.contains(tag)) {
                        viewModel.tags.add(tag);
                        view().addTag(tag);
                    } else {
                        view().showMessage(R.string.tag_already_present);
                    }
                });
    }

    void removeTag(String tag) {
        int position = viewModel.tags.indexOf(tag);
        viewModel.tags.remove(position);
        view().removeTag(position);
    }

    //endregion

    // ======= region DI =======

    @Subcomponent
    @DaggerScope(NewEventController.class)
    public interface Component {
        void inject(NewEventController controller);
    }

    //endregion

    // ======= region private methods =======

    private void sendData() {
        if (id != null) editEvent();
        else createEvent();
    }

    private void editEvent() {
        rootPresenter.showProgress(R.string.edit_post_progress);
        repository.editEvent(id, viewModel.body(), viewModel.getImageUri())
                .subscribe(
                        result -> {
                            rootPresenter.hideProgress();
                            rootPresenter.showMain();
                        },
                        error -> {
                            rootPresenter.hideProgress();
                            showError(error);
                        });
    }

    private void createEvent() {
        rootPresenter.showProgress(R.string.create_post_progress);
        repository.createEvent(viewModel.body(), viewModel.getImageUri())
                .subscribe(
                        result -> {
                            rootPresenter.hideProgress();
                            rootPresenter.showMain();
                        },
                        error -> {
                            rootPresenter.hideProgress();
                            showError(error);
                        });
    }

    // end
}
