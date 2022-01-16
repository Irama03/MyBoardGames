package com.example.myboardgames.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.ButtonsActions;
import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.ui.GameInfoActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{

    private List<Game> games;
    private boolean[] isHidden;
    private OnGameClickListener onGameClickListener;
    private final LayoutInflater inflater;
    private Context context;

    public void search(String newText) {
        if (newText.trim().isEmpty()) {
            isHidden = new boolean[games.size()];
        }
        assert games.size() == isHidden.length;
        for (int i = 0; i < games.size(); i++) {
            isHidden[i] = !games.get(i).getName().toLowerCase().contains(newText.toLowerCase()) &&
                    !games.get(i).getDescription().toLowerCase().contains(newText.toLowerCase());
        }
        notifyDataSetChanged();
    }

    /**
     * public interface is used to set action when the game item is clicked
     */
    public interface OnGameClickListener {
        /**
         * method is used to react to the click on the game item
         *
         * @param game
         * @param position
         */
        void onGameClicked(Game game, int position);
    }

    /**
     * public constructor with parameters
     *
     * @param context
     * @param games
     * @param onClickListener
     */
    public GameAdapter(Context context, List<Game> games, OnGameClickListener onClickListener) {
        this.games = games;
        this.isHidden = new boolean[games.size()];
        this.context = context;
        onGameClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.games_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * method is used to set data to the game item
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (games.size() != isHidden.length) {
            isHidden = new boolean[games.size()];
        }
        if (isHidden[position]) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(getZeroLayoutParams());
            return;
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(getDefaultLayoutParams());
        }

        Game game = games.get(position);
        //think about case when photo was deleted
        //holder.ivGameImage.setImageBitmap(BitmapFactory.decodeFile(game.getPhotoPath()));

        try {
            //final Uri imageUri = Uri.parse(game.getPhotoPath());
            //final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            final InputStream imageStream = context.openFileInput(game.getPhotoPath());
            final Bitmap image = BitmapFactory.decodeStream(imageStream);
            holder.ivGameImage.setImageBitmap(image);
            //holder.ivGameImage.setImageURI(imageUri);
        } catch (FileNotFoundException e) {
            //Toast.makeText(context, "FileNotFoundException: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (Exception e) {
            //Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        ButtonsActions.favoriteAction(game, holder.ibFavoriteButton, context);
        ButtonsActions.chooseAction(game, holder.ibCheckGame, context, null, null);
        ButtonsActions.pointsAction(game, holder.stars, context);

        holder.tvGameName.setText(game.getName());

        List<String> categories = game.getCategories();
        StringBuilder categoriesStringBuilder = new StringBuilder();
        for(int i = 0; i < categories.size()-1; i++){
            categoriesStringBuilder.append(categories.get(i)).append(", ");
        }
        categoriesStringBuilder.append(categories.get(categories.size()-1));

        holder.tvGameCategories.setText(categoriesStringBuilder.toString());
        String description = game.getDescription();
        if (description.isEmpty()) {
            holder.tvGameDescription.setText("No description");
        } else
            holder.tvGameDescription.setText(game.getDescription());

        //holder.tvgameDate.setText(Html.fromHtml("<b>Дата: </b>" + game.getgameDate(), Html.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameClickListener.onGameClicked(game, position);
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
        return games.size();
    }

    private static RecyclerView.LayoutParams getDefaultLayoutParams() {
        RecyclerView.LayoutParams res = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        //TODO Звідки такі числа???
        res.topMargin = 25;
        res.rightMargin = 25;
        res.leftMargin = 25;
        return res;
    }

    private static RecyclerView.LayoutParams getZeroLayoutParams() {
        return new RecyclerView.LayoutParams(0, 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivGameImage;
        public ImageButton ibStar1, ibStar2, ibStar3, ibStar4, ibStar5, ibFavoriteButton, ibCheckGame;
        public TextView tvGameName, tvGameCategories, tvGameDescription;
        public ImageButton[] stars = new ImageButton[5];

        /**
         * public constructor with parameters
         *
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * method is used to initialise views of the view holder
         *
         * @param itemView
         */
        private void init(View itemView) {
            ivGameImage = itemView.findViewById(R.id.ivGameImage);
            ibStar1 = itemView.findViewById(R.id.ibStar1);
            ibStar2 = itemView.findViewById(R.id.ibStar2);
            ibStar3 = itemView.findViewById(R.id.ibStar3);
            ibStar4 = itemView.findViewById(R.id.ibStar4);
            ibStar5 = itemView.findViewById(R.id.ibStar5);
            stars[0] = ibStar1;
            stars[1] = ibStar2;
            stars[2] = ibStar3;
            stars[3] = ibStar4;
            stars[4] = ibStar5;
            ibFavoriteButton = itemView.findViewById(R.id.ibFavoriteButton);
            ibCheckGame = itemView.findViewById(R.id.ibCheckGame);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvGameCategories = itemView.findViewById(R.id.tvGameCategories);
            tvGameDescription = itemView.findViewById(R.id.tvGameDescription);
        }
    }
}

