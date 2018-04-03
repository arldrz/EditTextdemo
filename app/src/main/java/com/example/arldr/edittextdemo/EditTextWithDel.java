package com.example.arldr.edittextdemo;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @date 2018/2/5
 * 手机号码344格式输入框自带删除按钮
 */

public class EditTextWithDel extends android.support.v7.widget.AppCompatEditText implements TextWatcher{
    private final static String TAG = "EditTextWithDel";

    private Drawable imgAble;
    private Drawable imgInable;
    private Context mContext;
    // 特殊下标位置
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;
    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
      //  imgInable = mContext.getResources().getDrawable(R.drawable.delete_gray);
        imgAble = mContext.getResources().getDrawable(R.drawable.cancle_button);
        //事件监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    //设置删除图片
    private void setDrawable() {
        if(length() < 1){
            //表示没有输入文本内容时显示的图片，个人觉得不需要设置默认图片
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        }
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 70;
            if(rect.contains(eventX, eventY)){
                setText("");
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }

            setText(sb.toString());
            setSelection(index);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    // 获得不包含空格的手机号
    public String getPhoneText() {
        String str = getText().toString();
        return replaceBlank(str);
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            if (m.find()) {
                dest = m.replaceAll("");
            }
        }
        return dest;
    }


}
