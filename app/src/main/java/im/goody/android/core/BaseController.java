package im.goody.android.core;

import android.support.annotation.NonNull;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;
import im.goody.android.di.components.RootComponent;
import im.goody.android.root.IRootPresenter;

public abstract class BaseController extends Controller {

    @Inject
    protected IRepository repository;
    @Inject
    protected IRootPresenter rootPresenter;

    public BaseController() {
        RootComponent rootComponent = App.getRootComponent();
        if (rootComponent != null) {
            initDaggerComponent(rootComponent);

        }
    }

    protected abstract void initDaggerComponent(RootComponent parentComponent);

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        ((BaseView) view).takeController(this);
        initActionBar();
    }

    protected abstract void initActionBar();

    @Override
    protected void onDetach(@NonNull View view) {
        ((BaseView) view).dropController();
        super.onDetach(view);
    }
}
