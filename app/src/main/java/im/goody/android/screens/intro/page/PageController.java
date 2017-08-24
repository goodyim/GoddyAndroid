package im.goody.android.screens.intro.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.R;
import im.goody.android.ui.helpers.BundleBuilder;

public class PageController extends Controller {
    private static final String PAGE_KEY = "PageController.page";

    public PageController(Page page) {
        this(new BundleBuilder(new Bundle())
                .putParcelable(PAGE_KEY, page)
                .build());
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public PageController(Bundle args) {
        super(args);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        Page page = getArgs().getParcelable(PAGE_KEY);
        ((PageView) view).setPage(page);
        ((PageView) view).takeController(this);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        ((PageView) view).dropController();
        super.onDetach(view);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.item_intro, container, false);
    }
}
