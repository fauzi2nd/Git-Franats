package id.franats.githubuser.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import id.franats.githubuser.models.User;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<User>> listUsers = new MutableLiveData<>();

    public void setListUsers(final String search) {
        final ArrayList<User> listUser = new ArrayList<>();

        String token = "token fd85b84e8c59b275dae6575dcd7d9ddd59b132a9";
        String url = "https://api.github.com/search/users?q=" + search + "";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", token);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray items = responseObject.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject userList = items.getJSONObject(i);
                        User user = new User();
                        user.setId(userList.getString("id"));
                        user.setName(userList.getString("login"));
                        user.setAvatar(userList.getString("avatar_url"));
                        listUser.add(user);
                    }
                    listUsers.postValue(listUser);
                } catch (JSONException e) {
                    Log.d("MainVM", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Log.d("MainVM", errorMessage);
            }
        });

    }

    public LiveData<ArrayList<User>> getUsers() {
        return listUsers;
    }
}
