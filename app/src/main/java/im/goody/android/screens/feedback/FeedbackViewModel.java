package im.goody.android.screens.feedback;

import java.util.List;

import im.goody.android.data.dto.Feedback;

public class FeedbackViewModel {
    private List<Feedback> data;

    public List<Feedback> getData() {
        return data;
    }

    public void setData(List<Feedback> data) {
        this.data = data;
    }
}
