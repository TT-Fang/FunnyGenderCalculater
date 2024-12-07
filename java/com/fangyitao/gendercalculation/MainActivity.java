package com.yourpackage.gendercalculation;

import  android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SelectedGender selectedGender;  // 新建的字符串类实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedGender = new SelectedGender();

        // 设置 Spinner 监听器
        Spinner chooser = findViewById(R.id.chooser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooser.setAdapter(adapter);

        chooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                selectedGender.setSelectedGender(selectedItem);  // 设置所选内容
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选择的情况
            }
        });
    }

    public void calculate(View view) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        // 更新进度条
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                progressBar.setProgress(progress);
            }
        });

        // 动画结束后 显示对话框 播放音频
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(View.GONE);
                int gender = new Random().nextInt(2);
                showGenderDialog(gender);
                 playAudio(gender);
            }
        });

        animator.start();
    }

    private void showGenderDialog(int gender) {
        String genderText = (gender == 0) ? "男" : "女";  // 设置性别
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setTitle("计算结果：")
        .setMessage("恭喜您，您 " + selectedGender.getSelectedGender() + " 的性别为：" + genderText)
        .setPositiveButton("确定", null);
        builder.show();
    }

     private void playAudio(int gender) {
         MediaPlayer mediaPlayer;
         if (gender == 0) {
             mediaPlayer = MediaPlayer.create(this, R.raw.male);  // 男
         } else {
             mediaPlayer = MediaPlayer.create(this, R.raw.female);  // 女
         }
         mediaPlayer.start();
         mediaPlayer.setOnCompletionListener(mp -> {
             mp.release();  // 释放资源
         });
     }
}
