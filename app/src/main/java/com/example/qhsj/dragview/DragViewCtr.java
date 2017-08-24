package com.example.qhsj.dragview;

/**
 * Created by thinkpad on 2017/8/18.
 */
//一下是代码

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * @author zhouL
 *         Description: 显示悬浮控件
 */
public class DragViewCtr {

    private static final int MOVE_LENGH = 150;

    private int screenHeight;
    private int screenWidth;
    /**
     * 被拖动的图
     */
    private ImageButton iv_drag;
    private SharedPreferences sp;
    private Activity activity;

    /**
     * 当前状态（leftInto控件进入左侧，rightInto控件进入右侧，noInto没有进入）
     */
    private String state = "noInto";

    /**
     * 进入时要移动的值
     */
    private int mIntoValue = 0;


    public DragViewCtr(Activity activity) {
        this.activity = activity;
        this.screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        this.screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        LayoutInflater factory = LayoutInflater.from(activity);
        View layout = factory.inflate(R.layout.layout_drag_view, null);
        decorView.addView(layout);

        this.iv_drag = (ImageButton) layout.findViewById(R.id.imageview_drag);
        this.sp = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    /**
     * 显示可拖动的客服电话图标
     */
    public void showDragCallView() {
        this.iv_drag.setVisibility(View.VISIBLE);
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        iv_drag.measure(w, h);
        int viewheight = iv_drag.getMeasuredHeight();
        int viewwidth = iv_drag.getMeasuredWidth();
        int lastx = this.sp.getInt("lastx", width - viewwidth - 30);
        int lasty = this.sp.getInt("lasty", height - viewheight - 50);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.iv_drag.getLayoutParams();
        params.leftMargin = lastx;
        params.topMargin = lasty;
        this.iv_drag.setLayoutParams(params);

        this.iv_drag.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;
            long downTime;
            long upTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 手指第一次触摸到屏幕
                        this.startX = (int) event.getRawX();
                        this.startY = (int) event.getRawY();
                        downTime = System.currentTimeMillis();

                        if (state.equals("leftInto")) {
                            leftOut();
                        }

                        if (state.equals("rightInto")) {
                            rightOut();
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:// 手指移动
                        if (state.equals("leftInto") || state.equals("rightInto")) {
                            break;
                        }

                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();

                        int dx = newX - this.startX;
                        int dy = newY - this.startY;

                        // 计算出来控件原来的位置
                        int l = iv_drag.getLeft();
                        int r = iv_drag.getRight();
                        int t = iv_drag.getTop();
                        int b = iv_drag.getBottom();

                        // 计算出来控件拖到的位置
                        int newt = t + dy;
                        int newb = b + dy;
                        int newl = l + dx;
                        int newr = r + dx;

//                        if ((newl < 0) || (newt < 0) || (newr > screenWidth) || (newb > screenHeight)) {
//                            break;
//                        }

                        //达到左边缘
                        if (newl < 0) {
                            newl = 0;
                            newr = iv_drag.getWidth();
                        }

                        //达到上边缘
                        if (newt < 0) {
                            newt = 0;
                            newb = iv_drag.getHeight();
                        }

                        //达到右边缘
                        if (newr > screenWidth) {
                            newr = screenWidth;
                            newl = newr - iv_drag.getWidth();
                        }

                        //达到下边缘
                        if (newb > screenHeight) {
                            newb = screenHeight;
                            newt = newb - iv_drag.getHeight();
                        }


                        // 更新iv在屏幕的位置.
                        iv_drag.layout(newl, newt, newr, newb);
                        this.startX = (int) event.getRawX();
                        this.startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP: // 手指离开屏幕的一瞬间
                        int lastx = iv_drag.getLeft();
                        int lasty = iv_drag.getTop();

                        upTime = System.currentTimeMillis();

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastx", lastx);
                        editor.putInt("lasty", lasty);
                        editor.commit();

                        if (lastx < 20 && !state.equals("leftInto")) {
                            mIntoValue = lastx + iv_drag.getWidth() / 2;
                            leftInto();
                            break;
                        }

                        if (screenWidth - iv_drag.getRight() < 20 && !state.equals("rightInto")) {
                            mIntoValue = screenWidth - iv_drag.getRight() + iv_drag.getWidth() / 2;
                            rightInto();
                            break;
                        }

                        if (Math.abs(lastx - startX) < MOVE_LENGH && Math.abs(lasty - startY) < MOVE_LENGH && (upTime - downTime) < 150l) {//点击
                            click();
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     *
     */
    public void hideDragCallView() {
        this.iv_drag.setVisibility(View.GONE);
    }

    /**
     * 点击
     */
    private void click() {

    }

    private void leftInto() {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_drag, "translationX", 0, -mIntoValue);
        translationX.setDuration(500);
        translationX.start();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_drag, "alpha", 0.5f);
        alpha.setDuration(500);
        alpha.start();

        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.play(translationX).with(alpha);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        state = "leftInto";
    }

    private void leftOut() {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_drag, "translationX", -mIntoValue, 0);
        translationX.setDuration(500);
        translationX.start();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_drag, "alpha", 1f);
        alpha.setDuration(500);
        alpha.start();

        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.play(translationX).with(alpha);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        state = "noInto";
    }

    private void rightInto() {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_drag, "translationX", 0, mIntoValue);
        translationX.setDuration(500);
        translationX.start();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_drag, "alpha", 0.5f);
        alpha.setDuration(500);
        alpha.start();

        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.play(translationX).with(alpha);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        state = "rightInto";
    }

    private void rightOut() {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_drag, "translationX", mIntoValue, 0);
        translationX.setDuration(500);
        translationX.start();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_drag, "alpha", 1f);
        alpha.setDuration(500);
        alpha.start();

        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.play(translationX).with(alpha);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        state = "noInto";
    }
}