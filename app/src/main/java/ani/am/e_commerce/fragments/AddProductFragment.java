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
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
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
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.view_models.ProductViewModel;
import dagger.android.support.AndroidSupportInjection;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

import static ani.am.e_commerce.Constants.BASE_URL;

public class AddProductFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private EditText nameEt, priceEt, sizeEt, countEt, descriptionEt;
    private ImageView productImageView;
    private Button addBtn, cancelBtn;
    private View view;
    private Uri uri;
    private String categoryId;
    private Context context;
    private Product product;
    private boolean isUpdate = false;

    @Inject
    ViewModelProvider.Factory viewModelProvider;
    ProductViewModel productViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel = ViewModelProviders.of(this, viewModelProvider).get(ProductViewModel.class);
    }

    public static Fragment newInstance(String categoryId) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString("id", categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddProductFragment newInstance(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        AddProductFragment fragment = new AddProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        init();
        return view;
    }

    private void init() {
        nameEt = view.findViewById(R.id.product_name_et);
        priceEt = view.findViewById(R.id.product_price_et);
        sizeEt = view.findViewById(R.id.product_size_et);
        countEt = view.findViewById(R.id.product_count_et);
        descriptionEt = view.findViewById(R.id.product_description_et);
        productImageView = view.findViewById(R.id.product_image_view);
        addBtn = view.findViewById(R.id.add_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);

        productImageView.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        Bundle args = getArguments();
        if (args.getString("id") != null) {
            isUpdate = false;
            categoryId = args.getString("id");
            getActivity().setTitle(getString(R.string.addProduct));
            addBtn.setText(getString(R.string.add));
        } else {
            isUpdate = true;
            product = (Product) args.getSerializable("product");
            getActivity().setTitle(getString(R.string.edit));
            addBtn.setText(getString(R.string.edit));
            fillFields();
        }

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
            case R.id.product_image_view:
                choosePhoto();
                break;
            case R.id.add_btn:
                if (!isUpdate)
                    addProduct();
                else
                    editProduct();
                break;
            case R.id.cancel_btn:
                getActivity().onBackPressed();
                break;
        }
    }

    private void choosePhoto() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, Constants.REQUEST_GALLERY_CODE);
    }

    private void addProduct() {
        if (!verifyFields()) {
            Toast.makeText(context, context.getString(R.string.complete_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        String filePath = Global.getRealPathFromURIPath(uri, getActivity());
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameEt.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), priceEt.getText().toString());
        RequestBody size = RequestBody.create(MediaType.parse("text/plain"), sizeEt.getText().toString());
        RequestBody count = RequestBody.create(MediaType.parse("text/plain"), countEt.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionEt.getText().toString());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("productPicture\"; filename=\"" + file.getName() + "\"", mFile);
        map.put("productName", name);
        map.put("productPrice", price);
        map.put("productStars", size);
        map.put("productItemsCount", count);
        map.put("productDescription", description);
        productViewModel.addProduct(categoryId, map);
        Global.hideKeyboard(getActivity());
        getActivity().onBackPressed();
    }

    private void editProduct() {
        if (!verifyFields()) {
            Toast.makeText(context, context.getString(R.string.complete_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        String filePath = Global.getRealPathFromURIPath(uri, getActivity());
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameEt.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), priceEt.getText().toString());
        RequestBody size = RequestBody.create(MediaType.parse("text/plain"), sizeEt.getText().toString());
        RequestBody count = RequestBody.create(MediaType.parse("text/plain"), countEt.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionEt.getText().toString());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("picture\"; filename=\"" + file.getName() + "\"", mFile);
        map.put("name", name);
        map.put("price", price);
        map.put("size", size);
        map.put("stars", size);
        map.put("count", count);
        map.put("description", description);
        Log.d("Tag", categoryId + " " + size + " " + price + " " + name);
        productViewModel.updateProduct(product.getCategoryId(), map);
        Global.hideKeyboard(getActivity());
        getActivity().onBackPressed();
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

    private boolean verifyFields() {
        Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake);
        boolean isFilledAllFields = true;
        if (uri == null) {
            isFilledAllFields = false;
            productImageView.startAnimation(animShake);
        }
        if (nameEt.getText().toString().isEmpty()) {
            isFilledAllFields = false;
            nameEt.startAnimation(animShake);
        }
        if (priceEt.getText().toString().isEmpty()) {
            isFilledAllFields = false;
            priceEt.startAnimation(animShake);
        }
        if (sizeEt.getText().toString().isEmpty()) {
            isFilledAllFields = false;
            sizeEt.startAnimation(animShake);
        }
        if (countEt.getText().toString().isEmpty()) {
            isFilledAllFields = false;
            countEt.startAnimation(animShake);
        }
        if (descriptionEt.getText().toString().isEmpty()) {
            isFilledAllFields = false;
            descriptionEt.startAnimation(animShake);
        }
        return isFilledAllFields;
    }

    private void fillFields() {
        if (product == null)
            return;
        nameEt.setText(product.getName());
        priceEt.setText(String.valueOf(product.getPrice()));
        sizeEt.setText(String.valueOf(product.getSize()));
        countEt.setText(String.valueOf(product.getCount()));
        descriptionEt.setText(product.getDescription());
        String path = BASE_URL + "/" + product.getPicture();
        path = path.replace("\\", "/");
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(productImageView);
        uri = Uri.parse(path);
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
