package im.goody.android.screens.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.dto.PresetTag;

public abstract class TagViewModel {
    public List<String> tags;
    public final PresetTag[] presetTags = loadPresetTags();

    public void addTags(String rawTags) {
        rawTags = rawTags.replace(", ", ",");
        String[] newTags = rawTags.split(",|\\s");
        tags.addAll(Arrays.asList(newTags));

        Set<String> set = new LinkedHashSet<>(tags);
        tags.clear();
        tags.addAll(set);

        movePredefinedTagsToPresets();
    }

    protected void movePredefinedTagsToPresets() {
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

    protected List<String> buildTags() {
        List<String> result = new ArrayList<>(tags);

        for (PresetTag tag : presetTags) {
            if (tag.isChecked()) {
                result.add(tag.getValue());
            }
        }

        return result;
    }

    private PresetTag[] loadPresetTags() {
        String[] presetStrings = App.getAppContext().getResources().getStringArray(R.array.preset_tags);

        PresetTag[] presets = new PresetTag[presetStrings.length];
        for (int i = 0; i < presetStrings.length; i++) {
            presets[i] = new PresetTag(presetStrings[i]);
        }

        return presets;
    }
}
