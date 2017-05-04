package Model;

import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;


import java.util.List;

import MemeObject.Meme;

/**
 * Created by Phansa on 5/3/17.
 */

public class MemeBrowserRecyclerViewAdapter extends RecyclerView.Adapter<MemeBrowserRecyclerViewAdapter.ViewHolder> {


    private final List<Meme> mMemeList;

    /**
     * Constructs an object that initializes the items and the listener
     *
     * @param memes    adapter for the recyclerView
     */
    public MemeBrowserRecyclerViewAdapter(List<Meme> memes) /*RecentTripsFragment.RecentTripsListInteractionListener listener)*/ {
        mMemeList = memes;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * Gets the views that are displayed in the fragment and initialize them
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            //messageButton = (Button) itemView.findViewById(R.id.message_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
