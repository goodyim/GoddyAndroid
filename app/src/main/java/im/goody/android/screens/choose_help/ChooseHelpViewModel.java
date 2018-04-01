package im.goody.android.screens.choose_help;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.dto.HelpInfo;
import im.goody.android.data.dto.Location;
import io.reactivex.annotations.NonNull;

public class ChooseHelpViewModel {
    final List<String> tags;
    final PresetTag[] presetTags = loadPresetTags();;
    public ObservableField<Location> place = new ObservableField<>(null);
    public ObservableBoolean isGeoEnabled = new ObservableBoolean(false);
    public ObservableInt radius = new ObservableInt(0);

    ChooseHelpViewModel() {
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

    private void movePredefinedTagsToPresets() {
        ListIterator<String> iterator = tags.listIterator();
        while (iterator.hasNext()) {
            String tag = iterator.next();
            for (PresetTag preset : presetTags) {
                if (tag.equals(preset.getValue())) {
                    iterator.remove();
                    preset.setChecked(true);
                    break;
                }
            }
        }
    }

    private List<String> buildTags() {
        List<String> result = new ArrayList<>(tags);

        for (PresetTag tag : presetTags) {
            if (tag.isChecked()) {
                result.add(tag.getValue());
            }
        }

        return result;
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

    private PresetTag[] loadPresetTags() {
        String[] presetStrings = App.getAppContext().getResources().getStringArray(R.array.preset_tags);

        PresetTag[] presets = new PresetTag[presetStrings.length];
        for (int i = 0; i < presetStrings.length; i++) {
            presets[i] = new PresetTag(presetStrings[i]);
        }

        return presets;
    }

    public static class PresetTag {
        private final String value;
        private boolean checked = false;

        PresetTag(String value) {
            this.value = value;
        }

        boolean isChecked() {
            return checked;
        }

        void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getValue() {
            return value;
        }
    }
}
