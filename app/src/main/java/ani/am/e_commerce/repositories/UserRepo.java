package ani.am.e_commerce.repositories;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

import javax.inject.Singleton;
import ani.am.e_commerce.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ani.am.e_commerce.activites.MainActivity.prefConfig;

@Singleton
public class UserRepo {
    private final ApiInterface apiInterface;
    private final Executor executor;

    public UserRepo(ApiInterface apiInterface, Executor executor) {
        this.apiInterface = apiInterface;
        this.executor = executor;
    }

    public void initSession(){
        String token = prefConfig.readToken("token");
        Log.d("Tag","init Session response "+ token);
        executor.execute(()->
                apiInterface.initSession(token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("Tag","init Session response " + response);
                        if(response.isSuccessful()){
                            String json = response.body().toString();
                            Log.d("Tag","init Session json " + json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                JSONObject data = jsonObject.getJSONObject("data");
                                prefConfig.writeToken(data.getString("token"),"token");
                                prefConfig.writeToken(data.getString("id"),"id");
                                prefConfig.writeName(data.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("Tag","init Session Fail " + t.getMessage());
                    }
                }));

    }
}
