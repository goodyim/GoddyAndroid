package im.goody.android.screens.near_events;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import dagger.Subcomponent;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.utils.DateUtils;
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;

@SuppressWarnings("MissingPermission")
public class NearEventsController extends BaseController<NearEventsView> implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private GoogleMap googleMap;
    private List<Deal> events;

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusNearEvents();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setTitleRes(R.string.near_events)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_near_events;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, Bundle savedState) {
        NearEventsView view = (NearEventsView) super.onCreateView(inflater, container, savedState);
        view.map().onCreate(null);
        view.map().onResume();
        view.map().getMapAsync(this);
        return view;
    }

    // end

    // ======= region lifecycle =======

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        view().map().onResume();
    }

    @Override
    protected void onDetach(@NonNull View view) {
        view().map().onPause();

        super.onDetach(view);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        view().map().onDestroy();

        super.onDestroyView(view);
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        view().map().onSaveInstanceState(outState);
        super.onSaveViewState(view, outState);
    }

    // end

    // ======= region permissions =======

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (googleMap != null) googleMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    // end

    // ======= region OnMapReadyCallback =======

    @Override
    public void onMapReady(GoogleMap map) {
        MapsInitializer.initialize(view().getContext());
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            googleMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }

        showMarkers();

        //view().map().onResume();

        googleMap.setOnInfoWindowClickListener(this);
    }

    // end


    // ======= region DI =======

    private void showMarkers() {
        Observable<Deal> observable;
        if (events != null) {
            observable = Observable.fromIterable(events);
        } else {
            observable = repository.getEvents()
                    .doOnNext(events -> this.events = events)
                    .flatMap(Observable::fromIterable);
        }
        observable
                .filter(deal -> !TextUtils.isEmpty(deal.getLocation().getLatitude()))
                .forEach(deal -> {
                    Location location = deal.getLocation();
                    MarkerOptions options = new MarkerOptions()
                            .position(location.toLatLng())
                            .title(TextUtils.getMarkerTitle(deal))
                            .snippet(DateUtils.getAbsoluteDate(deal.getEvent().getDate()));

                    googleMap.addMarker(options).setTag(deal.getId());
                });

        observable.doOnError(this::showError);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view().getContext());
                AlertDialog confirmDialog = dialogBuilder
                        .setMessage(view().getResources().getString(R.string.near_confirm))
                        .setCancelable(true)
                        .setPositiveButton(R.string.near_confirm_yes, (dialog, which) -> {
                            long id = (long) marker.getTag();
                            dialog.dismiss();
                            rootPresenter.showDetailScreen(id);
                        })
                        .setNegativeButton(R.string.near_confirm_no, (dialog, which) -> dialog.dismiss())
                        .create();

                confirmDialog.setCanceledOnTouchOutside(true);
                confirmDialog.show();

            });

        /*new ChooseImageOptionsDialog()
                .show(view().getContext())
                .subscribe(
                        integer -> {
                            rootPresenter.showLoginScreen();
                        }
                );*/
    }

    // end

    // ======= region private methods =======

    @Subcomponent
    @DaggerScope(NearEventsController.class)
    public interface Component {
        void inject(NearEventsController controller);
    }

    // end

}
