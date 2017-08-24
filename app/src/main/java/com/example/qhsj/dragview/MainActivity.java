package com.example.qhsj.dragview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvShow;
    private TextView tvHide;
    private DragViewCtr dragViewCtr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShow = (TextView) findViewById(R.id.tvShow);
        tvHide = (TextView) findViewById(R.id.tvHide);

        tvShow.setOnClickListener(this);
        tvHide.setOnClickListener(this);

        dragViewCtr = new DragViewCtr(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvShow){
            dragViewCtr.showDragCallView();
        }else if(v == tvHide){
            dragViewCtr.hideDragCallView();
        }
    }
}
