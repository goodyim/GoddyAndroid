package im.goody.android.screens.near_events;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.gms.maps.MapView;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenNearEventsBinding;

@SuppressWarnings("MissingPermission")
public class NearEventsView extends BaseView<NearEventsController, ScreenNearEventsBinding> {
    public NearEventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    MapView map() {
        return binding.mapView;
    }

    @Override
    protected void onAttached() {
    }

    @Override
    protected void onDetached() {

    }

}
