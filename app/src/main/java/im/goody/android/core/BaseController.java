package im.goody.android.core;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.RestoreViewOnCreateController;

import java.util.Locale;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;
import im.goody.android.data.network.error.StandardError;
import im.goody.android.data.validation.ValidateResult;
import im.goody.android.di.components.RootComponent;
import im.goody.android.root.IRootPresenter;
import io.reactivex.disposables.CompositeDisposable;

import static im.goody.android.Constants.Pattern.OPEN_MAP;

public abstract class BaseController<V extends BaseView> extends RestoreViewOnCreateController {

    @Inject
    protected IRepository repository;
    @Inject
    protected IRootPresenter rootPresenter;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected BaseController() {
        initDagger();
    }

    public BaseController(Bundle args) {
        super(args);
        initDagger();
    }

    protected abstract void initDaggerComponent(RootComponent parentComponent);

    @SuppressWarnings("unchecked")
    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().takeController(this);
        initActionBar();
        rootPresenter.launched();
    }

    protected abstract void initActionBar();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, Bundle savedState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    protected abstract @LayoutRes int getLayoutResId();

    @Override
    protected void onDetach(@NonNull View view) {
        view().dropController();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = new CompositeDisposable();
        }
        super.onDetach(view);
    }

    // ======= region common methods =======

    @SuppressWarnings("unchecked")
    protected V view() {
        return (V) getView();
    }

    protected void showError(Throwable error) {
        if (view() != null)
            view().showMessage(getErrorMessage(error));
    }

    protected void showError(ValidateResult result) {
        if (view() != null)
            view().showMessage(result.getMessage());
    }

    protected String getErrorMessage(Throwable error) {
        StandardError errorObject = repository.getError(error, StandardError.class);
        return errorObject != null ? errorObject.getErrors() : error.getMessage();
    }

    protected void share(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(shareIntent);
    }

    protected void openMap(String address) {
        String uri = String.format(Locale.ENGLISH, OPEN_MAP, address);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    protected void showToast(@StringRes int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT)
                .show();
    }

    // endregion

    //======= region Permissions =======

    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(App.getAppContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    //endregion

    // ======= region private methods =======

    private void initDagger() {
        RootComponent rootComponent = App.getRootComponent();
        if (rootComponent != null) {
            initDaggerComponent(rootComponent);
        }
    }

    // endregion
}
