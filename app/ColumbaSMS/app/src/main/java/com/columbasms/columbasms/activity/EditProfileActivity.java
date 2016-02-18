package com.columbasms.columbasms.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.columbasms.columbasms.MyApplication;
import com.columbasms.columbasms.R;
import com.columbasms.columbasms.model.User;
import com.columbasms.columbasms.utils.Utils;
import com.columbasms.columbasms.utils.network.API_URL;
import com.columbasms.columbasms.utils.network.CacheRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Matteo Brienza on 2/16/16.
 */
public class EditProfileActivity extends AppCompatActivity{

    private static int RESULT_LOAD_IMAGE_PROFILE = 1;
    private static int RESULT_LOAD_IMAGE_COVER = 2;

    private static Bitmap scaled_profile;
    private static Bitmap scaled_cover;
    private static Toolbar toolbar;
    private static Activity activity;
    private static TextView name;
    private static String USER_ID;
    private static ProgressDialog dialog;
    private static ImageView cover;
    private static ImageView profile;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        activity = this;
        scaled_cover = null;
        scaled_profile = null;

        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
        cd.setAlpha(50);

        //TOP TOOLBAR SETUP
        toolbar = (Toolbar)findViewById(R.id.toolbar_edit_profile);
        toolbar.bringToFront();
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setBackgroundDrawable(cd);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.this.finish();
            }
        });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        USER_ID = sp.getString("user_id", "");
        cover = (ImageView)findViewById(R.id.cover_image_usr);
        profile = (ImageView) findViewById(R.id.profile_image);
        name = (TextView)findViewById(R.id.profile_usr_name);

        ImageView edit_profile = (ImageView) findViewById(R.id.profile_image);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                verifyStoragePermissions(activity);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        activity.getResources().getString(R.string.pic_select)),
                        RESULT_LOAD_IMAGE_PROFILE);
            }
        });


        final AppCompatEditText editUsername = (AppCompatEditText)findViewById(R.id.editUsername);
        editUsername.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String editText = editUsername.getText().toString();
                    if(editText.length()>0){
                        name.setText(editText);
                    }
                    return true;
                }
                return false;
            }
        });




        ImageView edit_cover = (ImageView) findViewById(R.id.update_cover_button);
        edit_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                verifyStoragePermissions(activity);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        activity.getResources().getString(R.string.pic_select)),
                        RESULT_LOAD_IMAGE_COVER);
            }
        });

        TextView save = (TextView)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = new ProgressDialog(activity);
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_progress);

                //SAVE CHANGES LOCALLY
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor_account_information = sp.edit();
                editor_account_information.putString("user_name", String.valueOf(name.getText()));
                editor_account_information.apply();

                String URL = API_URL.USERS_URL + "/" + USER_ID;

                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                JSONObject body = new JSONObject();
                try {
                    body.put("user_name",name.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, URL,body,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(scaled_cover!=null || scaled_profile!=null){
                                    uploadImages();
                                }else{
                                    dialog.dismiss();
                                    EditProfileActivity.this.finish();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.toString());
                                dialog.dismiss();
                                EditProfileActivity.this.finish();
                            }
                        }
                ) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String credentials = "47ccf9098174f48be281f86103b9" + ":" + "c5906274ba1a14711a816db53f0d";
                        String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
                        headers.put("Authorization", "Basic " + credBase64);
                        return headers;
                    }

                };

                requestQueue.add(putRequest);
            }

        });

        getUser();


    }

    @Override
    public void onResume(){
        super.onResume();
    }


    private static void getUser(){

        String URL = API_URL.USERS_URL + "/" + USER_ID;
        System.out.println(URL);

        CacheRequest request = new CacheRequest(0, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONObject o = new JSONObject(jsonString);
                    name.setText(o.getString("user_name"));
                    Utils.downloadImage(o.getString("avatar_normal"), profile, true, true);
                    Utils.downloadImage(o.getString("cover_normal"), cover, false, false);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //Adding request to the queue
        requestQueue.add(request);

    }

    public void uploadImages(){

        String URL = API_URL.USERS_URL + "/" + USER_ID + API_URL.IMAGES;

        //send changes to server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        dialog.dismiss();
                        //NOTIFY CHANGE TO DRAWER IN MAINACTIVITY
                        //MISSED
                        EditProfileActivity.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        System.out.println(volleyError.toString());
                        dialog.dismiss();
                        EditProfileActivity.this.finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image_profile = "";
                if(scaled_profile!=null) image_profile = getStringImage(scaled_profile);
                String image_cover = "";
                if(scaled_cover!=null) image_cover = getStringImage(scaled_cover);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("profile_image", image_profile);
                params.put("cover_image", image_cover);

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String credentials = "47ccf9098174f48be281f86103b9" + ":" + "c5906274ba1a14711a816db53f0d";
                String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
                headers.put("Authorization", "Basic "+ credBase64);
                return headers;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_PROFILE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            try {
                ImageView img1 = (ImageView) findViewById(R.id.profile_image);

                Bitmap bm = getBitmapFromUri(selectedImage);
                int nh = (int) ( bm.getHeight() * (512.0 / bm.getWidth()) );
                scaled_profile = Bitmap.createScaledBitmap(bm, 512, nh, true);
                img1.setImageBitmap(scaled_profile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (requestCode == RESULT_LOAD_IMAGE_COVER && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                ImageView imageView = (ImageView) findViewById(R.id.cover_image_usr);

                Bitmap bm = getBitmapFromUri(selectedImage);
                int nh = (int) ( bm.getHeight() * (512.0 / bm.getWidth()) );
                scaled_cover = Bitmap.createScaledBitmap(bm, 512, nh, true);


                imageView.setImageBitmap(scaled_cover);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }





    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_info:
                i = new Intent(this,InfoActivity.class);
                startActivity(i);
                return true;
            case R.id.action_feedback:
                Intent j = new Intent(Intent.ACTION_SEND);
                j.setType("message/rfc822");
                j.putExtra(Intent.EXTRA_EMAIL  , new String[]{"columbasms@gmail.com"});
                j.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                j.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(j, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(EditProfileActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_guide:
                //i = new Intent(this,GuideActivity.class);
                //startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
