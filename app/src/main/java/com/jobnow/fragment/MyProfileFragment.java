package com.jobnow.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.adapter.LocationSpinnerAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.JobLocationResponse;
import com.jobnow.models.LoginResponse;
import com.jobnow.models.TokenRequest;
import com.jobnow.models.UpdateProfileRequest;
import com.jobnow.models.UserDetailResponse;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.newtech.jobnow.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class MyProfileFragment extends Fragment implements View.OnClickListener {
    private TextView tvEmail, tvPhoneNumber, tvGender, tvBirthday, tvCountry, tvPostalCode, tvDescription;
    private ProgressDialog progressDialog;
    private ImageView imgEditPhoneNumber, imgEditGender, imgEditBirthday, imgEditCountry, imgEditPostalCode, imgEditDescription;
    private UserModel userModel;
    private int gender = 0;
    private int keyFlag=0;
    /*public MyProfileFragment() {
        // Required empty public constructor
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initUI(view);
        bindData();
        event();
        return view;
    }

    private void event() {
        imgEditPhoneNumber.setOnClickListener(this);
        imgEditGender.setOnClickListener(this);
        imgEditBirthday.setOnClickListener(this);
        imgEditCountry.setOnClickListener(this);
        imgEditPostalCode.setOnClickListener(this);
        imgEditDescription.setOnClickListener(this);
        ProfileFragment.txtSave.setOnClickListener(this);
    }

    private void bindData() {
        loadUserDetail();
    }

    public void getApiToken() {
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        String email = sharedPreferences.getString(Config.KEY_EMAIL, "");
        Call<LoginResponse> call = service.getToken(new TokenRequest(userId, email));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                if(response.body() != null && response.body().code == 200) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.KEY_TOKEN, response.body().result.apiToken);
                    editor.commit();
                    bindData();
                } else
                    Toast.makeText(getActivity().getApplicationContext(), response.body().message,
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_connect, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void loadUserDetail() {
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, true);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt(Config.KEY_ID, 0);
        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<UserDetailResponse> call =
                service.getUserDetail(APICommon.getSign(APICommon.getApiKey(),
                                "api/v1/users/getUserDetail"),
                        APICommon.getAppId(),
                        APICommon.getDeviceType(), user_id, token, user_id);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(final Response<UserDetailResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    userModel = response.body().result;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProfileToUI(true);
                        }
                    });
                } else if(response.body().code == 503) {
                    getApiToken();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfileToUI(boolean isUpdateAvatar) {
        if (isUpdateAvatar)
            if (userModel.avatar != null) {
                Picasso.with(getActivity()).load(userModel.avatar).
                        placeholder(R.mipmap.default_avatar).
                        error(R.mipmap.default_avatar).
                        into(ProfileFragment.img_avatar);
            }
        ProfileFragment.tvName.setText(
                userModel.fullName == null || userModel.fullName.isEmpty() ? userModel.email : userModel.fullName);
        ProfileFragment.tvLocation.setText(userModel.countryName);
        tvEmail.setText(userModel.email);
        tvPhoneNumber.setText(userModel.phoneNumber);
        gender = userModel.gender;
        if (userModel.gender == 1) {
            tvGender.setText(getString(R.string.male));
        } else if (userModel.gender == 2) {
            tvGender.setText(getString(R.string.female));
        }
        tvBirthday.setText(userModel.birthDay);
        tvCountry.setText(userModel.countryName);
        tvPostalCode.setText(userModel.postalCode);
        tvDescription.setText(userModel.description);
    }

    private void initUI(View view) {
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        tvGender = (TextView) view.findViewById(R.id.tvGender);
        tvBirthday = (TextView) view.findViewById(R.id.tvBirthday);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvPostalCode = (TextView) view.findViewById(R.id.tvPostalCode);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);


        imgEditPhoneNumber = (ImageView) view.findViewById(R.id.imgEditPhoneNumber);
        imgEditGender = (ImageView) view.findViewById(R.id.imgEditGender);
        imgEditBirthday = (ImageView) view.findViewById(R.id.imgEditBirthday);
        imgEditCountry = (ImageView) view.findViewById(R.id.imgEditCountry);
        imgEditPostalCode = (ImageView) view.findViewById(R.id.imgEditPostalCode);
        imgEditDescription = (ImageView) view.findViewById(R.id.imgEditDescription);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgEditPhoneNumber:
                updateProfile(Config.TYPE_EDIT_PHONE_NUMBER);
                break;
            case R.id.imgEditGender:
                updateProfile(Config.TYPE_EDIT_GENDER);
                break;
            case R.id.imgEditBirthday:
                updateProfileBirthDay();
                break;
            case R.id.imgEditCountry:
                updateCountry();
                break;
            case R.id.imgEditPostalCode:
                updateProfile(Config.TYPE_EDIT_POSTALCODE);
                break;
            case R.id.imgEditDescription:
                updateProfile(Config.TYPE_EDIT_DESCRIPTION);
            case R.id.txtSave:
                updateProfile(0);
                keyFlag=1;
                break;
        }
    }

    Integer coutryId;
    String countryName;

    private void updateCountry() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, true);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        String url = APICommon.BASE_URL + "country/getAllCountry/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/country/getAllCountry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();
        Call<JobLocationResponse> jobLocationResponseCall = service.getJobLocation(url);
        jobLocationResponseCall.enqueue(new Callback<JobLocationResponse>() {
            @Override
            public void onResponse(final Response<JobLocationResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    if (response.body().result != null && response.body().result.size() > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.country));
                        Spinner spinner = new Spinner(getActivity());
                        final LocationSpinnerAdapter locationSpinnerAdapter = new LocationSpinnerAdapter(getActivity(), response.body().result);
                        spinner.setAdapter(locationSpinnerAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                coutryId = locationSpinnerAdapter.getItem(position).id;
                                countryName = locationSpinnerAdapter.getItem(position).Name;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        builder.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                                String phonenumber = userModel.phoneNumber;
                                String fullname = userModel.fullname == null ? "" : userModel.fullname;
                                String email = userModel.email;
                                String fb_id = userModel.fb_id == null ? "" : userModel.fb_id;
                                String birthday = userModel.birthDay;
                                String description = userModel.description == null ? "" : userModel.description;
                                int postalCode;
                                try {
                                    postalCode = Integer.parseInt(userModel.postalCode);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    postalCode = 0;
                                }
//                                String coutryID = userModel.countryID == null ? "" : userModel.countryID;
                                String coutryID = coutryId + "";
                                userModel.countryID = coutryID;
                                userModel.countryName = countryName;
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
                                String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
                                int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
                                Call<BaseResponse> call =
                                        service.postUpdateDetail(new UpdateProfileRequest(fullname, email, birthday, gender, postalCode, description, phonenumber, fb_id, token, userId, coutryID));
                                call.enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {

                                        if (response.body() != null) {
                                            if (response.body().code == 200) {
                                                setProfileToUI(false);
                                            }
                                            Toast.makeText(getActivity(), /*response.body().message*/"Profile updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        spinner.setLayoutParams(lp);
                        builder.setView(spinner);
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileBirthDay() {
        String birthday = userModel.birthDay;
        int year, month, day;
        if (birthday.equals("0000-00-00")) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            String dateArr[] = birthday.split("-");
            year = Integer.parseInt(dateArr[0]);
            month = Integer.parseInt(dateArr[1]) - 1;
            day = Integer.parseInt(dateArr[2]);
        }
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                String phonenumber = userModel.phoneNumber;
                String fullname = userModel.fullname == null ? "" : userModel.fullname;
                String email = userModel.email;
                String fb_id = userModel.fb_id == null ? "" : userModel.fb_id;
                final String birthday = Utils.formatStringTime(dayOfMonth, monthOfYear + 1, year);

                String description = userModel.description == null ? "" : userModel.description;
                int postalCode;
                try {
                    postalCode = Integer.parseInt(userModel.postalCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    postalCode = 0;
                }
                String coutryID = userModel.countryID == null ? "" : userModel.countryID;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
                int userId = sharedPreferences.getInt(Config.KEY_ID, 0);

                Call<BaseResponse> call =
                        service.postUpdateDetail(new UpdateProfileRequest(fullname, email, birthday, gender, postalCode, description, phonenumber, fb_id, token, userId, coutryID));
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {

                        if (response.body() != null) {
                            if (response.body().code == 200) {
                                userModel.birthDay = birthday;
                                setProfileToUI(false);
                            }
                            Toast.makeText(getActivity(), /*response.body().message*/"Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, year, month, day).show();
    }

    private void addItemToRadioGroup(final String name, final int radio_id, RadioGroup radioGroup) {
        RadioButton radioButtonView = new RadioButton(getActivity());
        radioButtonView.setText(name);
        radioGroup.addView(radioButtonView);
        if (radio_id == gender) {
            radioButtonView.setChecked(true);
        }
        radioButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender != radio_id) {
                    gender = radio_id;
                }
            }
        });
    }

    private void updateProfile(final int type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final RadioGroup radioGroup = new RadioGroup(getActivity());

        if (type == Config.TYPE_EDIT_PHONE_NUMBER) {
            builder.setTitle(getString(R.string.phoneNumber));
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            input.setHint(getString(R.string.phone_number));
            input.setLayoutParams(lp);
            input.setText(userModel.phoneNumber);
            builder.setView(input);
        } else if (type == Config.TYPE_EDIT_GENDER) {
            builder.setTitle(getString(R.string.gender));
            radioGroup.setLayoutParams(lp);
            final RadioButton[] rb = new RadioButton[3];
            addItemToRadioGroup(getString(R.string.male), 1, radioGroup);
            addItemToRadioGroup(getString(R.string.female), 2, radioGroup);
            builder.setView(radioGroup);
        } else if (type == Config.TYPE_EDIT_POSTALCODE) {
            builder.setTitle(getString(R.string.postalCode));
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint(getString(R.string.postalCode));
            input.setLayoutParams(lp);
            input.setText(userModel.postalCode);
            builder.setView(input);
        } else if (type == Config.TYPE_EDIT_DESCRIPTION) {
            builder.setTitle(getString(R.string.description));
            input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            input.setLines(5);
            input.setMaxLines(10);
            input.setHint(getString(R.string.description));
            lp.gravity = Gravity.LEFT | Gravity.TOP;
            input.setLayoutParams(lp);
            input.setText(userModel.description);
            builder.setView(input);
        }
        builder.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                String phonenumber = userModel.phoneNumber;
                String fullname = /*userModel.fullname == null ? "" : userModel.fullname*/ProfileFragment.tvName.getText().toString();
                String email = userModel.email;
                final String fb_id = userModel.fb_id == null ? "" : userModel.fb_id;
                String birthday = userModel.birthDay;
                String description = userModel.description == null ? "" : userModel.description;
                int postalCode;
                try {
                    postalCode = Integer.parseInt(userModel.postalCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    postalCode = 0;
                }
                String coutryID = userModel.countryID == null ? "" : userModel.countryID;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
                int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
                if (type == Config.TYPE_EDIT_PHONE_NUMBER) {
                    phonenumber = input.getText().toString();
                    userModel.phoneNumber = phonenumber;
                } else if (type == Config.TYPE_EDIT_GENDER) {
                    userModel.gender = gender;
                } else if (type == Config.TYPE_EDIT_POSTALCODE) {
                    try {
                        postalCode = Integer.parseInt(input.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        postalCode = 0;
                    }
                    userModel.postalCode = postalCode + "";
                } else if (type == Config.TYPE_EDIT_DESCRIPTION) {
                    description = input.getText().toString();
                    userModel.description = description;
                }
                Call<BaseResponse> call =
                        service.postUpdateDetail(new UpdateProfileRequest(fullname, email, birthday, gender, postalCode, description, phonenumber, fb_id, token, userId, coutryID));
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {

                        if (response.body() != null) {
                            if (response.body().code == 200) {
                                if(keyFlag==1) {
                                    loadUserDetail();
                                    ProfileFragment.flag=false;
                                    ProfileFragment.tvName.setEnabled(false);
                                    ProfileFragment.txtSave.setVisibility(View.GONE);
                                    ProfileFragment.txtCancel.setVisibility(View.GONE);
                                }else {
                                    setProfileToUI(false);
                                }
                            }
                            Toast.makeText(getActivity(),/* response.body().message*/"Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
