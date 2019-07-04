package com.gcstorage.parkinggather.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * 车牌号的EditText
 * 只能输入字母和数字,并且字母为大写
 */
@SuppressLint("AppCompatCustomView")
public class PlateNumEditText extends EditText {
    private Context mContext;

    public PlateNumEditText(Context context) {
        super(context);
        this.mContext = context;
        myinit();
    }

    private void myinit() {
        setTransformationMethod(new A2bigA());
    }

    public PlateNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        myinit();
    }

    public PlateNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        myinit();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if (inputConnection == null) {
            return null;
        }
        return new mYInputConnection(inputConnection,
                true);
    }

    class mYInputConnection extends InputConnectionWrapper implements InputConnection {

        public mYInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }


        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            // 只能字母和数字
            if (!text.toString().matches("[a-zA-Z /]+") && !text.toString().matches("[0-9]+")) {
                return false;
            }
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean setSelection(int start, int end) {
            return super.setSelection(start, end);
        }
    }


    public class A2bigA extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }

    }
}
