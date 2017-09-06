package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("comment")
public class NewCommentReq {
    private String content;

    public String getContent() {
        return content;
    }

    public NewCommentReq setContent(String content) {
        this.content = content;
        return this;
    }
}
