package com.example.qhsj.dragview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvShow01;
    private TextView tvHide01;
    private DragViewCtr01 dragViewCtr01;
    private TextView tvShow02;
    private TextView tvHide02;
    private DragViewCtr02 dragViewCtr02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        dragViewCtr01 = new DragViewCtr01(MainActivity.this);
        dragViewCtr01.hideDragCallView();
        dragViewCtr02 = new DragViewCtr02(MainActivity.this);
        dragViewCtr02.hideDragCallView();
    }

    private void initListener() {
        tvShow01.setOnClickListener(this);
        tvHide01.setOnClickListener(this);
        tvShow02.setOnClickListener(this);
        tvHide02.setOnClickListener(this);
    }

    private void initView() {
        tvShow01 = (TextView) findViewById(R.id.tvShow01);
        tvHide01 = (TextView) findViewById(R.id.tvHide01);
        tvShow02 = (TextView) findViewById(R.id.tvShow02);
        tvHide02 = (TextView) findViewById(R.id.tvHide02);
    }

    @Override
    public void onClick(View v) {
        if (v == tvShow01){
            dragViewCtr01.showDragCallView();
        }else if(v == tvHide01){
            dragViewCtr01.hideDragCallView();
        }else if(v == tvShow02){
            dragViewCtr02.showDragCallView();
        }else if(v == tvHide02){
            dragViewCtr02.hideDragCallView();
        }
    }
}
