package cafe.adriel.androidaudiorecorder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duy on 30/01/2018.
 */

public class StaticReponse {
    @SerializedName("code")
    private long code;
    @SerializedName("messange")
    private String messange;
    @SerializedName("data")
    private List<Day> data;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessange() {
        return messange;
    }

    public void setMessange(String messange) {
        this.messange = messange;
    }

    public List<Day> getData() {
        return data;
    }

    public void setData(List<Day> data) {
        this.data = data;
    }
}
