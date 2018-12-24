package im.goody.android.screens.finish_event;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenFinishEventBinding;

public class FinishEventView extends BaseView<FinishEventController, ScreenFinishEventBinding> implements FinishEventAdapter.Handler {

    public FinishEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(new FinishEventAdapter(this));

        binding.finishEventSend.setOnClickListener(v -> controller.send());
    }

    void setData(FinishEventViewModel viewModel) {
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onDetached() {

    }

    @Override
    public void removeParticipant(int position) {
        controller.removeParticipant(position);
    }

    @Override
    public void showProfile(long id) {
        controller.showProfile(id);
    }
}
