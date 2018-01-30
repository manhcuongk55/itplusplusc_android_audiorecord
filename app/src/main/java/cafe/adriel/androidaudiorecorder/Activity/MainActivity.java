package cafe.adriel.androidaudiorecorder.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.common.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.common.Const;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioResponse;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cafe.adriel.androidaudiorecorder.model.User;
import cafe.adriel.androidaudiorecorder.rest.ApiClient;
import cafe.adriel.androidaudiorecorder.rest.ApiInterface;
import cafe.adriel.androidaudiorecorder.storage.StorageManager;
import cafe.adriel.androidaudiorecorder.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        /*if (!validate()) {
            onLoginFailed("Bad form");
            return;
        }*/
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        //String email = _emailText.getText().toString();
        //String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        HashMap<String, String> map = new HashMap<>();
        map.put("email", "dmcksclc5@gmail.com");
        map.put("password","123456");
        Call<User> call = apiService.getTokenAuthen(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                int statusCode = response.code();
                if(statusCode == 200){
                    if(response.body().getToken() != null){
                        StorageManager.setStringValue(getApplicationContext(), Const.TOKEN, response.body().getToken());
                        onLoginSuccess();
                    }else{
                        onLoginFailed("token null");
                    }
                }else{
                    onLoginFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());

                onLoginFailed(t.toString());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
        recordAudio();

    }

    public void onLoginFailed(String statusCode) {
        Toast.makeText(getBaseContext(), statusCode, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public void recordAudio(){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String token = "Bearer " + StorageManager.getStringValue(getApplicationContext(),Const.TOKEN,"");
        Call<AudioResponse> call = apiService.getContent(0+"", token);
        final AndroidAudioRecorder builder = new AndroidAudioRecorder(this);
        builder.setFilePath(AUDIO_FILE_PATH)
                .setColor(ContextCompat.getColor(this, R.color.recorder_bg))
                .setRequestCode(REQUEST_RECORD_AUDIO)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(false)
                .setKeepDisplayOn(true);
        call.enqueue(new Callback<AudioResponse>() {
            @Override
            public void onResponse(Call<AudioResponse> call, Response<AudioResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200){
                    if(response.body().getAudio() != null){
                        StorageManager.setStringValue(getApplicationContext(),Const.ID_AUDIO,response.body().getAudio().getId()+"");
                        builder.setSub(response.body().getAudio().getContent());
                        builder.record();
                    }else{
                        builder.setSub("Sorry content null");
                        builder.record();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"error getContent :"+ statusCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudioResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

}
