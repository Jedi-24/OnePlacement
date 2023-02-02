package com.jedi.oneplacement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.databinding.FragmentUserProfileBinding;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

import org.modelmapper.ModelMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    FragmentUserProfileBinding mBinding;
    Boolean editable;
    Uri contentURI = null;
    Bitmap b = null;

    public interface BackListener{
        void onBack();
    }

    public UserProfileFragment() {
        // Required empty public constructor
    }

    private ActivityResultLauncher<Intent> mChooseFromGalleryLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        b = Cache.readFromCache(getContext());
        if (b != null) {
            mBinding.profilePhoto.setImageBitmap(b);
            Toast.makeText(requireContext(), "Image is loaded from Internal Cache!", Toast.LENGTH_SHORT).show();
        }
        mBinding.layout.toolBar.setNavigationIcon(R.drawable.ic_arrow_back_svgrepo_com);
        mBinding.layout.userPhoto.setVisibility(View.GONE);
        setUserDetails();

        mBinding.layout.toolBar.setNavigationOnClickListener(v->{
            // todo:-->
        });

        toggleEditables(false);

        mBinding.editImg.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: haa haaa");
            choosePhotoFromGallery();
        });

        mBinding.logOutBtn.setOnClickListener(v->{
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(AppConstants.JWT, "Jedi_24").apply();

            Cache.removeImgFromCache(requireContext());

            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.loginFragment, null, navOptions);
        });

        mBinding.editView.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: hehehehe");
            if (!editable) {
                mBinding.editView.setText("Save");
                mBinding.editImg.setVisibility(View.VISIBLE);
                toggleEditables(true);
            } else {
                mBinding.editView.setText("Edit");
                mBinding.editImg.setVisibility(View.INVISIBLE);
                //todo: // send request to server: to save the image and update user details AW;

                String branch = mBinding.userBranch.getText().toString();
                String email = mBinding.email.getText().toString();
                String mno = mBinding.mNumber.getText().toString();

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(AppConstants.JWT, null);

                // get img file from cache:
                File folder = getContext().getCacheDir();
                File file = new File(folder, AppConstants.USER_PHOTO_BAK); // creates an instance of the file located at the "specified" Location;

                if(file.exists()){
                    Log.d(TAG, "onCreateView: " + file.getAbsolutePath());
                    Log.d(TAG, "onCreateView: haa yehi " + file.getName());

                    // we don't need to read from the file, we need to send the file by creating RequestBody's object;
                    RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), file); // todo: try changing the MediaType.
                    Log.d(TAG, "onCreateView: " + requestBodyImg);
                    MultipartBody.Part formDataImg = MultipartBody.Part.createFormData("image", file.getName(), requestBodyImg);

                    ApiImpl.uploadImg(token, formDataImg,new ApiImpl.ApiCallListener<FileResponse>() {
                        @Override
                        public void onResponse(FileResponse response) {
                            Log.d(TAG, "onResponse: idhar aao");
                            Log.d(TAG, "onResponse: " + response.toString());
                            // todo: update cache.
                            Cache.updateCache(requireContext());
                        }

                        @Override
                        public void onFailure(int code) {
                        }
                    });
                }

                ApiImpl.updateUser(token, branch, email, mno, new ApiImpl.ApiCallListener<UserDto>() {
                    @Override
                    public void onResponse(UserDto response) {
                        UserInstance.setUserInstance(new ModelMapper().map(response, User.class));
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                    @Override
                    public void onFailure(int code) {
                        Toast.makeText(getContext(), "failed to update user details", Toast.LENGTH_SHORT).show();
                    }
                });

                toggleEditables(false);
            }
        });

        mChooseFromGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult:  haa bhai ");
                        Intent data = result.getData();
                        if (data == null) return;

                        contentURI = data.getData();
                        new Thread(() -> {
                            try (FileOutputStream fos = new FileOutputStream(
                                    new File(requireContext().getCacheDir(), AppConstants.USER_PHOTO_BAK))
                            ) {
                                b = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                b.recycle();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();

                        mBinding.profilePhoto.setImageURI(contentURI);
                    }
                });
        return mBinding.getRoot();
    }

    private void setUserDetails() {
        mBinding.name.setText(UserInstance.getName());
        mBinding.regNumber.setText(UserInstance.getRegNo());
        mBinding.userBranch.setText(UserInstance.getBranch());
        mBinding.email.setText(UserInstance.getEmail());
        mBinding.mNumber.setText(UserInstance.getPhoneNumber());
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mChooseFromGalleryLauncher.launch(galleryIntent);
    }

    private void toggleEditables(Boolean b) {
        mBinding.userBranch.setEnabled(b);
        mBinding.email.setEnabled(b);
        mBinding.mNumber.setEnabled(b);
        editable = b;
    }
}