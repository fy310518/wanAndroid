package com.gcstorage.map.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * 自定义输入框，不能数据emoji表情
 * Created by konwe
 * on 2016/12/23.
 */

@SuppressLint("AppCompatCustomView")
public class RestrictionEditText extends EditText {
    private Context mContext;

    public RestrictionEditText(Context context) {
        super(context);
        this.mContext = context;
    }

    public RestrictionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public RestrictionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if (inputConnection == null)
        {
            return null;
        }
        return new mYInputConnection(inputConnection,
                true);
    }

    class mYInputConnection extends InputConnectionWrapper implements InputConnection {

        public mYInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

//        public static final int CHAT_REFRESH_TIME = 2000;           //弹窗间隔时间
//        private long lastClickTime = 0;                 //比较时间

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            // 只能输入汉字或者英文
            if (!text.toString().matches("[\u4e00-\u9fa5]+")
                    && !text.toString().matches("[a-zA-Z /]+")
                    && !text.toString().matches("[0-9]+")
                    && !text.toString().matches("[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]")) {
//                long timeMillis = Calendar.getInstance().getTimeInMillis();
//                if (timeMillis - lastClickTime > CHAT_REFRESH_TIME) {
//                    Toast.makeText(mContext, "输入格式错误！", Toast.LENGTH_SHORT).show();
//                }
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
}