package ch.beerpro.presentation.profile.mybeers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.MyBeer;

public class MyBeersFragment extends Fragment {

    private static final String TAG = "MyBeersFragment";

    private OnMyBeerItemInteractionListener interactionListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private MyBeersRecyclerViewAdapter adapter;

    public MyBeersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchresult_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);


        MyBeersViewModel model = ViewModelProviders.of(getActivity()).get(MyBeersViewModel.class);

        adapter = new MyBeersRecyclerViewAdapter(interactionListener, model.getCurrentUser());
        model.getMyFilteredBeers().observe(getActivity(), this::handleBeersChanged);

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void handleBeersChanged(List<MyBeer> beers) {
        if (beers == null || beers.isEmpty()) {
            adapter.submitList(new ArrayList<>());
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            adapter.submitList(new ArrayList<>(beers));
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyBeerItemInteractionListener) {
            interactionListener = (OnMyBeerItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }
}
