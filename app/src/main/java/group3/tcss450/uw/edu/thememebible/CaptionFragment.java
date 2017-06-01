package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import group3.tcss450.uw.edu.thememebible.Model.Meme;

/**
 * This fragment allows for captioning of meme images.
 *
 * @author Vu Hoang
 * @author Phansa Chaonpoj
 */
public class CaptionFragment extends Fragment implements View.OnClickListener {

    private Drawable mDrawable;
    private OnFragmentInteractionListener mListener;
    private Meme mMeme;

    public CaptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMeme = (Meme) getArguments().getSerializable(getString(R.string.meme_obj_to_caption_key));

            // get byte array of bitmap and convert to drawable
            byte[] ba = getArguments().getByteArray(getString(R.string.drawable_to_caption_key));
            Bitmap bitmap = BitmapFactory.decodeByteArray(ba, 0, ba.length);
            mDrawable = new BitmapDrawable(getResources(), bitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_caption, container, false);
        ImageView meme = (ImageView) v.findViewById(R.id.meme_item);
        meme.setImageDrawable(mDrawable);

        Button b = (Button) v.findViewById(R.id.done_button);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v.getId() == R.id.done_button) {

                // get edit text references
                EditText topText = (EditText) getActivity().findViewById(R.id.edit_caption_top);
                EditText botText = (EditText) getActivity().findViewById(R.id.edit_caption_bottom);

                // grab strings from the text fields
                String topTextString = topText.getText().toString();
                String botTextString = botText.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("topText", topTextString);
                bundle.putString("botText", botTextString);

                if (mMeme != null) {
                    bundle.putInt("genId", mMeme.getmGeneratorID());
                    bundle.putInt("imageId", mMeme.getmImageID());
                }

                mListener.setShareArgs(bundle);
                mListener.onFragmentInteraction(v.getId());
            }
        }
    }

    /**
     * When attached initialize the listener
     *
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CaptionFragment.OnFragmentInteractionListener) {
            mListener = (CaptionFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Call super's onDetach and set our listener to null;
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Callback interface for CaptionFragment.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int buttonID);
        void setShareArgs(Bundle args);
    }
}
