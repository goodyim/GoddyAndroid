package im.goody.android.screens.near_events;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;

import com.google.android.gms.maps.MapView;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ScreenNearEventsBinding;

@SuppressWarnings("MissingPermission")
public class NearEventsView extends BaseView<NearEventsController, ScreenNearEventsBinding> {
    private BottomSheetBehavior behavior;

    public NearEventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    MapView map() {
        return binding.mapView;
    }

    @Override
    protected void onAttached() {
        behavior = BottomSheetBehavior.from(binding.markerPanel.getRoot());

        binding.markerPanel.mapBottomDetail.setOnClickListener(v -> {
            Deal event = binding.markerPanel.getEvent();
            controller.detailClicked(event);
        });

        hideInfoPanel();
    }


    @Override
    protected void onDetached() {
    }

    public void hideInfoPanel() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void showInfoPanel(Deal deal) {
        binding.markerPanel.setEvent(deal);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
