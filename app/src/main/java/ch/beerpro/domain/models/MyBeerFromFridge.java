package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyBeerFromFridge implements MyBeer {
    private FridgeItem fridgeItem;
    private Beer beer;

    public MyBeerFromFridge(FridgeItem fridgeItem, Beer beer) {
        this.fridgeItem = fridgeItem;
        this.beer = beer;
    }
    @Override
    public String getBeerId() {
        return fridgeItem.getBeerId();
    }

    @Override
    public Date getDate() {
        return fridgeItem.getAddedAt();
    }

    public FridgeItem getFridgeItem() {
        return this.fridgeItem;
    }

    public Beer getBeer() {
        return this.beer;
    }

    public void setFridgeItem(FridgeItem fridgeItem) {
        this.fridgeItem = fridgeItem;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MyBeerFromFridge)) return false;
        final MyBeerFromFridge other = (MyBeerFromFridge) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fridgeItem = this.getFridgeItem();
        final Object other$fridgeItem = other.getFridgeItem();
        if (this$fridgeItem == null ? other$fridgeItem != null : !this$fridgeItem.equals(other$fridgeItem)) return false;
        final Object this$beer = this.getBeer();
        final Object other$beer = other.getBeer();
        return this$beer == null ? other$beer == null : this$beer.equals(other$beer);
    }

    private boolean canEqual(final Object other) {
        return other instanceof MyBeerFromFridge;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fridgeItem = this.getFridgeItem();
        result = result * PRIME + ($fridgeItem == null ? 43 : $fridgeItem.hashCode());
        final Object $beer = this.getBeer();
        result = result * PRIME + ($beer == null ? 43 : $beer.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "MyBeerFromFridge(fridgeItem=" + this.getFridgeItem() + ", beer=" + this.getBeer() + ")";
    }
}
