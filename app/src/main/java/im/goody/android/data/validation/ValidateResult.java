package im.goody.android.data.validation;

import java.util.ArrayList;
import java.util.List;

import im.goody.android.App;
import im.goody.android.R;

public class ValidateResult {
    private final List<Integer> errorResources = new ArrayList<>();

    public List<Integer> getErrorResources() {
        return errorResources;
    }

    public String getMessage() {
        StringBuilder builder = new StringBuilder();

        for(Integer errorRes: errorResources) {
            String error = App.getAppContext().getString(errorRes);
            builder.append("*").append(error).append("\n");
        }

        return builder.toString();
    }

    public boolean isValid() {
        return errorResources.isEmpty();
    }

    public void invalidate() {
        errorResources.add(R.string.invalid_fields_message);
    }
}
