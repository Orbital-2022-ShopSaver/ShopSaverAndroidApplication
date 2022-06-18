package com.example.shopsaverandroidapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment2 extends Fragment {

    EditText editTextUsername, editTextPassword;

    public LoginFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsername = (EditText) view.findViewById(R.id.login_username);
        editTextPassword = (EditText) view.findViewById(R.id.login_password);

        final Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccount(editTextUsername.getText().toString());
            }
        });

        return view;
    }

    private void getAccount(String username) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"collection\":\"userCredential\"," +
                "\n    \"database\":\"android_app\"," +
                "\n    \"dataSource\":\"Cluster0\"," +
                "\n    \"filter\" : {\"username\":\"" + username + "\"}" +
                "\n" +
                "\n}");
        Request request = new Request.Builder()
                .url("https://data.mongodb-api.com/app/data-cxopk/endpoint/data/beta/action/findOne")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "RdWDOUN0AlYTnWxWfIp5nsDNXRP6UShcIyhB9b4rhgRVst13mH2Fn34JWFRFzU90")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("test", e.toString());
                Toast.makeText(getActivity(), "failed request", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("test", "onResponse: " + response.body().string());
                authenticate(response.body().string());
            }
        });
    }

    private void authenticate(String accInfo) {
        if (editTextPassword.getText().toString().equals("")) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "login failed", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        String password = null;
        try {
            JSONObject acc = new JSONObject(accInfo);
            password = acc.getJSONObject("document").getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (password != null && editTextPassword.getText().toString().equals(password)) {
            // login successfully
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, PassFragment.class, null);
            fragmentTransaction.commit();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}