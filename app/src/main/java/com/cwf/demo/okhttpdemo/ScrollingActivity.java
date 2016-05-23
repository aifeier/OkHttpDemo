package com.cwf.demo.okhttpdemo;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cwf.libs.okhttplibrary.OkHttpClientManager;
import com.cwf.libs.okhttplibrary.callback.ResultCallBack;
import com.cwf.libs.okhttplibrary.callback.SimpleCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView resultTv;

    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            resultTv.setText(msg.getData().getString("data"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
    }

    private void initView() {
        resultTv = (AppCompatTextView) findViewById(R.id.result);
        findViewById(R.id.test_get).setOnClickListener(this);
        findViewById(R.id.test_post).setOnClickListener(this);
        findViewById(R.id.test_down).setOnClickListener(this);
        findViewById(R.id.test_upload).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_get:
                test_Get();
                break;
            case R.id.test_post:
                test_Post();
                break;
            case R.id.test_down:
                test_down();
                break;
            case R.id.test_upload:
//                startTwoActivity();
                test_upload();
                break;
        }
    }

    private void test_upload() {
        HashMap<String, String> params = new HashMap<>();
        params.put("wd", "陈万烽");
        List<File> fileList = new ArrayList<>();
        fileList.add(new File("/storage/sdcard0/DCIM/Camera/IMG_20160523_153816.jpg"));
        OkHttpClientManager.getInstance().uploadFile("http://www.baidu.com", params, fileList,
                new SimpleCallBack(this) {

                    @Override
                    public void onFailure(Exception e) {
                        resultTv.setText(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String result) {
                        resultTv.setText(result);
                    }
                });
    }

    private void startTwoActivity() {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("cwf://meme"), "video/*");
        startActivity(intent);
    }

    private void test_down() {
        OkHttpClientManager.getInstance()
                .downloadFile("http://fc.topitme.com/c/4e/a2/110721716652ea24eco.jpg"
                        , getExternalCacheDir().getAbsolutePath(), new SimpleCallBack(this) {
                            @Override
                            public void onFailure(Exception e) {
                                resultTv.setText(e.getMessage());
                            }

                            @Override
                            public void onSuccess(String result) {
                                resultTv.setText(result);
                            }

                            @Override
                            public void onDownloading(long downSize, long allSize) {
                                super.onDownloading(downSize, allSize);
                                resultTv.setText(downSize + " / " + allSize);
                            }
                        });
    }

    private void test_Get() {
        HashMap<String, String> params = new HashMap<>();
        params.put("wd", "陈万烽");
        OkHttpClientManager.getInstance().get("http://apis.baidu.com/apistore/weatherservice/weather?citypinyin=hangzhou", new SimpleCallBack(this) {

            @Override
            public void onFailure(Exception e) {
                resultTv.setText(e.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                resultTv.setText(result);
            }
        });
    }

    private void test_Post() {
        HashMap<String, String> params = new HashMap<>();
        params.put("query", "虎妈猫爸的最新剧集");
        params.put("resource", "video_haiou");
        OkHttpClientManager.getInstance().post("http://apis.baidu.com/baidu_openkg/shipin_kg/shipin_kg", params, new SimpleCallBack(this) {

            @Override
            public void onFailure(Exception e) {
                resultTv.setText(e.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                Log.e("ABC", result);
                resultTv.setText(result);
            }
        });
    }

    private void get(String url, Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
//        if (response.isSuccessful()) {
//            return response.body().string();
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
    }
}
