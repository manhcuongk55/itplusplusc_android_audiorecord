package cafe.adriel.androidaudiorecorder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.model.Respose;
import cafe.adriel.androidaudiorecorder.rest.ApiClient;
import cafe.adriel.androidaudiorecorder.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.radioGrp)
    RadioGroup _group;
    @BindView(R.id.input_province)
    AutoCompleteTextView _namerovince;
    @BindView(R.id.input_age)
    EditText _nameAge;

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_address)
    EditText _addressText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;


    private static final String[] COUNTRIES = new String[]{
            "0", "Quảng Nam", "Quảng Ngãi", "Đà Nẵng", "Bình Định", "Hà Nội", "Phú Thọ", "Đắk Lắk", "Sơn La", "Hà Nam", "Bắc Giang", "Yên Bái", "Gia Lai", "Khánh Hòa", "Cần Thơ", "Đồng Tháp", "Hưng Yên", "Kiên Giang", "Thái Bình", "Ninh Bình", "Lạng Sơn", "Vĩnh Long", "Tiền Giang", "Hậu Giang", "Bình Phước", "Thanh Hóa", "Vĩnh Phúc", "Hòa Bình", "Tuyên Quang", "Thái Nguyên", "Hải Dương", "Đồng Nai", "Lâm Đồng", "Hồ Chí Minh", "Thừa Thiên Huế", "Long An", "Phú Yên", "Kon Tum", "Tây Ninh", "Bình Dương", "Bình Thuận", "Bà Rịa-Vũng Tàu", "Quảng Ninh", "Lai Châu", "Bắc Ninh", "Sóc Trăng", "Bạc Liêu", "Hà Giang", "Hải Phòng", "Cao Bằng", "Bắc Kạn", "Đắk Nông", "An Giang", "Bến Tre", "Trà Vinh", "Lào Cai", "Nam Định", "Ninh Thuận", "Quảng Trị", "Quảng Bình", "Nghệ An", "Hà Tĩnh", "Cà Mau", "Điện Biên"
    };

    private static int numvince, gens;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        _namerovince.setAdapter(adapter);


        _group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              @Override
                                              public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                  gens = checkedId;
                                                  RadioButton radioButton = (RadioButton) findViewById(checkedId);
                                                  Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
        );


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String age = _nameAge.getText().toString();
        // TODO: Implement your own signup logic here.


        ApiInterface apiService2 =
                null;
        try {
            apiService2 = ApiClient.getClient(this).create(ApiInterface.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", name);
        map.put("fullName", address);
        map.put("passWord", password);
        map.put("timeRecorder", "30");
        map.put("email", email);
        map.put("province", numvince + "");
        map.put("old", age);
        map.put("gender", gens + "");

        Call<Respose> call2 = apiService2.register(map);

        call2.enqueue(new Callback<Respose>() {
            @Override
            public void onResponse(Call<Respose> call, Response<Respose> response1) {
                int statusCode = response1.code();

                if (statusCode == 200) {
                    String mesa = response1.body().getMessange();
                    Toast.makeText(getApplicationContext(), "result :" + mesa, Toast.LENGTH_LONG).show();
                    Log.e(TAG, statusCode + "");
                } else {
                    Toast.makeText(getApplicationContext(), "error :" + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Respose> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //  finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String province = _namerovince.getText().toString();
        String age = _nameAge.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty()) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (age.isEmpty()) {
            _nameAge.setError("Enter Valid Age");
            valid = false;
        } else {
            _nameAge.setError(null);
        }

        if (province.isEmpty()) {
            _namerovince.setError("Enter Valid Province");
            valid = false;
        } else {
            for (int i = 0; i < COUNTRIES.length; i++) {
                if (province.equals(COUNTRIES[i])) {
                    numvince = i;
                }
            }
            _namerovince.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}