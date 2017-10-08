package im.goody.android.screens.near_events;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import io.reactivex.Observable;

@SuppressWarnings("MissingPermission")
public class NearEventsController extends BaseController<NearEventsView> implements OnMapReadyCallback {
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
                    if(googleMap != null) googleMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    // end

    // ======= region OnMapReadyCallback =======

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            googleMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST);
        }
        showMarkers();
        view().map().onResume();
    }

    // end


    // ======= region DI =======

    @Subcomponent
    @DaggerScope(NearEventsController.class)
    public interface Component {
        void inject(NearEventsController controller);
    }

    // end

    // ======= region private methods =======

    private void showMarkers() {
        Observable<Deal> observable;
        if (events != null) {
            observable = Observable.fromIterable(events);
        } else {
            observable = repository.getEvents()
                    .doOnNext(events -> this.events = events)
                    .flatMap(Observable::fromIterable);
        }
        observable.map(deal -> {
            Location location = deal.getLocation();
            return new MarkerOptions()
                    .position(location.toLatLng())
                    .title(deal.getTitle())
                    .snippet(DateUtils.getAbsoluteDate(deal.getEvent().getDate()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        })
                .forEach(googleMap::addMarker);
    }

    // end

}