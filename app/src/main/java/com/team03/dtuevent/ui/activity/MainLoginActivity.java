package com.team03.dtuevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.team03.dtuevent.MainActivity;
import com.team03.dtuevent.R;
import com.team03.dtuevent.helper.Constant;
import com.team03.dtuevent.helper.HttpUtils;
import com.team03.dtuevent.helper.SharedPreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MainLoginActivity extends AppCompatActivity {


    private Button btnLogin;
    private EditText edtUname;
    private EditText edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        btnLogin = findViewById(R.id.btn_login);
        edtUname = findViewById(R.id.edt_uname);
        edtPassword = findViewById(R.id.edt_password);

        // kiem tra user da login hay chua
        String token = SharedPreferenceHelper.getSharedPreferenceString(this, Constant.TOKEN_KEY, "");

        if (!"".equals(token)) {
            navigateToMainActivity();
        }

        btnLogin.setOnClickListener(view -> {

//            String uname = edtUname.getText().toString();
//            String password = edtPassword.getText().toString();

            String uname = "cus";
            String password = "Ssy12345678@";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", uname);
                jsonObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.post(HttpUtils.getAbsoluteUrl("/auth/login"))
                    .addJSONObjectBody(jsonObject) // posting json
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                String tokenRes = response.getString("jwtToken");
                                String accountId = response.getJSONObject("account").getString("accountId");
                                SharedPreferenceHelper.setSharedPreferenceString(MainLoginActivity.this, Constant.TOKEN_KEY, tokenRes);
                                SharedPreferenceHelper.setSharedPreferenceString(MainLoginActivity.this,  Constant.ACCOUNT_ID_KEY, accountId);
                                navigateToMainActivity();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            Toast.makeText(MainLoginActivity.this, "Login Fail!!!", Toast.LENGTH_LONG).show();
                        }
                    });

            // login thanh cong di
        });
    }

    void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}