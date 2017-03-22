package com.jobnow.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.acitvity.MenuActivity;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.common.CustomEditextHelveticaneuelight;
import com.jobnow.common.DrawableClickListener;
import com.jobnow.config.Config;
import com.jobnow.controller.UserController;
import com.jobnow.models.ProfileModel;
import com.jobnow.models.ProfileRequest;
import com.jobnow.models.UploadFileResponse;
import com.jobnow.models.UserModel;
import com.newtech.jobnow.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 10/02/2017.
 */

public class ProfileManagerFragment extends Fragment {
    ImageView img_add,img_photo_company;
    CustomEditextHelveticaneuelight txt_profile__name_company;
    EditText editDescription;
    Button btnSaveProfileManager;
    UserModel userModel;
    boolean flag=false;
    private SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_manager, container, false);
        InitUI(rootView);
        InitEvent();
        bindData();
        return rootView;
    }

    public void InitUI(View rootView){
        img_photo_company=(ImageView) rootView.findViewById(R.id.img_photo_company);
        img_add=(ImageView) rootView.findViewById(R.id.img_add);
        txt_profile__name_company=(CustomEditextHelveticaneuelight) rootView.findViewById(R.id.txt_profile__name_company);
        editDescription=(EditText) rootView.findViewById(R.id.editDescription);
        btnSaveProfileManager=(Button) rootView.findViewById(R.id.btnSaveProfileManager);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
    }
    public void InitEvent(){
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatar();
            }
        });
        btnSaveProfileManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileRequest profileRequest= new ProfileRequest(userModel.id,userModel.apiToken,"",editDescription.getText().toString(),txt_profile__name_company.getText().toString());
                UpdateProfileAsystask updateProfileAsystask= new UpdateProfileAsystask(getActivity(),profileRequest);
                updateProfileAsystask.execute();
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                bindData();
            }
        });

        txt_profile__name_company.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {

                switch (target) {
                    case LEFT:
                        //Do something here
                        break;
                    case RIGHT:
                        if(!flag){
                            txt_profile__name_company.setEnabled(true);
                            flag=true;
                            txt_profile__name_company.setSelection(txt_profile__name_company.length());
                            txt_profile__name_company.setFocusable(true);
                            txt_profile__name_company.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil, 0);
                        }else {
                            txt_profile__name_company.setEnabled(false);
                            flag=false;
                            txt_profile__name_company.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil_inactive, 0);
                        }
                        break;
                    case TOP:
                        //Do something here
                        break;
                    case BOTTOM:
                        //Do something here
                        break;
                    default:
                        break;

                }

            }
        });
    }
    private void bindData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
        Gson gson= new Gson();
        userModel=gson.fromJson(profile,UserModel.class);

        ProfileAsystask profileAsystask= new ProfileAsystask(getActivity(),userModel);
        profileAsystask.execute();


    }
    private void changeAvatar() {
        final CharSequence[] items = {getString(R.string.takephoto), getString(R.string.gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.addPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.takephoto))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(intent, 11);
                } else if (items[item].equals(getString(R.string.gallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.selectFile)),
                            12);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_photo_company.setImageBitmap(thumbnail);
                uploadAvatar(destination.getPath());
            } else if (requestCode == 12) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                img_photo_company.setImageBitmap(bm);
                uploadAvatar(selectedImagePath);
            }
        }
    }
    private void uploadAvatar(final String path) {
        final File file = new File(path);
        if (file.exists()) {
            RequestBody requestBodyImage = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody sign = RequestBody.create(MediaType.parse("text/plain"), APICommon.getSign(APICommon.getApiKey(), "api/v1/companyprofile/postCompanyUploadFile"));
            RequestBody appid = RequestBody.create(MediaType.parse("text/plain"), APICommon.getAppId());
            RequestBody deviceType = RequestBody.create(MediaType.parse("text/plain"), APICommon.getDeviceType() + "");
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userModel.id + "");
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), userModel.apiToken);

            APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
            Call<UploadFileResponse> call = service.uploadImageProfile(sign, appid, deviceType, token, userId, requestBodyImage);
            call.enqueue(new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Response<UploadFileResponse> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // AsynTask
    class ProfileAsystask extends AsyncTask<UserModel,Void,ProfileModel> {
        ProgressDialog dialog;
        String sessionId="";
        UserModel userModel;
        Context ct;
        public ProfileAsystask(Context ct,UserModel userModel){
            this.ct=ct;
            this.userModel=userModel;
        }

        @Override
        protected ProfileModel doInBackground(UserModel... params) {
            try {
                UserController controller= new UserController();
                return controller.GetProfileCompany(userModel.id,userModel.apiToken,userModel.id);
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("" );
            dialog.show();
        }

        @Override
        protected void onPostExecute(ProfileModel code) {
            try {
                refresh.setRefreshing(false);
                if(code!=null){

                    MenuActivity.txt_numberCredits.setText(code.CreditNumber+"");

                    Gson gson= new Gson();
                    String profile=gson.toJson(code);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.KEY_COMPANY_PROFILE, profile).commit();

                    try {
                        Picasso.with(getActivity()).load(code.Logo).placeholder(R.mipmap.default_avatar).error(R.mipmap.default_avatar).into(img_photo_company);
                        txt_profile__name_company.setText(code.Name);
                        editDescription.setText(code.Overview);
                    }catch (Exception err){

                    }
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }

    class UpdateProfileAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        ProfileRequest profileRequest;
        Context ct;
        public UpdateProfileAsystask(Context ct,ProfileRequest profileRequest){
            this.ct=ct;
            this.profileRequest=profileRequest;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                UserController controller= new UserController();
                return controller.UpdateProfileCompany(profileRequest);
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("" );
            dialog.show();
        }

        @Override
        protected void onPostExecute(String code) {
            try {
                if(!code.equals("")){
                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    txt_profile__name_company.setEnabled(false);
                    flag=false;
                    txt_profile__name_company.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil_inactive, 0);
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }

}
