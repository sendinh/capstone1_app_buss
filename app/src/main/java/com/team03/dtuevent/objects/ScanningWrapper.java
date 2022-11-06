package com.team03.dtuevent.objects;

import android.app.Dialog;
import android.content.Context;

import com.team03.dtuevent.App;
import com.team03.dtuevent.database.HistoryDatabase;
import com.team03.dtuevent.ui.ResultDisplayDialog;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.Objects;

/**
 * An UI wrapper around {@link Code} to manage UI interactions.
 */
public class ScanningWrapper {

    private Dialog dialog;
    private final Code code;
    private boolean scanned;
    private DismissListener dismissListener;

    public ScanningWrapper(Code code) {
        this.code = code;
    }

    public ScanningWrapper(Barcode barcode, DismissListener dismissListener) {
        this.code = new Code(barcode);
        this.dismissListener = dismissListener;
    }

    public Code getCode() {
        return code;
    }

    public void release() {
        App.globalExService.submit(() -> {
            // Wait for a while 1st
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanned = false;

            dismissListener.destroy(this);
        });


    }

    public void display(Context ctx, boolean showDialog) {
        if (!scanned) {
            if (showDialog) {
                dialog = new ResultDisplayDialog(ctx, code);
                dialog.setOnDismissListener(__ -> release());
                dialog.show();
            }
            scanned = true;

            // Add to history
            HistoryDatabase.insertCode(ctx, code);
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null; // Prevent references to old dialog.
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanningWrapper that = (ScanningWrapper) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public interface DismissListener {
        void destroy(ScanningWrapper instance);
    }

}
