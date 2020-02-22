package ani.am.e_commerce.fragments;


import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import ani.am.e_commerce.view_models.ProductViewModel;
import dagger.android.support.AndroidSupportInjection;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class AddProductFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private EditText nameEt, priceEt, sizeEt, descriptionEt;
    private ImageView productImageView;
    private Button addBtn, cancelBtn;
    private View view;
    private Uri uri;
    private String categoryId;
    @Inject
    ViewModelProvider.Factory viewModelProvider;
    ProductViewModel productViewModel;

    public AddProductFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this );
        productViewModel = ViewModelProviders.of(this,viewModelProvider).get(ProductViewModel.class);
    }

    public static Fragment newInstance(String categoryId) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString("id", categoryId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        init();
        return view;
    }

    private void init() {
        getActivity().setTitle(getString(R.string.addProduct));
        Bundle args = getArguments();
        categoryId = args.getString("id");
        nameEt = view.findViewById(R.id.product_name_et);
        priceEt = view.findViewById(R.id.product_price_et);
        sizeEt = view.findViewById(R.id.product_size_et);
        descriptionEt = view.findViewById(R.id.product_description_et);
        productImageView = view.findViewById(R.id.product_image_view);
        addBtn = view.findViewById(R.id.add_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);

        productImageView.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        if (!EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), Constants.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_image_view:
                choosePhoto();
                break;
            case R.id.add_btn:
                addProduct();
                break;
            case R.id.cancel_btn:
                break;
        }
    }

    private void choosePhoto() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, Constants.REQUEST_GALLERY_CODE);
    }

    private void addProduct() {
        String filePath = Global.getRealPathFromURIPath(uri, getActivity());
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameEt.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), priceEt.getText().toString());
        RequestBody size = RequestBody.create(MediaType.parse("text/plain"), sizeEt.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionEt.getText().toString());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("picture\"; filename=\"" + file.getName() + "\"", mFile);
        map.put("name", name);
        map.put("price", price);
        map.put("size", size);
        map.put("stars", size);
        map.put("description", description);
        Log.d("Tag",categoryId+" "+ size+ " "+ price+" " +name);
        productViewModel.addProduct(categoryId,map);
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
                .start(getActivity(), AddProductFragment.this);
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
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            uri = data.getData();
            File file = getImageFile();
            Uri destinationUri = Uri.fromFile(file);
            openCropActivity(uri, destinationUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            showImage(uri);
        }
        Log.d("Tag", "requestCode " + requestCode + resultCode);
    }

    private void showImage(Uri imageUri) {
        uri = imageUri;
        try {
            final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            productImageView.setImageBitmap(selectedImage);
            addBtn.setOnClickListener(this);
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
