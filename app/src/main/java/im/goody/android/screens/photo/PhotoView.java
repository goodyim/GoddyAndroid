package im.goody.android.screens.photo;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenPhotoBinding;

public class PhotoView extends BaseView<PhotoController, ScreenPhotoBinding> {
    private boolean isToolbarHidden = false;

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setUrl(String url) {
        binding.setUrl(url);
    }

    @Override
    protected void onAttached() {
        binding.photoView.setOnClickListener(v -> {
            float alpha = isToolbarHidden ? 1.0f : 0;
            binding.photoBack.animate().alpha(alpha).start();
            binding.photoTitle.animate().alpha(alpha).start();
            isToolbarHidden = !isToolbarHidden;
        });
        binding.photoBack.setOnClickListener(v -> controller.finish());
    }

    @Override
    protected void onDetached() {
    }
}
