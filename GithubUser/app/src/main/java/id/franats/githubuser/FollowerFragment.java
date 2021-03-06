package id.franats.githubuser;

import android.content.Intent;
import android.os.Bundle;
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
import id.franats.githubuser.viewmodels.FollowerViewModel;

public class FollowerFragment extends Fragment {

    public static final String EXTRA_FOLLOWER = "extra_follower";
    protected FollowerViewModel followerViewModel;
    private UserAdapter adapter;
    private ProgressBar progressBar;
    private String mUsername;

    public FollowerFragment() {
    }

    public static FollowerFragment newInstance(String fUser) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLLOWER, fUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUsername = getArguments().getString(EXTRA_FOLLOWER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        followerViewModel = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel.class);

        RecyclerView rvFollowers = view.findViewById(R.id.rv_follower);
        rvFollowers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        rvFollowers.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);

        followerViewModel.setListFollower(mUsername);

        followerViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
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