package id.franats.githubuser.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import id.franats.githubuser.models.User;

public class DetailViewModel extends ViewModel {
    private MutableLiveData<User> mUser = new MutableLiveData<>();

    public void setUser(final String search) {
        final User user = new User();

        String token = "token fd85b84e8c59b275dae6575dcd7d9ddd59b132a9";
        String url = "https://api.github.com/users/" + search;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", token);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    user.setId(responseObject.getString("id"));
                    user.setName(responseObject.getString("name"));
                    user.setUsername(responseObject.getString("login"));
                    user.setAvatar(responseObject.getString("avatar_url"));
                    user.setLocation(responseObject.getString("location"));
                    user.setCompany(responseObject.getString("company"));
                    user.setRepository(responseObject.getString("public_repos"));
                    user.setFollower(responseObject.getString("followers"));
                    user.setFollowing(responseObject.getString("following"));
                    mUser.postValue(user);
                } catch (JSONException e) {
                    Log.d("DetailVM", Objects.requireNonNull(e.getMessage()));
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
                Log.d("DetailVM", errorMessage);
            }
        });
    }

    public LiveData<User> getUser() {
        return mUser;
    }
}
