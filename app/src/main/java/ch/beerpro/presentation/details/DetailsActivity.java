package ch.beerpro.presentation.details;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.MainActivity;
import ch.beerpro.presentation.details.createrating.CreateRatingActivity;

import static ch.beerpro.presentation.utils.DrawableHelpers.setDrawableTint;

public class DetailsActivity extends AppCompatActivity implements OnRatingLikedListener {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.avgRating)
    TextView avgRating;

    @BindView(R.id.numRatings)
    TextView numRatings;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.wishlist)
    ToggleButton wishlist;

    @BindView(R.id.manufacturer)
    TextView manufacturer;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.addRatingBar)
    RatingBar addRatingBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RatingsRecyclerViewAdapter adapter;

    private DetailsViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String beerId = "";
        String beerUriData = "";
        String subBeerData = "";
        int dataLength = 0;

        if (getIntent().getExtras().getString(ITEM_ID) != null) {
            beerId = getIntent().getExtras().getString(ITEM_ID);
        } else if(getIntent().getDataString() != null){
            beerUriData = getIntent().getDataString();
            dataLength = beerUriData.length();
            subBeerData = beerUriData.substring(39, dataLength);
            if (dataLength > 39 && subBeerData.length() == 20) {
                beerId = subBeerData;
            }
        }
        if (getIntent().getExtras().getString(ITEM_ID) != null || dataLength > 39 && subBeerData.length() == 20) {
            setContentView(R.layout.activity_details);
            ButterKnife.bind(this);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            toolbar.setTitleTextColor(Color.alpha(0));

            model = ViewModelProviders.of(this).get(DetailsViewModel.class);
            model.setBeerId(beerId);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new RatingsRecyclerViewAdapter(this, model.getCurrentUser());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));


            final String ratingBeerId = beerId;
            model.getOwnRatings().observe(this, (ratings) -> {
                if (ratings.size() > 0) {

                    List<Rating> ownBeerRatings = new ArrayList<Rating>();
                    for (Rating rating : ratings) {
                        if (rating.getBeerId().equals(ratingBeerId)) {
                            ownBeerRatings.add(rating);
                        }
                    }

                    if (ownBeerRatings.size() > 0) {
                        float ratingTotal = 0;
                        for (Rating rating : ownBeerRatings) {
                            ratingTotal += rating.getRating();
                        }
                        float avgRating = ratingTotal / ownBeerRatings.size();
                        addRatingBar.setRating(avgRating);
                    }
                }
            });

            model.getBeer().observe(this, this::updateBeer);
            model.getRatings().observe(this, this::updateRatings);
            model.getWish().observe(this, this::toggleWishlistView);
            addRatingBar.setOnRatingBarChangeListener(this::addNewRating);

            recyclerView.setAdapter(adapter);
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        // read private note and set clicklistener to change note
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        TextView privateNote = findViewById(R.id.privateNote);
        String note = sharedPref.getString(beerId, null);
        if(note != null) {
            privateNote.setText(note);
        }

        CardView noteView = findViewById(R.id.noteView);
        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrivateNoteDialog();
            }
        });
    }

    private void addNewRating(RatingBar ratingBar, float v, boolean b) {
        if (!b) {
            return;
        }
        Intent intent = new Intent(this, CreateRatingActivity.class);
        intent.putExtra(CreateRatingActivity.ITEM, model.getBeer().getValue());
        intent.putExtra(CreateRatingActivity.RATING, v);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, addRatingBar, "rating");
        startActivity(intent, options.toBundle());
    }

    @OnClick(R.id.actionsButton)
    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.single_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        Button fridgeBtn = dialog.findViewById(R.id.addToFridge);
        fridgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemInFridge(findViewById(R.id.detail_view));
                dialog.dismiss();
            }
        });

        Button addNoteBtn = dialog.findViewById(R.id.addPrivateNote);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrivateNoteDialog();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addItemInFridge(View detail_view) {
        model.addItemInFridge(model.getBeer().getValue().getId());
        Snackbar
                .make(detail_view, "Dem Kühlschrank hinzugefügt", Snackbar.LENGTH_LONG)
                .setAction("+1", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addItemInFridge(detail_view);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public void showPrivateNoteDialog() {
        View noteInputView = getLayoutInflater().inflate(R.layout.note_dialog, null);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String beerId = getIntent().getExtras().getString(ITEM_ID);

        EditText input = noteInputView.findViewById(R.id.noteInput);
        input.setText(sharedPref.getString(beerId, null));

        AlertDialog.Builder noteDialogBuilder = new AlertDialog.Builder(this);
        noteDialogBuilder.setTitle("Private Notiz hinzufügen");


        noteDialogBuilder.setView(noteInputView);
        noteDialogBuilder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView noteText = findViewById(R.id.privateNote);
                noteText.setText(input.getText().toString());

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(beerId, input.getText().toString());
                editor.commit();
            }
        });

        noteDialogBuilder.show();
    }

    private void updateBeer(Beer item) {
        name.setText(item.getName());
        manufacturer.setText(item.getManufacturer());
        category.setText(item.getCategory());
        name.setText(item.getName());
        GlideApp.with(this).load(item.getPhoto()).apply(new RequestOptions().override(120, 160).centerInside())
                .into(photo);
        ratingBar.setNumStars(5);
        ratingBar.setRating(item.getAvgRating());
        avgRating.setText(getResources().getString(R.string.fmt_avg_rating, item.getAvgRating()));
        numRatings.setText(getResources().getString(R.string.fmt_ratings, item.getNumRatings()));
        toolbar.setTitle(item.getName());
    }

    private void updateRatings(List<Rating> ratings) {
        adapter.submitList(new ArrayList<>(ratings));
    }

    @Override
    public void onRatingLikedListener(Rating rating) {
        model.toggleLike(rating);
    }

    @OnClick(R.id.wishlist)
    public void onWishClickedListener(View view) {
        model.toggleItemInWishlist(model.getBeer().getValue().getId());
        /*
         * We won't get an update from firestore when the wish is removed, so we need to reset the UI state ourselves.
         * */
        if (!wishlist.isChecked()) {
            toggleWishlistView(null);
        }
    }

    private void toggleWishlistView(Wish wish) {
        if (wish != null) {
            int color = getResources().getColor(R.color.colorPrimary);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.button2)
    public void onShareClickedListener() {
        String beerID = model.getBeer().getValue().getId();
        String url = "https://beershare.page.link/sh5r3?data=" + beerID;

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this beer:");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("plain/text");

        startActivity(Intent.createChooser(shareIntent, "Share this beer"));
    }

}

