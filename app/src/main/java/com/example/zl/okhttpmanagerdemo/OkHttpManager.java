package com.example.zl.okhttpmanagerdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zl on 2017/4/22.
 */

public class OkHttpManager {
    private OkHttpClient client;
    private volatile static OkHttpManager manager;
    private Handler handler;
    private final String TAG = OkHttpManager.class.getSimpleName();//获得类名

    private static final MediaType JSON = MediaType.parse("");
    private static final MediaType MEDIA_TYPE_MAKEDOWN = MediaType.parse("");

    private OkHttpManager(){
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }
    //单例模式获取对象（好像有问题这个单例）
    public static OkHttpManager getInstance(){
        OkHttpManager instance = null;
        if (manager == null){
            synchronized (OkHttpManager.class){
                if (instance == null){
                    instance = new OkHttpManager();
                    manager = instance;
                }
            }
        }
        return instance;
    }

    //单例参考
    //private static DownloadImageUtil downloadImageUtil;

    //public static DownloadImageUtil getInstance (){
//        if (null == downloadImageUtil){
//        synchronized (DownloadImageUtil.class){
//            downloadImageUtil = new DownloadImageUtil();
//        }
//    }
//        return downloadImageUtil;
//    }

    /**
     * 同步请求json 不常用
     * @param url
     * @return
     */
    public String syncGetByURL(String url){
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try{
            response = client.newCall(request).execute();//同步请求数据
            if (response.isSuccessful()){
                return response.body().string();
            }
        }catch (Exception e){}
        return null;
    }

    /**
     * 请求url，返回Jason字符串
     * @param url
     * @param callBack
     */
    public void asyncJsonStringByURL(String url,final Func1 callBack){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    onSuccessJsonStringMethod(response.body().string(),callBack);
                }
            }
        });
    }

    /**
     * 请求返回的是json对象
     * @param url
     * @param callBack
     */
    public void asyncJsonObjectByURL(String url,final Func4 callBack){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    onSuccessJsonObjectMethod(response.body().string(),callBack);
                }
            }
        });
    }

    /**
     * 请求返回byte[]
     * @param url
     * @param callBack
     */
    public void asyncGetByteByURL(String url,final Func2 callBack){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    onSuccessByteMethod(response.body().bytes(),callBack);
                }
            }
        });
    }

    /**
     * 请求一张图片
     * @param url
     * @param callBack
     */
    public void asyncDownloadImageByURL(String url,final Func3 callBack){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    byte[] bytes = response.body().bytes();
                    onSuccessBitmapMethod(BitmapFactory.decodeByteArray(bytes,0,bytes.length),callBack);
                }
            }
        });
    }

    /**
     * 提交表单数据
     * @param url
     * @param params
     * @param callBack
     */
    public void sendComplexForm(String url, Map<String,String> params, final Func4 callBack){
        FormBody.Builder from_builder = new FormBody.Builder();//表单对象，包含以input开始的对象，yihtml表单为主
        if(params != null && !params.isEmpty()){
            for (Map.Entry<String,String> entry : params.entrySet()) {
                from_builder.add(entry.getKey(),entry.getValue());
            }
        }
        RequestBody requestBody = from_builder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();//采用post方式提交
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    onSuccessJsonObjectMethod(response.body().string(),callBack);
                }
            }
        });
    }



//    public void uploadFile(String url,Map<String,String> map ,File[] files ,String[] formFieldNames){
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//
////        RequestBody requestBody = RequestBody.create(
////                MediaType.parse("application/octet-stream"), file);
////        // 添加参数
////        builder.addFormDataPart("userName", userName);
////        // 添加文件，第二个参数将传给服务器端的文件名mPhotoFileName
////        builder.addFormDataPart("mPhoto", userName + ".jpg", requestBody);
////        RequestBody requestBody2 = builder.build();
////        Request.Builder builder2 = new Request.Builder();
////        Request request = builder2.url(url)
////                .post(requestBody2).build();
////        client.newCall(request).enqueue(new Callback() {
////            @Override
////            public void onFailure(Call call, IOException e) {
////
////            }
////
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////
////            }
////        });
//
//    }

    /**
     * 请求返回的结果是json字符串
     * @param jsonvalue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonvalue, final Func1 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                    try {
                        callBack.onResponse(jsonvalue);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 返回的结果是json对象
     * @param jsonvalue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonvalue, final Func4 callBack){
        handler.post(new Runnable(){
            @Override
            public void run() {
                if (callBack != null){
                    try {
                        callBack.onResponse(new JSONObject(jsonvalue));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 请求返回的是byte[]
     * @param data
     * @param callBack
     */
    private void onSuccessByteMethod(final byte[] data, final Func2 callBack){
        handler.post(new Runnable(){
            @Override
            public void run() {
                if (callBack != null){
                    try {
                        callBack.onResponse(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 请求返回的是bitmap
     * @param bitmap
     * @param callBack
     */
    private void onSuccessBitmapMethod(final Bitmap bitmap, final Func3 callBack){
        handler.post(new Runnable(){
            @Override
            public void run() {
                if (callBack != null){
                    try {
                        callBack.onResponse(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    interface Func1{
        void onResponse(String result);
    }
    interface Func2{
        void onResponse(byte[] result);
    }
    interface Func3{
        void onResponse(Bitmap bitmap);
    }
    interface Func4{
        void onResponse(JSONObject jsonObject);
    }
}
//使用：
//        OkHttpManaget okmanager = OkHttpManager.getInstance()
//        okmanager.asyncJsonStringByURL(url,new Func1(){
//                onresponse(){
//                     这里面可以更改UI的
//                 }
//        });


//给请求设置tag，方便后面根据tag取消请求
//Request.Builder builder = new Request.Builder();
//Request request = builder.tag("").url(url).build();

//设置缓存，设置时间限制（注意这个builder不是Request.Builder builder，而是OkHttpClient.Builder builder ）
//okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(context.getExternalCacheDir(),"okhttpcache"),1*1024*1024)).build();
//OkHttpClient.Builder builder = new OkHttpClient.Builder();
//builder.connectTimeout(5, TimeUnit.SECONDS)
//builder.writeTimeout()
//builder.readTimeout()
//builder.cache()
//OkHttpClient client = builder.build();