package com.team03.dtuevent.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team03.dtuevent.R;
import com.team03.dtuevent.Utils;
import com.team03.dtuevent.database.CodeMemento;
import com.team03.dtuevent.database.HistoryDao;
import com.team03.dtuevent.databinding.SingleHistoryItemBinding;
import com.team03.dtuevent.objects.Code;
import com.team03.dtuevent.ui.ResultDisplayDialog;

import java.text.DateFormat;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<CodeMemento> data;


    public HistoryAdapter(HistoryDao dao) {
        data = Utils.getAllSortLatest(dao.getAll());
    }

    public void invalidate(List<CodeMemento> newData) {
        data = newData;
        notifyDataSetChanged();
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
        binding.historyItemDate.setText(dateFormat.format(data.get(position).getDate()));
        binding.historyItemSummary.setText(data.get(position).getDisplayData());
        binding.historyItemType.setText(data.get(position).getDataType().getTypeName());

        // Display UI for showing Code information
        holder.itemView.setOnClickListener(v -> {
            CodeMemento code = data.get(position);
            new ResultDisplayDialog (holder.itemView.getContext(), Code.fromHistoryElement(code)).show();
        });
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
