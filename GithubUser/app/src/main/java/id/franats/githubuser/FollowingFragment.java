package id.franats.githubuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.franats.githubuser.adpters.UserAdapter;
import id.franats.githubuser.models.User;
import id.franats.githubuser.viewmodels.FollowingViewModel;

public class FollowingFragment extends Fragment {

    private static final String EXTRA_FOLLOWING = "extra_following";
    private static final String TAG = "FollowingVM";
    protected FollowingViewModel followingViewModel;
    private UserAdapter adapter;
    private ProgressBar progressBar;
    private String mUser;

    public FollowingFragment() {
    }

    public static FollowingFragment newInstance(String fUser) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLLOWING, fUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getString(EXTRA_FOLLOWING);
        } else {
            Log.d(TAG, "getArgumen() = null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        followingViewModel = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);

        RecyclerView rvFollowers = view.findViewById(R.id.rv_following);
        rvFollowers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        rvFollowers.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);

        followingViewModel.setListFollowing(mUser);

        followingViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null) {
                    adapter.setData(users);
                    showLoading(false);
                }
            }
        });

        adapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                showSelectedHero(data);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, data);
                startActivity(intent);
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showSelectedHero(User user) {
        Toast.makeText(getContext(), getString(R.string.you_choose) + " " + user.getName(), Toast.LENGTH_SHORT).show();
    }
}