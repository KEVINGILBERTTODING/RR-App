package com.example.rumahraga.ui.fragments.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentProfileBinding;
import com.example.rumahraga.model.CityModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.UserModel;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.city.CityViewModel;
import com.example.rumahraga.viewmodel.user.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String userId, username, name, cityName;
    private UserViewModel userViewModel;
    private List<CityModel> cityModelList;
    private BottomSheetBehavior bottomSheetBehaviorCity, bottomSheetBehaviorUpdtUsername, bottomSheetBehaviorUpdtPwd;
    private CityViewModel cityViewModel;
    private SearchableSpinner searchableSpinnerCity;
    private ArrayList<String> arrayListCity = new ArrayList<>();
    private Boolean isTextInputLayoutClicked = false;
    private File file;
    private ImageView imagePicker;
    private boolean isFile;

    private Dialog dialogUploading, dialogUpdatePhotoProfile;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

   

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserData();
        getCity();


        binding.tvUsername.setText(username);
        binding.etUsername.setText(username);

        // init bottom sheet
        initBottomSheetCity();
        initBottomSheetUpdtUsernme();
        initBottomSheetUpdtPwd();
        bottomSheetBehaviorCity.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviorUpdtUsername.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviorUpdtPwd.setState(BottomSheetBehavior.STATE_HIDDEN);
        listener();
    }

    private void listener() {
        binding.swipeReresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserData();
                getCity();
                binding.swipeReresh.setRefreshing(false);
            }
        });
        binding.cvCity.setOnClickListener(view -> {
            showBtmSheetCity();
        });
        binding.btnSaveMyLocation.setOnClickListener(view -> {
            if (cityName != null) {
                saveSharedPref(ConsSharedPref.CITY, cityName);
                saveMyLocation();
            }
        });

        binding.cvUpdateUsername.setOnClickListener(view -> {
            showBtmSheetUpdtUsernmae();
        });


        binding.btnUpdateUsername.setOnClickListener(view -> {
            updateUsername();
        });

        binding.cvPwdUpdate.setOnClickListener(view -> {
            showBtmSheetUpdtPwd();
        });

        binding.btnUpdatePassword.setOnClickListener(view -> {
            updatePassword();
        });

        binding.vOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBtmSheetCity();
                hideBtmSheetUpdtPwd();
                hideBtmSheetUpdtUsernmae();
            }
        });

        binding.profileImage.setOnClickListener(view -> {
            showUpadateProfileDialog();
        });






    }



    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, "");
        username = sharedPreferences.getString(ConsSharedPref.USERNAME, "");
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString(ConsSharedPref.NAME, "");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);


    }

    private void getUserData() {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.profileImage.setVisibility(View.GONE);
        userViewModel.getUser(userId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
            @Override
            public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                if (userModelResponseModel.isStatus() == true) {
                    Glide.with(getContext()).load(userModelResponseModel.getData().getProfile_picture())
                            .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                            .into(binding.profileImage);


                    binding.shimmer.setVisibility(View.GONE);
                    binding.profileImage.setVisibility(View.VISIBLE);

                }else {
                    showToast(ConsOther.TOAST_ERR, ConsOther.TOAST_ERR);
                }
            }
        });

    }

    private void getCity() {
        cityViewModel.getAllCity().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CityModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CityModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true && listResponseModel != null) {
                    for (CityModel item : listResponseModel.getData()) {
                        arrayListCity.add(item.getNama());
                    }
                    initSpinnerLocation();


                }else {
                    showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                    binding.btnSaveMyLocation.setEnabled(false);
                }
            }
        });
    }

    private void initBottomSheetCity() {
        bottomSheetBehaviorCity = BottomSheetBehavior.from(binding.bottomSheetCity);
        bottomSheetBehaviorCity.setHideable(true);
        bottomSheetBehaviorCity.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBtmSheetCity();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBtmSheetCity() {
        bottomSheetBehaviorCity.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideBtmSheetCity() {
        bottomSheetBehaviorCity.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }


    private void initBottomSheetUpdtUsernme() {
        bottomSheetBehaviorUpdtUsername = BottomSheetBehavior.from(binding.bottomUpdateUsername);
        bottomSheetBehaviorUpdtUsername.setHideable(true);
        bottomSheetBehaviorUpdtUsername.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBtmSheetUpdtUsernmae();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBtmSheetUpdtUsernmae() {
        bottomSheetBehaviorUpdtUsername.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideBtmSheetUpdtUsernmae() {
        bottomSheetBehaviorUpdtUsername.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }


    private void initBottomSheetUpdtPwd() {
        bottomSheetBehaviorUpdtPwd = BottomSheetBehavior.from(binding.bottomUpdatePassword);
        bottomSheetBehaviorUpdtPwd.setHideable(true);
        bottomSheetBehaviorUpdtPwd.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBtmSheetUpdtPwd();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBtmSheetUpdtPwd() {
        bottomSheetBehaviorUpdtPwd.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideBtmSheetUpdtPwd() {
        bottomSheetBehaviorUpdtPwd.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        binding.etNewPassword.setText("");
        binding.etOldPassword.setText("");
    }

    private void initSpinnerLocation() {


        searchableSpinnerCity = new SearchableSpinner(getContext());
        searchableSpinnerCity.setWindowTitle("Pilih Kota Anda");
        searchableSpinnerCity.setSpinnerListItems(arrayListCity);

        // listener spinner
        searchableSpinnerCity.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                if (isTextInputLayoutClicked) {
                    binding.textInputSpinnerCity.getEditText().setText(selectedString);
                    cityName = selectedString;
                    showToast(ConsOther.TOAST_NORMAL, selectedString);
                }


            }
        });

        binding.textInputSpinnerCity.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTextInputLayoutClicked = true;
                searchableSpinnerCity.setHighlightSelectedItem(true);
                searchableSpinnerCity.show();

            }
        });


    }

    private void saveMyLocation() {
        userViewModel.updateLocation(userId, cityName).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if (responseModel.isStatus() == true) {
                    showToast(ConsOther.TOAST_SUCCESS, "Berhasil mengubah lokasi");
                    hideBtmSheetCity();
                }else {
                    showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                }
            }
        });
    }

    private void showUpadateProfileDialog(){
        dialogUpdatePhotoProfile = new Dialog(getContext());
        dialogUpdatePhotoProfile.setContentView(R.layout.dialog_update_photo_profile);
        dialogUpdatePhotoProfile.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        imagePicker = dialogUpdatePhotoProfile.findViewById(R.id.ivUpdateProfile);
        final ImageButton btnClose  = dialogUpdatePhotoProfile.findViewById(R.id.btnDialogClose);
        final Button btnUpdatePhotoProfile = dialogUpdatePhotoProfile.findViewById(R.id.btnUpdatePhotoProfile);

        dialogUpdatePhotoProfile.show();

        // listener
        imagePicker.setOnClickListener(view -> {
            checkPermissionExternalStorage();
        });

        btnClose.setOnClickListener(view -> {
            dialogUpdatePhotoProfile.dismiss();
        });

        btnUpdatePhotoProfile.setOnClickListener(view -> {
            updatePhotoProfile();
        });


    }

    private void updateUsername() {
        if (binding.etUsername.getText().toString().isEmpty()) {
            showToast(ConsOther.TOAST_ERR, "Username tidak boleh kosong");
        }else if (binding.etUsername.getText().toString().matches("[0-9]+")) {
            showToast(ConsOther.TOAST_ERR, "Username tidak boleh mengandung angka");
         }else if (userId == null) {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
         }  else {
            showDialogUploading();
            userViewModel.updateUsername(binding.etUsername.getText().toString(), userId)
                    .observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(ResponseModel responseModel) {
                            dialogUploading.dismiss();
                            if (responseModel.isStatus() == true) {
                                saveSharedPref(ConsSharedPref.USERNAME, binding.etUsername.getText().toString());
                                binding.tvUsername.setText(binding.etUsername.getText().toString());
                                binding.etUsername.setText(binding.etUsername.getText().toString());
                                hideBtmSheetUpdtUsernmae();
                                showToast(ConsOther.TOAST_SUCCESS, "Berhasil mengubah username");
                            }else {
                                showToast(ConsOther.TOAST_ERR, responseModel.getMessage());
                            }
                        }
                    });

        }
    }

    private void updatePassword() {
        if (binding.etOldPassword.getText().toString().isEmpty()) {
            showToast(ConsOther.TOAST_ERR, "Kata sandi lama tidak boleh kosong");
        }else if (binding.etNewPassword.getText().toString().isEmpty()){
            showToast(ConsOther.TOAST_ERR, "Kata sandi baru tidak boleh kosong");

        }else if (userId == null) {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }else {
            showDialogUploading();
            userViewModel.updatePassword(
                    userId, binding.etOldPassword.getText().toString(),
                    binding.etNewPassword.getText().toString()
            ).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel responseModel) {
                    dialogUploading.dismiss();
                    if (responseModel.isStatus() == true) {
                        hideBtmSheetUpdtPwd();
                        showToast(ConsOther.TOAST_SUCCESS, "Berhasil mengubah kata sandi");
                    }else {
                        showToast(ConsOther.TOAST_ERR, responseModel.getMessage());
                    }
                }
            });
        }
    }

    private void updatePhotoProfile() {
        if (userId != null && file != null && isFile == true) {
            showDialogUploading();


            HashMap map = new HashMap();
            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);


            userViewModel.updateProfilePhoto(map, filePart).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel responseModel) {
                    dialogUploading.dismiss();
                    if (responseModel.isStatus() == true) {
                        showToast(ConsOther.TOAST_SUCCESS, "Berhasil mengubah foto profil");
                        dialogUpdatePhotoProfile.dismiss();
                        getUserData();
                    }else {
                        showToast(ConsOther.TOAST_ERR, responseModel.getMessage());

                    }
                }
            });
        }else{
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }

    }


    private void showToast(String type, String message) {
        if (type.equals(ConsOther.TOAST_SUCCESS)) {
            Toasty.success(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else if (type.equals(ConsOther.TOAST_NORMAL)){
            Toasty.normal(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), message, Toasty.LENGTH_SHORT).show();

        }
    }

    private void saveSharedPref(String sharedName, String value) {
        editor.putString(sharedName, value);
        editor.apply();
    }

    private void showDialogUploading() {
        dialogUploading = new Dialog(getContext());
        dialogUploading.setContentView(R.layout.dialog_uploading);
        dialogUploading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogUploading.show();
    }


    private void checkPermissionExternalStorage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getContentLauncher.launch("image/*");
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast( ConsOther.TOAST_ERR,"Izin dibutuhkan untuk mengakses galeri");
            }
            // Meminta izin READ_EXTERNAL_STORAGE.
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }


    private ActivityResultLauncher<String> getContentLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    Uri uri = result;
                    String pdfPath = getRealPathFromUri(uri);
                    file = new File(pdfPath);
                    imagePicker.setVisibility(View.VISIBLE);
                    imagePicker.setImageURI(uri);
                    isFile = true;



                } else {
                    showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                    isFile = false;

                }
            }
    );

    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        if (getContext().getContentResolver() != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                File file = new File(getContext().getCacheDir(), getFileName(uri));
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}