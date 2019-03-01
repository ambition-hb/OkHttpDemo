package com.haobi.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 采用OkHttp框架，通过访问聚合API查询手机归属地
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button sync_get;
    private Button async_get;
    private Button sync_post;
    private Button async_post;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sync_get = (Button)findViewById(R.id.sync_get);
        async_get = (Button)findViewById(R.id.async_get);
        sync_post = (Button)findViewById(R.id.sync_post);
        async_post = (Button)findViewById(R.id.async_post);
        textView = (TextView)findViewById(R.id.tv);
        sync_get.setOnClickListener(this);
        async_get.setOnClickListener(this);
        sync_post.setOnClickListener(this);
        async_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sync_get:
                Sync_Get();
                break;
            case R.id.sync_post:
                Sync_Post();
                break;
            case R.id.async_get:
                Async_Get();
                break;
            case R.id.async_post:
                Async_Post();
                break;
            default:
                break;
        }
    }

    //同步请求-GET
    private void Sync_Get(){
        //开启新线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://apis.juhe.cn/mobile/get?phone=13429667914&key=fef8795fcfa0a1977582d8c31b529112")
                        .build();
                Response response = null;//初始化
                try {
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //异步请求-GET
    private void Async_Get(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://apis.juhe.cn/mobile/get?phone=13956676914&key=fef8795fcfa0a1977582d8c31b529112")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(MainActivity.this, "异步网络请求(GET)失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        showResponse(responseData);
                    }
                });
            }
        }).start();
    }

    //同步请求-Post
    private void Sync_Post(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("phone", "13852676914")
                        .add("key", "fef8795fcfa0a1977582d8c31b529112")
                        .build();
                Request request = new Request.Builder()
                        .url("http://apis.juhe.cn/mobile/get?")
                        .post(requestBody)
                        .build();
                Response response = null;//初始化
                try {
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //异步请求-Post
    private void Async_Post(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("phone", "13159476914")
                        .add("key", "fef8795fcfa0a1977582d8c31b529112")
                        .build();
                Request request = new Request.Builder()
                        .url("http://apis.juhe.cn/mobile/get?")
                        .post(requestBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(MainActivity.this, "异步网络请求(Post)失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        showResponse(responseData);
                    }
                });
            }
        }).start();
    }

    private void showResponse(final String response){
        //Android不允许在子线程中进行UI操作
        //通过该方法可以将线程切换到主线程，然后再更新UI元素
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });
    }

}
