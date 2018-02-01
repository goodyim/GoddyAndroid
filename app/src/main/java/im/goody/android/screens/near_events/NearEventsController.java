package im.goody.android.screens.near_events;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;

import static com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import static com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

@SuppressWarnings("MissingPermission")
public class NearEventsController extends BaseController<NearEventsView>
        implements OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener {

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
        view.map().onCreate(savedState);
        view.map().getMapAsync(this);

        setHasOptionsMenu(true);
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

    // ======= region Menu =======

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.near_events_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            view().hideInfoPanel();
            refreshMarkers();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // end

    // ======= region Callbacks =======

    @Override
    public void onMapReady(GoogleMap map) {
        MapsInitializer.initialize(view().getContext());
        googleMap = map;

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            googleMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }

        view().map().onResume();

        showMarkers();

        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        view().showInfoPanel((Deal) marker.getTag());

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        view().hideInfoPanel();
    }

    void detailClicked(Deal event) {
        if (event != null)
            rootPresenter.showDetailScreen(event.getId());
    }

    // end

    // ======= region private methods =======

    private void refreshMarkers() {
        googleMap.clear();

        repository.getEvents().subscribe(events -> {
            this.events = events;
            showMarkers();
        }, this::showError);
    }

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
                .subscribe(deal -> {
                    Location location = deal.getLocation();
                    MarkerOptions options = new MarkerOptions()
                            .position(location.toLatLng());

                    googleMap.addMarker(options).setTag(deal);
                }, this::showError);
    }

    // end

    // ======= region DI =======

    @Subcomponent
    @DaggerScope(NearEventsController.class)
    public interface Component {
        void inject(NearEventsController controller);
    }

    // end
}
