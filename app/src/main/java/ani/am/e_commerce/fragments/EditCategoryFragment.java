package ani.am.e_commerce.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ani.am.e_commerce.Constants;
import ani.am.e_commerce.Global;
import ani.am.e_commerce.R;
import ani.am.e_commerce.api.ApiClient;
import ani.am.e_commerce.api.ApiInterface;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.CategoryResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static ani.am.e_commerce.activites.MainActivity.prefConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCategoryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText categoryName;
    private ImageView categoryImageView;
    private Uri uri;
    private Category category;

    public EditCategoryFragment newInstance(String json) {
        Bundle bundle = new Bundle();
        bundle.putString("category", json);
        EditCategoryFragment fragment = new EditCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            String json = bundle.getString("category");
            Gson gson = new Gson();
            category = gson.fromJson(json, Category.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_category, container, false);
        init();
        fillFields();
        return view;
    }

    private void init() {
        categoryName = view.findViewById(R.id.category_name);
        categoryImageView = view.findViewById(R.id.image);
        Button choosePhoto = view.findViewById(R.id.choosePhoto);
        Button edit = view.findViewById(R.id.edit_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        choosePhoto.setOnClickListener(this);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        readBundle(getArguments());
        if (!EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE))
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), Constants.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void fillFields() {
        categoryName.setText(category.getCategoryName());
        if (category.getCategoryPicture() != " ") {
            Glide.with(this).load("http://5.9.1.58:3000/" + category.getCategoryPicture())
                    .into(categoryImageView);
        }
        uri = Uri.parse("http://5.9.1.58:3000/" + category.getCategoryPicture());
        Log.d("Tag", "Uri " + uri);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosePhoto:
                choosePhoto();
                break;
            case R.id.edit_btn:
                editCategory();
                break;
            case R.id.cancel_btn:
                fillFields();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK && null != data) {
            //String[] filePathColumn = { MediaStore.Images.Media.DATA };
            uri = data.getData();
            Log.d("Tag", "Uri " + uri);
            try {
                final InputStream imageStream = getContext().getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                categoryImageView.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.getMessage();
            }

        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void editCategory() {
        String filePath = getRealPathFromURIPath(uri, getActivity());
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        // MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("categoryPicture", file.getName(), mFile);
        //Global.showProgressDialog(getContext());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), categoryName.getText().toString());
        //Retrofit retrofit = ApiClient.getApiClient();
        //ApiInterface apiInterface = retrofit.create(ApiInterface.class);
       // String token = prefConfig.readToken("token");
         String id = category.getId();
        //Log.d("Tag", "id = " + id);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("categoryPicture\"; filename=\"" + file.getName() + "\"", mFile);
        Log.d("Tag", "categoryPicture\"; filename=\"" + file.getName() + "\"");
        map.put("categoryName", name);
        Global.categoryViewModel.updateCategory(id,map);

       /* Call<CategoryResponse> call = apiInterface.updateCategory(id, token, map);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Tag", response.body().getMessage());
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                } else {
                    Log.d("Tag", response.message());

                }
                Global.hideProgressDialog(getContext());
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.d("Tag", "Error " + t.getMessage());
                Global.hideProgressDialog(getContext());
                Toast.makeText(getContext(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void choosePhoto() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, Constants.REQUEST_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }
}
