package com.team03.dtuevent.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.team03.dtuevent.R;
import com.team03.dtuevent.database.HistoryDao;
import com.team03.dtuevent.databinding.SingleHistoryItemBinding;
import com.team03.dtuevent.helper.Constant;
import com.team03.dtuevent.helper.HttpUtils;
import com.team03.dtuevent.helper.SharedPreferenceHelper;
import com.team03.dtuevent.helper.StudentAttendDto;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
//    private List<CodeMemento> data;
    private List<StudentAttendDto> data;
    private HistoryFragment.DismissLoadingListener listener;

    public HistoryAdapter(HistoryDao dao, Context context, HistoryFragment.DismissLoadingListener listener) {
        this.listener = listener;
        String accountId = SharedPreferenceHelper.getSharedPreferenceString(context, Constant.ACCOUNT_ID_KEY, "");
        String token = SharedPreferenceHelper.getSharedPreferenceString(context, Constant.TOKEN_KEY, "");
        AndroidNetworking.get(HttpUtils.getAbsoluteUrl("/api/event/userByAccount"))
                .addHeaders("Authorization", String.format("Bearer %s", token))
                .addQueryParameter("accountId", accountId)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<StudentAttendDto> arr = new ArrayList<>();
                        String code, name, klass;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                name = response.optJSONObject(i).getString("name");
                                code = response.optJSONObject(i).getString("code");
                                klass = response.optJSONObject(i).getJSONObject("classUser").getString("name");
                                arr.add(new StudentAttendDto(code, name, klass));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        invalidate(arr);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Call API Error", Toast.LENGTH_LONG).show();
                    }
                });
        data = new ArrayList<>();
    }

    public void invalidate(List<StudentAttendDto> newData) {
        data = newData;
        notifyDataSetChanged();
        listener.dismiss();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleHistoryItemBinding binding = SingleHistoryItemBinding.bind(holder.itemView);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
//        binding.historyItemDate.setText(dateFormat.format(data.get(position).getDate()));
//        binding.historyItemSummary.setText(data.get(position).getDisplayData());
//        binding.historyItemType.setText(data.get(position).getDataType().getTypeName());
        binding.historyItemDate.setText(data.get(position).code);
        binding.historyItemSummary.setText( data.get(position).klass);
        binding.historyItemType.setText(data.get(position).name);

        // Display UI for showing Code information
//        holder.itemView.setOnClickListener(v -> {
//            CodeMemento code = data.get(position);
//            new ResultDisplayDialog (holder.itemView.getContext(), Code.fromHistoryElement(code)).show();
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
