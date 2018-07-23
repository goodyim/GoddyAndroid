package im.goody.android.data.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import im.goody.android.App;
import im.goody.android.R;

public class ValidateResult {
    private final List<Integer> errorResources = new ArrayList<>();

    private static final String MULTIPLE_ERROR_FORMAT = "%d. %s\n";

    public List<Integer> getErrorResources() {
        return errorResources;
    }

    public void addError(Integer errorRes) {
        errorResources.add(errorRes);
    }

    public String getMessage() {
        if (errorResources.size() == 1) {
            Integer errorRes = errorResources.get(0);
            return App.getAppContext().getString(errorRes);
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < errorResources.size(); i++) {
            Integer errorRes = errorResources.get(i);
            String error = App.getAppContext().getString(errorRes);
            builder.append(String.format(Locale.getDefault(), MULTIPLE_ERROR_FORMAT, i + 1, error));
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
