package com.team03.dtuevent.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.mlkit.vision.barcode.Barcode;
import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.databinding.FragmentSearchBinding;
import com.team03.dtuevent.helper.Constant;
import com.team03.dtuevent.helper.HttpUtils;
import com.team03.dtuevent.helper.SharedPreferenceHelper;
import com.team03.dtuevent.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private boolean canRun = true;
    private Handler handler = new Handler();
    private String token;
    private String accountId;
    private String rawValue = "";
    private CustomDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        token = SharedPreferenceHelper.getSharedPreferenceString(getContext(), Constant.TOKEN_KEY, "");
        accountId = SharedPreferenceHelper.getSharedPreferenceString(getContext(), Constant.ACCOUNT_ID_KEY, "");
        dialog = new CustomDialog(getActivity(), new CustomDialog.OnInputDialogClicked() {
            @Override
            public void onOkiButton() {
                // TODO: lam gi thi chua biet
            }
        });
        return binding.getRoot();
    }


    private void attendance() {
        if ("".equals(rawValue)) return;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", rawValue);
            jsonObject.put("accountId", accountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(HttpUtils.getAbsoluteUrl("/api/event/addEventUser"))
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", String.format("Bearer %s", token))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String studentName = response.getString("name");
                            dialog.showConfirm(String.format("Hello %s", studentName), true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.showConfirm(String.format("Hello %s", rawValue), true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ducnvx", "onError" + anError.getErrorCode());
                        dialog.showConfirm(String.format("Attendance Failed %s", rawValue), false);
                    }
                });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.tvNotFond.setVisibility(View.GONE);
        binding.rlInfo.setVisibility(View.GONE);
        binding.btnAttend.setOnClickListener(vv -> {
            attendance();
        });
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                handleLocationSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (newText.length() > 3) {
//                    if (canRun) {
//                        canRun = false;
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                canRun = true;
//                                handleLocationSearch(newText);
//                            }
//                        }, 200);
//                    }
//                }
                return false;
            }
        });

    }

    private void handleLocationSearch(String newText) {
        showLoading();
        AndroidNetworking.get(HttpUtils.getAbsoluteUrl("/api/user/find/{code}"))
                .addPathParameter("code", newText)
                .addHeaders("Authorization", String.format("Bearer %s", token))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showView(false);
                        String code, name, klass, course, majors;
                        try {
                            name = response.getString("name");
                            code = response.getString("code");
                            klass = response.getJSONObject("classUser").getString("name");
                            course = response.getJSONObject("course").getString("name");
                            majors = response.getJSONObject("majors").getString("name");
                            showSearchResultSuccess(code, name, klass, course, majors);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Parse Json Error !!!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        showView(true);
                    }
                });
    }

    private void showView(boolean isShowNotFound) {
        binding.tvNotFond.setVisibility(isShowNotFound ? View.VISIBLE : View.GONE);
        binding.rlInfo.setVisibility(!isShowNotFound ? View.VISIBLE : View.GONE);
        binding.pgbLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.pgbLoading.setVisibility(View.VISIBLE);
        binding.tvNotFond.setVisibility(View.GONE);
        binding.rlInfo.setVisibility(View.GONE);
    }

    private void showSearchResultSuccess(String code, String name, String klass, String course, String majors) {
        binding.tvStName.setText(name);
        binding.tvStCode.setText(code);
        binding.tvStInfo.setText(String.format("%s - %s - %s", course, klass, majors));
        rawValue = code;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
    }
}