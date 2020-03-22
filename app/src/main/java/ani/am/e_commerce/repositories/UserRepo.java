package ani.am.e_commerce.repositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.BaseActivity;
import ani.am.e_commerce.activities.MainActivity;
import ani.am.e_commerce.api.ApiInterface;
import ani.am.e_commerce.db.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ani.am.e_commerce.activities.MainActivity.prefConfig;

@Singleton
public class UserRepo {
    private final ApiInterface apiInterface;
    private final Executor executor;

    public UserRepo(ApiInterface apiInterface, Executor executor) {
        this.apiInterface = apiInterface;
        this.executor = executor;
    }

    public void initSession(Context context) {
        String token = prefConfig.readToken("token");
        Log.d("Tag", "init Session response " + token);
        executor.execute(() ->
                apiInterface.initSession(token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag", "init Session response " + response);
                        if (response.isSuccessful()) {
                            String json = response.body().toString();
                            Log.d("Tag", "init Session json " + json);
                            if (json != null)
                                saveUserData(context, json);
                        } else
                            prefConfig.displayToast(context.getString(R.string.something_went_wrong));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("Tag", "init Session Fail " + t.getMessage());
                        Toast.makeText(context, context.getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    }
                }));

    }

    public void login(Context context, User user) {
        executor.execute(() ->
                apiInterface.performUserLogin(user).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            String json = response.body().toString();
                            Log.d("Tag", "init Session json " + json);
                            if (json != null)
                                saveUserData(context, json);
                        } else
                            prefConfig.displayToast(context.getString(R.string.something_went_wrong));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(context, context.getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void registration(Context context, User user) {
        executor.execute(() ->
                apiInterface.performRegistration(user).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            String json = response.body().toString();
                            Log.d("Tag", "init Session json " + json);
                            if (json != null)
                                saveUserData(context, json);
                        } else
                            prefConfig.displayToast(context.getString(R.string.something_went_wrong));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(context, context.getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void logout(Context context, String token) {
        executor.execute(() ->
                apiInterface.performUserLogOut(token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            prefConfig.writeLoginStatus(false);
                            prefConfig.writeName("User");
                            prefConfig.writeToken("", "token");
                            prefConfig.writeToken("", "id");
                            context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else
                            prefConfig.displayToast(context.getString(R.string.logout_failed));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(context, context.getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void saveUserData(Context context, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            String token = data.getString("token");
            String id = data.getString("userId");
            String name = data.getString("name");
            prefConfig.writeName(name);
            prefConfig.writeToken(token, "token");
            prefConfig.writeToken(id, "id");
            Intent intent = new Intent(context, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            MainActivity.prefConfig.writeLoginStatus(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
