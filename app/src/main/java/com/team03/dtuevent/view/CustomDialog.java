package com.team03.dtuevent.view;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.team03.dtuevent.R;

public class CustomDialog {

    private final Activity activity;
    private AlertDialog alertDialog;

    private final OnInputDialogClicked listener;


    public CustomDialog(Activity mActivity, OnInputDialogClicked mListener) {
        activity = mActivity;
        listener = mListener;
    }

    public void showConfirm(String content, boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_custom_dialog, null);
        builder.setView(dialogView);

        final Button btnOk = dialogView.findViewById(R.id.btnInputDialogOK);
        final ImageView imgDismiss = dialogView.findViewById(R.id.imgDismiss);
        final ImageView imgBg = dialogView.findViewById(R.id.img_bg);
        final TextView tvTitle = dialogView.findViewById(R.id.tvTitle);

        imgBg.setImageResource(isSuccess ? R.drawable.ic_success: R.drawable.ic_fail);

        tvTitle.setText(content);
        imgDismiss.setOnClickListener(view -> dismiss());

        btnOk.setOnClickListener(btnView -> {
            listener.onOkiButton();
            dismiss();
        });
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }


    public interface OnInputDialogClicked {
        void onOkiButton();

        default void clearTimer() {
            Log.d("ducnvx", "InputDialog::clearTimer");
        }
    }
}
