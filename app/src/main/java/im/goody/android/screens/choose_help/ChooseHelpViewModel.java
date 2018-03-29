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

public class ChooseHelpViewModel {
    final List<String> tags;
    final PresetTag[] presetTags;
    public ObservableField<Location> place = new ObservableField<>(null);
    public ObservableBoolean isGeoEnabled = new ObservableBoolean(false);
    public ObservableInt radius = new ObservableInt(0);

    ChooseHelpViewModel() {
        tags = new ArrayList<>();

        presetTags = buildPresetTags();
    }

    ChooseHelpViewModel(HelpInfo info) {
        tags = info.getTags();

        presetTags = buildPresetTags();

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

        if (info.getArea() != null) {
            place.set(new Location(info.getArea().getLatitude(),
                    info.getArea().getLongitude(),
                    info.getArea().getAddress()));
            radius.set(info.getArea().getRadius());
        }

        isGeoEnabled.set(info.getArea() != null);
    }

    public HelpInfo body() {
        return new HelpInfo()
                .setTags(tags)
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
                    .setLongitude(coordinates.longitude);
        }
    }

    private PresetTag[] buildPresetTags() {
        String[] presetStrings = App.getAppContext().getResources().getStringArray(R.array.preset_tags);

        PresetTag[] presets = new PresetTag[presetStrings.length];
        for (int i = 0; i < presetStrings.length; i++) {
            presets[i] = new PresetTag(presetStrings[i]);
        }

        return presets;
    }

    public class PresetTag {
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
