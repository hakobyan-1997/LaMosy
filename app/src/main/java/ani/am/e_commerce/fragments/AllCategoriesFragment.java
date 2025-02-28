package ani.am.e_commerce.fragments;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ani.am.e_commerce.Global;
import ani.am.e_commerce.R;

import ani.am.e_commerce.adapters.UserCategoryAdapter;
import ani.am.e_commerce.adapters.UserProductAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.db.entity.Product;
import ani.am.e_commerce.interfaces.CustomOnClickListener;
import ani.am.e_commerce.view_models.CategoryViewModel;
import ani.am.e_commerce.view_models.ProductViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static ani.am.e_commerce.Constants.REQ_CODE_SPEECH_INPUT;
import static ani.am.e_commerce.activities.MainActivity.prefConfig;

public class AllCategoriesFragment extends Fragment implements RecognitionListener, CustomOnClickListener {
    private View view;
    private SearchView searchView;
    private TextView errorTv;
    private ImageButton mSpeakBtn;
    private Context context;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private ProgressBar progressBar;
    private boolean isAnimated;
    private List<Category> categoryList;
    private List<Product> productList;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.category_rv)
    RecyclerView categoryRv;
    @BindView(R.id.products_rv)
    RecyclerView productRv;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static AllCategoriesFragment newInstance(String href){
        AllCategoriesFragment fragment = new AllCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("href",href);
        fragment.setArguments(bundle);
        return fragment;
    }

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);

    }*/

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        isAnimated = false;
        Global.productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);
        Global.categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
        Global.categoryViewModel.getCategoriesList().observe(this, categories -> updateUI(categories));
    }

    private void updateUI(@Nullable List<Category> categoryList) {
        Log.d("Tag", "all categories list  update " + categoryList.size());
        if (categoryList != null) {
            createArrayList(categoryList);
            this.categoryList = categoryList;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_category, container, false);
        getActivity().setTitle(getString(R.string.app_name));
        ButterKnife.bind(this, view);
        init();
        //getAllCategories();
        return view;
    }

    private void init() {
        final Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        errorTv = view.findViewById(R.id.error_tv);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Tag", "Search : " + s);
                Global.hideKeyboard(getActivity());
                if (!s.isEmpty())
                    searchByName(s);
                else
                    createArrayList(categoryList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty())
                    searchByName(s);
                else
                    createArrayList(categoryList);
                return false;
            }
        });
        mSpeakBtn = (ImageButton) view.findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(v -> startVoiceInput());
        ImageView loginBtn = view.findViewById(R.id.login_btn);
        if (prefConfig.readLoginStatus()) {
            loginBtn.setVisibility(View.GONE);
        } else {
            loginBtn.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(view1 -> {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
            });
            String href = getArguments().getString("href");
            if(href != null && href.equals("login")){
                loginBtn.performClick();
            }
        }
    }

    private void startVoiceInput() {
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hy");
        //recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"hy"});
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, REQ_CODE_SPEECH_INPUT);
        speech.startListening(recognizerIntent);
        errorTv.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void createArrayList(List<Category> list) {
        UserCategoryAdapter adapter = new UserCategoryAdapter(list, null, false);
        categoryRv.setAdapter(adapter);
        categoryRv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        if (!isAnimated) {
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
            categoryRv.setLayoutAnimation(animation);
            isAnimated = true;
        }
    }

    private void createSearchedList(List<Product> list) {
        productList = list;
        UserProductAdapter adapter = new UserProductAdapter(list, this, false);
        productRv.setAdapter(adapter);
        productRv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
        productRv.setLayoutAnimation(animation);

    }

    private void searchByName(String s) {
        translate(s);
        Global.categoryViewModel.searchCategory(s).observe(this, categoryList -> {
            Log.d("Tag", "categoryList " + categoryList);
            List<Category> newList = new ArrayList<>();
            String searchableString = s.toLowerCase();
            String categoryName;
            Log.d("Tag", "searchableString " + searchableString);
            for (Category category : categoryList) {
                categoryName = category.getCategoryName().toLowerCase();
                if (searchableString.contains(categoryName) || categoryName.contains(searchableString)) {
                    newList.add(category);
                }
            }
            Log.d("Tag", "newList " + newList);
            createArrayList(newList);
        });

        Global.productViewModel.searchProduct(s).observe(this, products -> {
            Log.d("Tag", "productList " + products);
            List<Product> newList = new ArrayList<>();
            String searchableString = s.toLowerCase();
            String productName;
            Log.d("Tag", "searchableString " + searchableString);
                for (Product product : products){
                    productName = product.getName().toLowerCase();
                    if (searchableString.contains(productName) || productName.contains(searchableString))
                        newList.add(product);

            }
            Log.d("Tag", "newList " + newList);
            createSearchedList(newList);
        });
    }


    public void translate(String text) {
    }


    @Override
    public void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.d("Tag", "destroy");
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("Tag", "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d("Tag", "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("Tag", "onEndOfSpeech");
        speech.stopListening();
    }

    @Override
    public void onError(int errorCode) {
        progressBar.setVisibility(View.INVISIBLE);
        String errorMessage = getErrorText(errorCode);
        Log.d("Tag", "FAILED " + errorMessage);
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(errorMessage);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.d("Tag", "onEvent");
    }

    @Override
    public void onPartialResults(Bundle results) {
        Log.d("Tag", "onPartialResults ");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.d("Tag", "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d("Tag", "onResults " + matches.get(0));
        searchView.onActionViewExpanded();
        searchView.setQuery(matches.get(0), true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("Tag", "onRmsChanged: " + rmsdB);
    }

    private String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = context.getString(R.string.audio_recording_error);
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = context.getString(R.string.client_side_error);
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = context.getString(R.string.insufficient_permissions);
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = context.getString(R.string.network_error);
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = context.getString(R.string.network_timeout);
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = context.getString(R.string.no_match);
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = context.getString(R.string.recognition_service_busy);
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = context.getString(R.string.error_from_server);
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = context.getString(R.string.no_speech_input);
                break;
            default:
                message = context.getString(R.string.didnt_understand);
                break;
        }
        return message;
    }

    @Override
    public void onClickListener(int position) {
        Log.d("Tag","position "+ position);
        if (productList == null)
            return;
        Product product = productList.get(position);
        if (product == null)
            return;
        ProductFragment productFragment = ProductFragment.newInstance(product);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, productFragment)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                .commit();
    }

    @Override
    public void editProduct(Product product) {

    }

    @Override
    public void editCategory(int position) {

    }

    @Override
    public void removeCategory(int position) {

    }
}
