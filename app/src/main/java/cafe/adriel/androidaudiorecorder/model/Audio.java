package cafe.adriel.androidaudiorecorder.model;

import com.google.gson.annotations.SerializedName;


public class Audio {
    @SerializedName("id")
    private long id;
    @SerializedName("content")
    private String content;
    @SerializedName("code")
    private String code;
    @SerializedName("text_ord")
    private String text_ord;
    @SerializedName("reasonMessage")
    private String reasonMessage;
    @SerializedName("toatalText")
    private String toatalText;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText_ord() {
        return text_ord;
    }

    public void setText_ord(String text_ord) {
        this.text_ord = text_ord;
    }

    public String getReasonMessage() {
        return reasonMessage;
    }

    public void setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

    public String getToatalText() {
        return toatalText;
    }

    public void setToatalText(String toatalText) {
        this.toatalText = toatalText;
    }
}

