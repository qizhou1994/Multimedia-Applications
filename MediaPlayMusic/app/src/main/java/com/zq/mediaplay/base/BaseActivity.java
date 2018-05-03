package com.zq.mediaplay.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Author zq
 * Created by CTSIG on 2018/5/3.
 * Email : qizhou1994@126.com
 */

public class BaseActivity extends AppCompatActivity {


    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThen(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
