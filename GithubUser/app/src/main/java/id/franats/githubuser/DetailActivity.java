package id.franats.githubuser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.franats.githubuser.adpters.SectionsPagerAdapter;
import id.franats.githubuser.models.User;
import id.franats.githubuser.viewmodels.DetailViewModel;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "extra_user";

    private DetailViewModel detailViewModel;
    private CircleImageView avatarImage;
    private TextView nameText;
    private TextView usernameText;
    private TextView followingText;
    private TextView followerText;
    private TextView repositoryText;
    private TextView companyText;
    private TextView locationText;

    private String TAG = "DetailActivity";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailViewModel = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(DetailViewModel.class);

        avatarImage = findViewById(R.id.avatarView);
        nameText = findViewById(R.id.textName);
        usernameText = findViewById(R.id.textUsername);
        followingText = findViewById(R.id.textFollowing);
        followerText = findViewById(R.id.textFollower);
        repositoryText = findViewById(R.id.textRepository);
        companyText = findViewById(R.id.textCompany);
        locationText = findViewById(R.id.textLocation);

        User userSearch = getIntent().getParcelableExtra(EXTRA_USER);
        if (userSearch != null) {
            detailViewModel.setUser(userSearch.getName());
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), userSearch.getName());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        detailViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    showDetail(user);
                } else {
                    Log.d(TAG, "user = null");
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

    private void showDetail(User user) {
        if (user != null) {
            Glide.with(getApplicationContext())
                    .load(Objects.requireNonNull(user).getAvatar())
                    .placeholder(R.drawable.ic_person)
                    .into(avatarImage);
            nameText.setText(user.getName());
            usernameText.setText(user.getUsername());
            followingText.setText(user.getFollowing());
            followerText.setText(user.getFollower());
            repositoryText.setText(user.getRepository());
            companyText.setText(user.getCompany());
            locationText.setText(user.getLocation());
        } else {
            Log.d(TAG, "user = null");
        }
    }
}