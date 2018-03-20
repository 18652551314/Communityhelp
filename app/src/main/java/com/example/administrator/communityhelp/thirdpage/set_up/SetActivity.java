package com.example.administrator.communityhelp.thirdpage.set_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.Codes;
import com.example.administrator.communityhelp.R;

public class SetActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout reLayout_sound, reLayout_shake, reLayout_ringtone, reLayout_check;
    private View line_one, line_two, line_three;
    private ImageView btn_notification, btn_sound, btn_shake;
    private TextView text_soundName;
    private int flag_notification, flag_sound, flag_shak = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        init();
        SharedPreferences sp=getSharedPreferences("soundName",MODE_PRIVATE);
        text_soundName.setText(sp.getString("RingToneName",""));
    }

    private void init() {
        btn_notification = (ImageView) findViewById(R.id.image_notify);
        btn_notification.setOnClickListener(this);
        btn_sound = (ImageView) findViewById(R.id.image_sound);
        btn_sound.setOnClickListener(this);
        btn_shake = (ImageView) findViewById(R.id.image_shake);
        btn_shake.setOnClickListener(this);
        reLayout_sound = (RelativeLayout) findViewById(R.id.relayout_sound);
        reLayout_shake = (RelativeLayout) findViewById(R.id.relayout_shake);
        reLayout_ringtone = (RelativeLayout) findViewById(R.id.relayout_ringtone);
        reLayout_check = (RelativeLayout) findViewById(R.id.relayout_check);
        reLayout_ringtone.setOnClickListener(this);
        reLayout_check.setOnClickListener(this);
        line_one = findViewById(R.id.line_one);
        line_two = findViewById(R.id.line_two);
        line_three = findViewById(R.id.line_three);
        text_soundName = (TextView) findViewById(R.id.text_ringtone);
        TextView text_set_mine = (TextView) findViewById(R.id.text_set_mine);
        text_set_mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_notify:
                if (flag_notification == 1) {
                    btn_notification.setBackgroundResource(R.mipmap.on350);
                    reLayout_sound.setVisibility(View.VISIBLE);
                    line_one.setVisibility(View.VISIBLE);
                    reLayout_shake.setVisibility(View.VISIBLE);
                    line_two.setVisibility(View.VISIBLE);
                    if (flag_sound == 1) {
                        reLayout_ringtone.setVisibility(View.GONE);
                        line_three.setVisibility(View.GONE);

                    } else if (flag_sound == 0) {
                        reLayout_ringtone.setVisibility(View.VISIBLE);
                        line_three.setVisibility(View.VISIBLE);
                    }
                    flag_notification = 0;
                } else if (flag_notification == 0) {
                    btn_notification.setBackgroundResource(R.mipmap.off350);
                    reLayout_sound.setVisibility(View.GONE);
                    line_one.setVisibility(View.GONE);
                    reLayout_shake.setVisibility(View.GONE);
                    line_two.setVisibility(View.GONE);
                    reLayout_ringtone.setVisibility(View.GONE);
                    line_three.setVisibility(View.GONE);
                    flag_notification = 1;
                }
                break;
            case R.id.image_sound:
                if (flag_sound == 0) {
                    btn_sound.setBackgroundResource(R.mipmap.off350);
                    reLayout_ringtone.setVisibility(View.GONE);
                    line_three.setVisibility(View.GONE);
                    flag_sound = 1;
                } else if (flag_sound == 1) {
                    btn_sound.setBackgroundResource(R.mipmap.on350);
                    reLayout_ringtone.setVisibility(View.VISIBLE);
                    line_three.setVisibility(View.VISIBLE);
                    flag_sound = 0;
                }
                break;
            case R.id.image_shake:
                if (flag_shak == 0) {
                    btn_shake.setBackgroundResource(R.mipmap.off350);
                    flag_shak = 1;
                } else if (flag_shak == 1) {
                    btn_shake.setBackgroundResource(R.mipmap.on350);
                    flag_shak = 0;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                    vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                }
                break;
            case R.id.relayout_ringtone:
                Intent intent = new Intent(this, SoundSelectActivity.class);
                startActivityForResult(intent, Codes.SOUNDSELECTED_REQUESTCODE);
                break;
            case R.id.relayout_check:
                //Toast若是new出来，则需设置setView
                Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View view = getLayoutInflater().inflate(R.layout.toastview, null);
                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                toastText.setText("当前已是最新版本");
                toast.setView(view);
                toast.show();
                break;
            case R.id.text_set_mine:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codes.SOUNDSELECTED_REQUESTCODE && resultCode == RESULT_OK) {
            text_soundName.setText(data.getStringExtra("soundName"));
        }
    }
}
