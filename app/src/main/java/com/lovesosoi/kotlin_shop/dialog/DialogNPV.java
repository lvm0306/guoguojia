package com.lovesosoi.kotlin_shop.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.carbs.android.indicatorview.library.IndicatorView;
import com.lovesosoi.kotlin_shop.R;
import com.lovesosoi.kotlin_shop.interfaces.IPickListener;
import com.lovesosoi.kotlin_shop.view.ChineseCalendar;
import com.lovesosoi.kotlin_shop.view.GregorianLunarCalendarView;

import java.util.Calendar;

/**
 * Created by carbs on 2016/7/12.
 */

public class DialogNPV extends Dialog implements View.OnClickListener, IndicatorView.OnIndicatorChangedListener {

    private Context mContext;
    private IndicatorView mIndicatorView;
    private GregorianLunarCalendarView mGLCView;
    private Button mButtonGetData, mClose, mSure;
    private TextView mTitle;
    private String title;
    private IPickListener mListener;

    public DialogNPV(Context context, String title) {
        super(context, R.style.dialog);
        mContext = context;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_npv);
        initWindow();

        mGLCView = (GregorianLunarCalendarView) findViewById(R.id.calendar_view);
//        mGLCView.init();//init has no scroll effect, to today
        mIndicatorView = (IndicatorView) findViewById(R.id.indicator_view);
        mIndicatorView.setOnIndicatorChangedListener(this);

        mButtonGetData = (Button) findViewById(R.id.button_get_data);
        mClose = (Button) findViewById(R.id.close);
        mSure = (Button) findViewById(R.id.sure);
        mTitle = (TextView) findViewById(R.id.title);
        mButtonGetData.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mTitle.setText(title);
        initCalendar();
    }

    public void initCalendar() {
        mGLCView.init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_data:
                mGLCView.init();
//                GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
//                Calendar calendar = calendarData.getCalendar();
//                String showToast = "Gregorian : " + calendar.get(Calendar.YEAR) + "-"
//                        + (calendar.get(Calendar.MONTH) + 1) + "-"
//                        + calendar.get(Calendar.DAY_OF_MONTH) + "\n"
//                        + "Lunar     : " + calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
//                        + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
//                        + calendar.get(ChineseCalendar.CHINESE_DATE);
//                Toast.makeText(mContext.getApplicationContext(), showToast, Toast.LENGTH_LONG).show();
                break;
            case R.id.close:
                dismiss();
                break;
            case R.id.sure:
                if (mListener != null) {
                    GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
                    Calendar calendar = calendarData.getCalendar();
                    mListener.pick(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
        if (newSelectedIndex == 0) {
            toGregorianMode();
        } else if (newSelectedIndex == 1) {
            toLunarMode();
        }
    }

    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    private void toLunarMode() {
        mGLCView.toLunarMode();
    }

    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (0.90 * getScreenWidth(getContext()));
        if (lp.width > dp2px(getContext(), 480)) {
            lp.width = dp2px(getContext(), 480);
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setListener(IPickListener mListener) {
        this.mListener = mListener;
    }
}