package com.example.myboardgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.R;
import com.example.myboardgames.database.AppDatabase;
import com.example.myboardgames.models.SharedGame;
import com.example.myboardgames.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class SharedGameAdapter extends RecyclerView.Adapter<SharedGameAdapter.SharedGameViewHolder>{

    private List<SharedGame> sharedGames;
    private boolean[] isHidden;
    private OnSharedGameClickListener onSharedGameClickListener;
    private final LayoutInflater inflater;
    private Context context;
    private AppDatabase appDatabase;

    /**
     * public interface is used to set action when the game item is clicked
     */
    public interface OnSharedGameClickListener {
        /**
         * method is used to react to the click on the game item
         *
         * @param game
         * @param position
         */
        void onSharedGameClicked(SharedGame game, int position);
    }

    /**
     * public constructor with parameters
     *
     * @param context
     * @param sharedGames
     * @param onClickListener
     */
    public SharedGameAdapter(Context context, List<SharedGame> sharedGames, OnSharedGameClickListener onClickListener) {
        this.sharedGames = sharedGames;
        this.isHidden = new boolean[sharedGames.size()];
        this.context = context;
        onSharedGameClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
        appDatabase = new AppDatabase();
    }

    /**
     * method is used to set layout to the game item
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public SharedGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.games_shared_item, parent, false);
        return new SharedGameViewHolder(view);
    }

    /**
     * method is used to set data to the game item
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SharedGameViewHolder holder, int position) {
        if (sharedGames.size() != isHidden.length) {
            isHidden = new boolean[sharedGames.size()];
        }
        if (isHidden[position]) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(getZeroLayoutParams());
            return;
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(getDefaultLayoutParams());
        }

        SharedGame game = sharedGames.get(position);

        holder.tvGameName.setText(game.getName());

        holder.tvGameRecommender.setText("Від " + game.getRecommenderId());
        appDatabase.getReferenceToGroup(AppDatabase.USERS_GROUP_KEY).child(game.getRecommenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (user.getUsername() != null && !user.getUsername().equals(""))
                    holder.tvGameRecommender.setText("Від " + user.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        holder.tvGameCategories.setText(game.getCategories());
        String description = game.getDescription();
        if (description.isEmpty()) {
            holder.tvGameDescription.setText("Немає опису");
        } else
            holder.tvGameDescription.setText(game.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSharedGameClickListener.onSharedGameClicked(game, position);
            }
        });
    }

    /**
     * method is used to get size of the games list
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return sharedGames.size();
    }

    private static RecyclerView.LayoutParams getDefaultLayoutParams() {
        RecyclerView.LayoutParams res = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        res.topMargin = 25;
        res.rightMargin = 25;
        res.leftMargin = 25;
        return res;
    }

    private static RecyclerView.LayoutParams getZeroLayoutParams() {
        return new RecyclerView.LayoutParams(0, 0);
    }

    public static class SharedGameViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGameName, tvGameRecommender, tvGameCategories, tvGameDescription;

        /**
         * public constructor with parameters
         *
         * @param itemView
         */
        public SharedGameViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * method is used to initialise views of the view holder
         *
         * @param itemView
         */
        private void init(View itemView) {
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvGameRecommender = itemView.findViewById(R.id.tvGameRecommender);
            tvGameCategories = itemView.findViewById(R.id.tvGameCategories);
            tvGameDescription = itemView.findViewById(R.id.tvGameDescription);
        }
    }
}

