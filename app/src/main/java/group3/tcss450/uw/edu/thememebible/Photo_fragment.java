package group3.tcss450.uw.edu.thememebible;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Photo_fragment extends Fragment {

    private List<Photo> mPhoto;
    private RecyclerView rv;


    public Photo_fragment() {
        // Required empty public constructor
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_fragment, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        rv.setHasFixedSize(true);

        return v;
    }

    private void initializeData(){
        mPhoto = new ArrayList<>();
        mPhoto.add(new Photo(R.drawable.cropped_doge));
//        mPhoto.add(new Photo(R.drawable.cropped_doge));
    }

    private void initializeAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(mPhoto);
        rv.setAdapter(adapter);
    }

}
