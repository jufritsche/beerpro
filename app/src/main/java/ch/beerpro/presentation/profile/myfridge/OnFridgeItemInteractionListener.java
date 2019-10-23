package ch.beerpro.presentation.profile.myfridge;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;

public interface OnFridgeItemInteractionListener {
    void onMoreClickedListener(ImageView photo, Beer beer);

    void onFridgeItemRemoveClickedListener(Beer beer);

    void onFridgeItemTopUpClickedListener(Beer beer);

    void onFridgeItemDrinkClickedListener(Beer beer);
}
