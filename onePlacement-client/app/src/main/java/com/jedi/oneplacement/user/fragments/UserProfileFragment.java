package com.jedi.oneplacement.user.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jedi.oneplacement.R;
import com.jedi.oneplacement.activities.EntryActivity;
import com.jedi.oneplacement.activities.MainActivity;
import com.jedi.oneplacement.databinding.FragmentUserProfileBinding;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.data.UserInstance;

import org.modelmapper.ModelMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    boolean isEditable = true;
    FragmentUserProfileBinding mBinding;
    Boolean editable;
    Uri contentURI = null;
    Bitmap b = null;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    private ActivityResultLauncher<Intent> mChooseFromGalleryLauncher;
    private ActivityResultLauncher<Intent> mChooseFromStorageLauncher;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        b = Cache.readImgFromCache(getContext(), 0);
        if (b != null) mBinding.profilePhoto.setImageBitmap(b);
        requireActivity().findViewById(R.id.bottom_nav).setVisibility(View.GONE);

        mBinding.layout.toolBar.setNavigationIcon(R.drawable.ic_arrow_back_svgrepo_com);
        mBinding.layout.userPhoto.setVisibility(View.GONE);
        setUserDetails();
        isEditable = UserInstance.getProfileStatus().matches(AppConstants.VERIFIED) ? false : true;
        if (!isEditable) {
            mBinding.warning.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "onCreateView: " + isEditable);

        mBinding.layout.toolBar.setNavigationOnClickListener(v -> {
            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, true).build();
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.homeFragment, null, navOptions);
            MainActivity.setCheckz();
            requireActivity().findViewById(R.id.bottom_nav).setVisibility(View.VISIBLE); // todo: fix this transition
        });

        toggleEditables(false);

        mBinding.editImg.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: haa haaa");
            choosePhotoFromGallery();
        });

        mBinding.logOutBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(AppConstants.JWT, AppConstants.JEDI).apply();
            sharedPreferences.edit().putString(AppConstants.DEV_TOKEN, AppConstants.JEDI).apply();
            Cache.ClearCache(requireContext());

            Intent intent = new Intent(requireActivity(), EntryActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        mBinding.editView.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: hehehehe");
            if (!editable) {
                mBinding.editView.setText("Save");
                mBinding.resume.setText("UPLOAD RESUME");
                editable = true;
                if (isEditable) {
                    mBinding.editImg.setVisibility(View.VISIBLE);
                    toggleEditables(true);
                }
            } else {
                mBinding.resume.setEnabled(true);
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
                String jwt = sharedPreferences.getString(AppConstants.JWT, null);

                ApiImpl.checkUser(jwt, new ApiImpl.ApiCallListener<Map<String, Object>>() {
                    @Override
                    public void onResponse(Map<String, Object> response) {
                        //todo: // send request to server: to save the image and update user details AW;
                        String branch = mBinding.userBranch.getText().toString();
                        String email = mBinding.email.getText().toString();
                        String mno = mBinding.mNumber.getText().toString();

                        // get img file from cache:
                        File file = new File(getContext().getCacheDir(), AppConstants.USER_PHOTO_BAK); // creates an instance of the file located at the "specified" Location;

                        if (file.exists()) {
                            // we don't need to read from the file, we need to send the file by creating RequestBody's object;
                            RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), file); // todo: try changing the MediaType.
                            MultipartBody.Part formDataImg = MultipartBody.Part.createFormData("image", file.getName(), requestBodyImg);

                            ApiImpl.uploadImg(jwt, formDataImg, new ApiImpl.ApiCallListener<FileResponse>() {
                                @Override
                                public void onResponse(FileResponse response) {
                                    Cache.updateImgCache(requireContext());
                                }

                                @Override
                                public void onFailure(int code) {
                                    if (code == -1)
                                        Toast.makeText(requireContext(), code + ": Error could not upload Image, check your Internet.", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(requireContext(), code + ": Bad Request..", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        File mResume = new File(getContext().getCacheDir(), AppConstants.RESUME_BAK); // creates an instance of the file located at the "specified" Location;
                        if (mResume.exists()) {
                            RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), mResume); // todo: try changing the MediaType.
                            MultipartBody.Part formDataResume = MultipartBody.Part.createFormData("resume", file.getName(), requestBodyImg);

                            ApiImpl.uploadResume(jwt, formDataResume, new ApiImpl.ApiCallListener<FileResponse>() {
                                @Override
                                public void onResponse(FileResponse response) {
                                    Cache.updateResCache(requireContext());
                                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int code) {
                                    Toast.makeText(requireContext(), code + " Error in uploading resume.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        if (isEditable) {
                            ApiImpl.updateUser(jwt, branch, email, mno, new ApiImpl.ApiCallListener<UserDto>() {
                                @Override
                                public void onResponse(UserDto response) {
                                    UserInstance.setUserInstance(new ModelMapper().map(response, User.class));
                                    Log.d(TAG, "onResponse: " + response.toString());
                                    toggleEditables(false);
                                }

                                @Override
                                public void onFailure(int code) {
                                    Log.d(TAG, "onFailure: code: " + code);
                                    if (code == -1)
                                        Toast.makeText(requireContext(), code + ": Error could not update Details, check your Internet.", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(requireContext(), code + ": Bad Request..", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            toggleEditables(false);
                    }

                    @Override
                    public void onFailure(int code) {
                        // redirect to login page:
                        Toast.makeText(requireContext(), "Session Timed Out...", Toast.LENGTH_SHORT).show();
                        UserInstance.sessionOver(requireContext(), UserProfileFragment.this);
                    }
                });
            }
        });

        mBinding.resume.setOnClickListener(v -> {
            if (editable) {
                // todo: upload resume
                chooseFileFromStorage();
            } else {
                Bundle result = new Bundle();
                result.putInt("userId", 0);
                requireActivity().getSupportFragmentManager().setFragmentResult("userIdKey", result);
                // todo: view resume:
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.pdfFragment);
            }
        });

        mChooseFromStorageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data == null) return;
                        Uri uri = data.getData();
                        Log.d(TAG, "onCreateView: " + uri.getPath());

                        //todo: revise this, incredibly important JAVA Files.
                        new Thread(() -> {
                            try {
                                InputStream is = getContext().getContentResolver().openInputStream(uri);
                                OutputStream os = new FileOutputStream(new File(getContext().getCacheDir(), AppConstants.RESUME_BAK));

                                FileUtils.copy(is, os);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            new Handler(Looper.getMainLooper()).post(() -> {
                                mBinding.resume.setText("PDF SELECTED");
                                mBinding.resume.setEnabled(false);
                            });
                        }).start();
                    }
                }
        );

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
                                b.compress(Bitmap.CompressFormat.WEBP, 100, fos);
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

    private void chooseFileFromStorage() {
        Intent pdfIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pdfIntent.setType("application/pdf");
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);

        mChooseFromStorageLauncher.launch(pdfIntent);
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
        if (!b) {
            mBinding.editView.setText("Edit");
            mBinding.resume.setText("VIEW RESUME");
            mBinding.editImg.setVisibility(View.INVISIBLE);
        }

        mBinding.userBranch.setEnabled(b);
        mBinding.email.setEnabled(b);
        mBinding.mNumber.setEnabled(b);
        editable = b;
    }
}