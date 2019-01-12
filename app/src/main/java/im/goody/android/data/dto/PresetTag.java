package im.goody.android.data.dto;

public class PresetTag {
    private final String value;
    private boolean checked = false;

    public PresetTag(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getValue() {
        return value;
    }
}
