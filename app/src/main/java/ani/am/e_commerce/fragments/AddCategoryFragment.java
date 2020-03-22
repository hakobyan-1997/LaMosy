package ani.am.e_commerce.fragments;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ani.am.e_commerce.Constants;
import ani.am.e_commerce.Global;
import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.BaseActivity;
import ani.am.e_commerce.view_models.CategoryViewModel;
import dagger.android.support.AndroidSupportInjection;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class AddCategoryFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private View view;
    private EditText categoryName;
    private ImageView categoryImageView;
    private Uri uri;
    private Button add;
    private Context context;
    @Inject
    ViewModelProvider.Factory viewModelProvider;
    CategoryViewModel categoryViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        categoryViewModel = ViewModelProviders.of(this, viewModelProvider).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_category, container, false);
        getActivity().setTitle(getString(R.string.addCategory));
        init();
        return view;
    }

    private void init() {
        categoryName = view.findViewById(R.id.category_name);
        categoryImageView = view.findViewById(R.id.image);
        categoryImageView.setOnClickListener(this);
        add = view.findViewById(R.id.add_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(this);
        if (!EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), Constants.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                choosePhoto();
                break;
            case R.id.add_btn:
                addCategory();
                break;
            case R.id.cancel_btn:
                ((BaseActivity) getActivity()).onBackPressed();
                break;
        }
    }

    private void choosePhoto() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, Constants.REQUEST_GALLERY_CODE);
    }

    private void addCategory() {
        if (!verifyFields()) {
            Toast.makeText(context, context.getString(R.string.complete_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        String filePath = Global.getRealPathFromURIPath(uri, getActivity());
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), categoryName.getText().toString());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("categoryPicture\"; filename=\"" + file.getName() + "\"", mFile);
        map.put("categoryName", name);
        categoryViewModel.addCategory(map);
        ((BaseActivity) getActivity()).onBackPressed();
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setShowCropGrid(false);
        options.setHideBottomControls(true);
        options.setToolbarTitle(getString(R.string.edit));
        options.setToolbarColor(getContext().getResources().getColor(R.color.colorPrimary));

        options.setToolbarWidgetColor(getContext().getResources().getColor(R.color.md_white_1000));
        options.setStatusBarColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .withAspectRatio(5f, 5f)
                .start(getActivity(), AddCategoryFragment.this);
    }

    private File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
        } catch (IOException e) {
            Log.d("Tag", "EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK && null != data) {
            uri = data.getData();
            File file = getImageFile();
            Uri destinationUri = Uri.fromFile(file);
            openCropActivity(uri, destinationUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            showImage(uri);
        }
    }

    private boolean verifyFields() {
        Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake);
        boolean isFilledAllFields = true;
        if (uri == null) {
            isFilledAllFields = false;
            categoryImageView.startAnimation(animShake);
        }
        if (categoryName.getText().toString().isEmpty()) {
            isFilledAllFields = false;
            categoryName.startAnimation(animShake);
        }
        return isFilledAllFields;
    }

    private void showImage(Uri imageUri) {
        uri = imageUri;
        try {
            final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            categoryImageView.setImageBitmap(selectedImage);
            add.setOnClickListener(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
