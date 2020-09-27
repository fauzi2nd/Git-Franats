package id.franats.githubuser.adpters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import id.franats.githubuser.R;
import id.franats.githubuser.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ListViewHolder> {
    private ArrayList<User> listUser = new ArrayList<>();

    public void setData(ArrayList<User> items) {
        listUser.clear();
        listUser.addAll(items);
        notifyDataSetChanged();
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hero, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        User user = listUser.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .apply(new RequestOptions().override(55, 55))
                .into(holder.imgView);
        holder.tvName.setText(user.getName());
        holder.tvId.setText(user.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listUser.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView tvName, tvId;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_avatar);
            tvName = itemView.findViewById(R.id.txt_name);
            tvId = itemView.findViewById(R.id.txt_id);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
