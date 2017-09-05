package im.goody.android.root;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.List;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.databinding.ActivityRootBinding;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.detail_post.DetailPostController;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.MenuItemHolder;

@SuppressWarnings("deprecation")
public class RootActivity extends AppCompatActivity
        implements IRootView, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    IRootPresenter presenter;

    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;
    private ActivityRootBinding binding;
    private Router router;
    private ProgressDialog progressDialog;

    //region ================= Life cycle =================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDaggerComponent();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_root);
        router = Conductor.attachRouter(this, binding.rootFrame, savedInstanceState);

        initToolBar();

        if (!router.hasRootController())
            showScreenAsRoot(presenter.getStartController());
    }

    private void initDaggerComponent() {
        RootComponent component = App.getRootComponent();
        if (component != null)
            component.inject(this);
    }

    private void initToolBar() {
        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();

        drawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        binding.navView.setNavigationItemSelectedListener(this);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false; // TODO: 21.08.2017
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((RootPresenter) presenter).takeView(this);
    }

    @Override
    protected void onStop() {
        ((RootPresenter) presenter).dropView();
        binding.navView.setNavigationItemSelectedListener(null);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (router.getBackstackSize() > 1) {
            router.popCurrentController();
        } else {
            super.onBackPressed();
        }
    }

    //endregion

    //region ================= IBarView =================

    @Override
    public void setToolbarTitle(Integer titleRes) {
        if (actionBar != null) {
            if (titleRes == null) {
                actionBar.setDisplayShowTitleEnabled(false);
            } else {
                actionBar.setDisplayShowTitleEnabled(true);
                String title = getResources().getString(titleRes);
                actionBar.setTitle(title);
            }
        }
    }

    @Override
    public void setToolBarVisible(boolean visible) {
        binding.toolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setHomeState(int state) {
        if (drawerToggle != null && actionBar != null) {
            switch (state) {
                case BarBuilder.HOME_ARROW:
                    drawerToggle.setDrawerIndicatorEnabled(false);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    if (drawerToggle.getToolbarNavigationClickListener() == null) {
                        drawerToggle.setToolbarNavigationClickListener(v -> onBackPressed());
                    }
                    break;
                case BarBuilder.HOME_HAMBURGER:
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    drawerToggle.setToolbarNavigationClickListener(null);
                    break;
                    case BarBuilder.HOME_GONE:
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        drawerToggle.setDrawerIndicatorEnabled(false);
                        drawerToggle.setToolbarNavigationClickListener(null);
            }

            binding.drawerLayout.setDrawerLockMode(
                    state == BarBuilder.HOME_HAMBURGER
                            ? DrawerLayout.LOCK_MODE_UNLOCKED
                            : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            drawerToggle.syncState();
        }
    }

    @Override
    public void setHomeListener(View.OnClickListener listener) {
        drawerToggle.setToolbarNavigationClickListener(listener);
    }

    @Override
    public void setStatusBarVisible(boolean visible) {
        if (visible) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void setToolBarMenuItem(List<MenuItemHolder> items) {
        // TODO: 21.08.2017
    }

    //endregion

    //region ================= IRootView =================

    @Override
    public void showScreen(Class<? extends Controller> controllerClass) {
        String tag = controllerClass.getName();

        Controller controller = router.getControllerWithTag(tag);
        if (controller == null) {
            controller = instantiateController(controllerClass);
        }
        router.pushController(RouterTransaction.with(controller).tag(tag));
    }

    @Override
    public void showScreenAsRoot(Class<? extends Controller> controllerClass) {
        String tag = controllerClass.getName();
        Controller controller = instantiateController(controllerClass);
        router.setRoot(RouterTransaction.with(controller).tag(tag));
    }

    @Override
    public void showDetailScreen(long id) {
        String tag = DetailPostController.class.getName() + id;

        Controller controller = router.getControllerWithTag(tag);

        if (controller == null)
            controller = new DetailPostController(id);

        router.pushController(RouterTransaction.with(controller).tag(tag));
    }

    @Override
    public void showProgress(@StringRes int titleRes) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setTitle(titleRes);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }
        }
    }

    //endregion

    private <C extends Controller> C instantiateController(Class<C> controllerClass) {
        try {
            return controllerClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
