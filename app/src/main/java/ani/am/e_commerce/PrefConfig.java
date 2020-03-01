package ani.am.e_commerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ani.am.e_commerce.db.entity.Category;

public class PrefConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file),Context.MODE_PRIVATE);
    }

    public void setLang(String lang){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", lang);
        editor.commit();
    }
    public String getLang(){
        return sharedPreferences.getString("language", "hy");
    }
    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status),status);
        editor.commit();
    }

    public boolean readLoginStatus(){
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status),false);
    }

    public void writeName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name),name);
        editor.commit();
    }

    public String readName(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_name),"User");
    }

    public void writeToken(String token, String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,token);
        editor.commit();
    }

    public String readToken(String key){
        return sharedPreferences.getString(key,"");
    }

    public void setCategoriesList(String key, List<Category> list){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();
    }

    public List<Category> getCategoriesList(String key){
        List<Category> list = new ArrayList<Category>();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        if (json.isEmpty()) {
            list = new ArrayList<Category>();
        } else {
            Type type = new TypeToken<List<Category>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }

    public void displayToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
