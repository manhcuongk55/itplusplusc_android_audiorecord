package cafe.adriel.androidaudiorecorder.model;

import com.google.gson.annotations.SerializedName;


public class AudioResponse {
    @SerializedName("code")
    private long code;
    @SerializedName("data")
    private Audio data;
    @SerializedName("messange")
    private String messange;

    public Audio getAudio() {
        return data;
    }

    public long getCode() {
        return code;
    }

    public String getMessange() {
        return messange;
    }
}
