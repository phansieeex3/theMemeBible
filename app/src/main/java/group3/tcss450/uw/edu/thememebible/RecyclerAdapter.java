package group3.tcss450.uw.edu.thememebible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Vu Hoang on 4/30/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PhotoHolder> {
    private List<Photo> mPhotos;


    public RecyclerAdapter(List<Photo> photos) {
        this.mPhotos= photos;
    }
    //1
    public static class PhotoHolder extends RecyclerView.ViewHolder {
        //2
        private ImageView mItemImage;

        public PhotoHolder(View v) {
            super(v);

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
    public void onBindViewHolder(RecyclerAdapter.PhotoHolder holder, int position) {

//        Photo itemPhoto = mPhotos.get(position);
        holder.mItemImage.setImageResource(mPhotos.get(position).photoID);
//        holder.bindPhoto(itemPhoto);


    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }


}
