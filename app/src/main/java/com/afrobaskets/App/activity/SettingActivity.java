package com.afrobaskets.App.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.interfaces.Constant;
import com.afrobaskets.App.interfaces.SavePref;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.SettingActivityBinding;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP-PC on 11/24/2017.
 */

public class SettingActivity  extends AppCompatActivity {
JSONObject sendJson;
    SettingActivityBinding settingActivityBinding;
    String message, encodedImage;
    Button btnSubmit;
    private ProgressDialog pDialog;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    Bitmap thumbnail = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         settingActivityBinding= DataBindingUtil.setContentView(this,R.layout.setting_activity);
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("Setting");
        Glide
                .with(this)
                .load(SavePref.getPref(this,SavePref.image))
                .centerCrop()

                .into( settingActivityBinding.docImageEdit);
        settingActivityBinding.docImageEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(ContextCompat.checkSelfPermission(SettingActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    selectImage();                        }
                else{
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }                return false;
            }
        });

        settingActivityBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String old_password = settingActivityBinding.oldPassword.getText().toString();
                if (Constant.isEmpty(old_password)) {

                    if (!Constant.isEmpty(old_password)) {
                        settingActivityBinding.oldPassword.setError("Enter Password");
                        return;
                    }
                    final String password = settingActivityBinding.txtPassword.getText().toString();
                    if (!Constant.isEmpty(password)) {
                        settingActivityBinding.txtPassword.setError("Enter Password");
                        return;
                    }

                    if (!old_password.equalsIgnoreCase(SavePref.get_credential(SettingActivity.this,
                            SavePref.Password))) {
                        settingActivityBinding.oldPassword.setError("Enter correct old Password");
                        settingActivityBinding.oldPassword.setText(" ");
                        return;
                    }

                    final String cfm_password = settingActivityBinding.cfmPasword.getText().toString();
                    if (!Constant.isEmpty(cfm_password)) {
                        settingActivityBinding.cfmPasword.setError("Enter Password");
                        return;
                    }

                    if (!password.equalsIgnoreCase(cfm_password)) {

                        settingActivityBinding.cfmPasword.setError("Password Not Matched");
                        settingActivityBinding.cfmPasword.setText("");
                        settingActivityBinding.txtPassword.setText("");
                        return;
                    }
                }


                String image = getStringImage(thumbnail);

                change_password(image);
            }
        });
        ImageView toolbar_back=(ImageView)findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public  void change_password(String encodedImage)
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "addEditRider");
            sendJson.put("id",SavePref.getPref(SettingActivity.this,
                  SavePref.User_id));
           // if (settingActivityBinding.txtPassword.getText().toString().trim().length() == 0) {
                sendJson.put("password", settingActivityBinding.txtPassword.getText().toString());
            //}

                /*if (settingActivityBinding.docImageEdit.getDrawable() != null) {
                    sendJson.put("image", "data:image/jpeg;base64," + encodedImage);
                }*/

            /*  sendJson.put("mobile_number","");
            sendJson.put("location_id","");
            sendJson.put("fcm_reg_id","");*/
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL+"application/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                Toast.makeText(getApplicationContext(), "Photo updated successfully", Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                            }
                            else
                            {
                                pDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();                            }
                        } catch (Exception e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(SettingActivity.this, "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(SettingActivity.this, "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(SettingActivity.this, "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(SettingActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(SettingActivity.this, "Parse Error!", Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


        private void selectImage() {
            final CharSequence[] items = {"Take Photo","Gallary",
                    "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            userChoosenTask = "Take Photo";
    cameraIntent();
dialog.dismiss();
                        }  else if (items[item].equals("Gallary")) {
                            userChoosenTask = "Gallary";
                            galleryIntent();
                            dialog.dismiss();
                        }
                        else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }  }



            });
            builder.show();
        }
    private final int CAMERA_RESULT = 101;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                selectImage();                        }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }/*

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingActivityBinding.docImageEdit.setImageBitmap(thumbnail);
    }*/
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingActivityBinding.docImageEdit.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(SettingActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        settingActivityBinding.docImageEdit.setImageBitmap(thumbnail);
    }


    public String getStringImage(Bitmap bmp){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG,60, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }catch (Exception e){

        }
        return encodedImage;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

