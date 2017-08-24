package im.goody.android.screens.intro.page;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;

public class Page implements Parcelable{
    private String title;
    private String description;
    private int icon;

    @ColorRes
    private int color;

    private Page() {}

    // ======= region getters =======

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }

    //endregion

    // ======= region Parcelable =======

    public Page(Parcel in) {
        String[] strings = new String[2];
        int[] ints = new int[2];

        in.readStringArray(strings);
        in.readIntArray(ints);

        title = strings[0];
        description = strings[1];

        icon = ints[0];
        color = ints[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] { title, description });
        parcel.writeIntArray(new int[] { icon, color });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    // endregion

    public static class Builder {
        private Page page;

        public Builder() {
            page = new Page();
        }

        public Builder setTitle(String title) {
            page.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            page.description = description;
            return this;
        }

        public Builder setIcon(int icon) {
            page.icon = icon;
            return this;
        }

        public Builder setColor(int color) {
            page.color = color;
            return this;
        }

        public Page build() {
            return page;
        }
    }
}
