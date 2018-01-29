package cafe.adriel.androidaudiorecorder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 20/12/2017.
 */

public class User {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
}
