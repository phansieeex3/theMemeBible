package group3.tcss450.uw.edu.thememebible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * An adapter implementation to support the RecyclerView for the PhotoFragment.
 *
 * @author Vu Hoang
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PhotoHolder> {

    private List<Photo> mPhotos;
    private PhotoFragment.OnPhotofragmentInteractionListener mListener;

    /**constructor for recyclerAdapter
     * @param photos List of photos to be displayed. */
    public RecyclerAdapter(List<Photo> photos,PhotoFragment.OnPhotofragmentInteractionListener listener )
    {
        mListener = listener;
        this.mPhotos = photos;
    }

    /**
     * Class for view holder.
     */
    public static class PhotoHolder extends RecyclerView.ViewHolder  {

        public ImageView mItemImage;
        public final View mView;

        public PhotoHolder(View v) {
            super(v);
            mView = v;
            mItemImage = (ImageView) v.findViewById(R.id.item_image);
        }
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        PhotoHolder viewHolder = new PhotoHolder(inflatedView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.PhotoHolder holder, final int position) {
        holder.mItemImage.setImageDrawable(mPhotos.get(position).mPhotoDraw);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.setDrawableArgs(mPhotos.get(position).mPhotoDraw,
                            mPhotos.get(position).mMeme);
                    mListener.onPhotofragmentInteractionListener();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }
}
