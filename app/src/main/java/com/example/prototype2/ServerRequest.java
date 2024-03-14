package com.example.prototype2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerRequest extends AsyncTask<Void, Void, String> {

    private WeakReference<Context> contextReference;
    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String encodedImage;
    private ServerCallback  callback;

    ServerRequest(Context context, String eventTitle, String eventDate, String eventTime, String encodedImage, ServerCallback callback) {
        this.contextReference = new WeakReference<>(context);
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.encodedImage = encodedImage;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // You can perform UI operations before the background task starts (if needed)
    }

    @Override
    protected String doInBackground(Void... params) {
        Context context = contextReference.get();
        if (context == null) {
            return null;
        }

        try {
            // Create a RequestBody object with form data
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("eventTitle", eventTitle)
                    .addFormDataPart("eventDate", eventDate)
                    .addFormDataPart("eventTime", eventTime)
                    .addFormDataPart("encodedImage", encodedImage)
                    .build();

            // Create an OkHttpClient instance
            OkHttpClient client = new OkHttpClient();

            // Build the request
            Request request = new Request.Builder()
                    .url("http://192.168.1.161/serverrequest.php")  // Update the URL accordingly
                    .post(requestBody)
                    .build();

            // Execute the request and get the response
            Response response = client.newCall(request).execute();

            // Check if the response was successful
            if (response.isSuccessful()) {

                Log.d("ServerRequest", "Request was successful. Response code: " + response.code());
                return response.body().string();
            } else {
                // Return an error message
                return "Unsuccessful";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public interface ServerCallback {
        void onResponse(String result);
        void onError(String error);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Perform UI operations with the result on the main thread
        Context context = contextReference.get();
        if (context != null) {
            if (result != null) {
                // The network call was successful
                callback.onResponse(result);
            } else {
                // The network call was not successful
                callback.onError("Error during the network request");
            }
        }
    }
}
