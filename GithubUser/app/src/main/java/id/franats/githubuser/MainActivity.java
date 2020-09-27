package id.franats.githubuser;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.franats.githubuser.adpters.UserAdapter;
import id.franats.githubuser.models.User;
import id.franats.githubuser.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private UserAdapter adapter;
    private ProgressBar progressBar;
    private EditText searchText;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        progressBar = findViewById(R.id.progressBar);
        searchText = findViewById(R.id.SearchInputEditText);

        RecyclerView rvUsers = findViewById(R.id.rv_follower);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        rvUsers.setAdapter(adapter);

        adapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                showSelectedHero(data);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, data);
                startActivity(intent);
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchText.getText().toString().trim();
                    if (!TextUtils.isEmpty(search)) {
                        showLoading(true);
                        mainViewModel.setListUsers(search);
                    }
                }
                return false;
            }
        });

        mainViewModel.getUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null) {
                    adapter.setData(users);
                    showLoading(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.language) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showSelectedHero(User user) {
        Toast.makeText(getApplicationContext(), getString(R.string.you_choose) + " " + user.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
    }
}
