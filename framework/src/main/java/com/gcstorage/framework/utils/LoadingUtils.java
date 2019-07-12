package com.gcstorage.framework.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcstorage.framework.R;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class LoadingUtils extends Dialog {

    public static final int FADED_ROUND_SPINNER = 0;
    public static final int GEAR_SPINNER = 1;
    public static final int SIMPLE_ROUND_SPINNER = 2;

    TextView tvMessage;
    ImageView ivSuccess;
    ImageView ivFailure;
    ImageView ivProgressSpinner;
    AnimationDrawable adProgressSpinner;
    Context context;

    OnDialogDismiss onDialogDismiss;

    public OnDialogDismiss getOnDialogDismiss() {
        return onDialogDismiss;
    }

    public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss) {
        this.onDialogDismiss = onDialogDismiss;
    }

    public LoadingUtils(Context context) {
        super(context, R.style.DialogTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        tvMessage = (TextView) view.findViewById(R.id.textview_message);
        ivSuccess = (ImageView) view.findViewById(R.id.imageview_success);
        ivFailure = (ImageView) view.findViewById(R.id.imageview_failure);
        ivProgressSpinner = (ImageView) view
                .findViewById(R.id.imageview_progress_spinner);

        setSpinnerType(FADED_ROUND_SPINNER);
        this.setContentView(view);
    }

    @Override
    public void show() {
        reset();
        try {
            super.show();
        } catch (Exception e) {

        }
    }

    public void setSpinnerType(int spinnerType) {
        ivProgressSpinner.setImageResource(R.drawable.round_spinner_fade);

        adProgressSpinner = (AnimationDrawable) ivProgressSpinner.getDrawable();

    }

    public void setMessage(String message) {
        tvMessage.setText(message);
    }

    public void dismissWithSuccess() {
        tvMessage.setText("Success");
        showSuccessImage();

        if (onDialogDismiss != null) {
            this.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDialogDismiss.onDismiss();
                }
            });
        }
        dismiss();
    }

    public void dismissWithSuccess(String message) {
        showSuccessImage();
        if (message != null) {
            tvMessage.setText(message);
        } else {
            tvMessage.setText("");
        }

        if (onDialogDismiss != null) {
            this.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDialogDismiss.onDismiss();
                }
            });
        }
        dismiss();
    }

    public void dismissWithFailure() {
        showFailureImage();
        tvMessage.setText("Failure");
        if (onDialogDismiss != null) {
            this.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDialogDismiss.onDismiss();
                }
            });
        }
        dismiss();
    }

    public void dismissWithFailure(String message) {
        showFailureImage();
        if (message != null) {
            tvMessage.setText(message);
        } else {
            tvMessage.setText("");
        }
        if (onDialogDismiss != null) {
            this.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDialogDismiss.onDismiss();
                }
            });
        }
        dismiss();
    }

    protected void showSuccessImage() {
        ivProgressSpinner.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.VISIBLE);
    }

    protected void showFailureImage() {
        ivProgressSpinner.setVisibility(View.GONE);
        ivFailure.setVisibility(View.VISIBLE);
    }

    protected void reset() {
        ivProgressSpinner.setVisibility(View.VISIBLE);
        ivFailure.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
    }

    private void finishActivity() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    protected void dismissHUD() {
        AsyncTask<String, Integer, Long> task = new AsyncTask<String, Integer, Long>() {

            @Override
            protected Long doInBackground(String... params) {
                SystemClock.sleep(500);
                return null;
            }

            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                dismiss();
                reset();
            }
        };
        task.execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ivProgressSpinner.post(new Runnable() {

            @Override
            public void run() {
                adProgressSpinner.start();

            }
        });
    }

    public interface OnDialogDismiss {
        public void onDismiss();
    }
}
