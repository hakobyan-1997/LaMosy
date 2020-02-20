package ani.am.e_commerce.fragments;

import android.Manifest;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ani.am.e_commerce.Global;
import ani.am.e_commerce.R;

import ani.am.e_commerce.activites.MainActivity;
import ani.am.e_commerce.adapters.CategoryAdapter;
import ani.am.e_commerce.db.entity.Category;
import ani.am.e_commerce.view_models.CategoryViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class AllCategoriesFragment extends Fragment implements RecognitionListener {
    private View view;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private SearchView searchView;
    private TextView errorTv;
    private ImageButton mSpeakBtn;
    private Context context;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private ProgressBar progressBar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.category_rv)
    RecyclerView rv;

    public AllCategoriesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);

    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        Global.categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
        Global.categoryViewModel.getCategoriesList().observe(this, categories -> updateUI(categories));
    }

    private void updateUI(@Nullable List<Category> categoryList) {
        Log.d("Tag", "allcategorieslist  update " + categoryList.size());
        if (categoryList != null) {
            createArrayList(categoryList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
        return super.onOptionsItemSelected(item);
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
        errorTv = view.findViewById(R.id.error_tv);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Tag", "Search yes I am win :D");
                Global.hideKeyboard(getActivity());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mSpeakBtn = (ImageButton) view.findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(v -> startVoiceInput());
    }

    private void startVoiceInput() {
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.RECORD_AUDIO}, REQ_CODE_SPEECH_INPUT);
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
        CategoryAdapter adapter = new CategoryAdapter(list);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
    public void onPartialResults(Bundle arg0) {
        Log.d("Tag", "onPartialResults");
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.d("Tag", "onReadyForSpeech");
    }
    @Override
    public void onResults(Bundle results) {
        Log.d("Tag", "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        searchView.onActionViewExpanded();
        searchView.setQuery(text, true);
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onRmsChanged(float rmsdB) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("Tag", "onRmsChanged: " + rmsdB);
    }
    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
