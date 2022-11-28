package com.team03.dtuevent.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.team03.dtuevent.R;
import com.team03.dtuevent.App;
import com.team03.dtuevent.database.HistoryDao;
import com.team03.dtuevent.database.HistoryDatabase;
import com.team03.dtuevent.databinding.FragmentHistoryBinding;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryAdapter adapter;
    private HistoryDao dao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        dao = HistoryDatabase.getInstance(getContext()).historyDao();

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.clearHistory) {
            App.globalExService.submit(() -> {
                dao.clearAll();
                // Invalidate the list.
                requireView().post(() -> adapter.invalidate(new ArrayList<>()));
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.historyList.setLayoutManager(layoutManager);
        App.globalExService.submit(() -> {
            adapter = new HistoryAdapter(dao, getContext(), new DismissLoadingListener() {
                @Override
                public void dismiss() {
                    dismissLoading();
                }
            });
            view.post(() -> binding.historyList.setAdapter(adapter));
        });
    }

    public void dismissLoading(){
        binding.pgbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.historyList.setAdapter(null);
        binding = null;
    }

   public interface DismissLoadingListener{
        void dismiss();
    }

}