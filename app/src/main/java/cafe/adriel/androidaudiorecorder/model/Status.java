package cafe.adriel.androidaudiorecorder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 20/12/2017.
 */

public class Status {
    @SerializedName("status")
    private String status;
    public Status(String status){
        this.status = status;
    }
    public String getstatus() {
        return status;
    }
}
