package cafe.adriel.androidaudiorecorder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */

public class Respose {
    @SerializedName("code")
    private long  code;
    @SerializedName("messange")
    private String messange;

    public String getMessange() {
        return messange;
    }

    public long getCode() {
        return code;
    }
}
