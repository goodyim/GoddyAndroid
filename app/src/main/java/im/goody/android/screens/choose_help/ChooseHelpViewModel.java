package im.goody.android.screens.choose_help;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.dto.HelpInfo;
import im.goody.android.data.dto.Location;
import im.goody.android.data.dto.PresetTag;
import im.goody.android.screens.common.TagViewModel;
import io.reactivex.annotations.NonNull;

public class ChooseHelpViewModel extends TagViewModel {
    public ObservableField<Location> place = new ObservableField<>();
    public ObservableBoolean isGeoEnabled = new ObservableBoolean(false);
    public ObservableInt radius = new ObservableInt(0);

    public ChooseHelpViewModel() {
        tags = new ArrayList<>();
    }

    ChooseHelpViewModel(@NonNull HelpInfo info) {
        tags = info.getTags() != null ? info.getTags() : new ArrayList<>();

        movePredefinedTagsToPresets();

        HelpInfo.Area area = info.getArea();
        boolean areaValid = area != null && area.getLatitude() != 0.0 && area.getLongitude() != 0.0;
        if (areaValid) {
            place.set(new Location(info.getArea().getLatitude(),
                    info.getArea().getLongitude(),
                    info.getArea().getAddress()));
            radius.set(info.getArea().getRadius());
        }

        isGeoEnabled.set(areaValid);
    }

    public HelpInfo body() {
        return new HelpInfo()
                .setTags(buildTags())
                .setArea(buildArea());
    }

    private HelpInfo.Area buildArea() {
        if (!isGeoEnabled.get() || place.get() == null) {
            return null;
        } else {
            LatLng coordinates = place.get().toLatLng();
            return new HelpInfo.Area()
                    .setAddress(place.get().getAddress())
                    .setLatitude(coordinates.latitude)
                    .setLongitude(coordinates.longitude)
                    .setRadius(radius.get());
        }
    }
}
