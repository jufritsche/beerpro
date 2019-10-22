package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class FridgeItem implements Entity {
    public static final String COLLECTION = "fridge";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_ADDED_AT = "addedAt";

    /**
     * The id is formed by `$userId_$beerId` to make queries easier.
     */
    @Exclude
    private String id;
    private String userId;
    private String beerId;
    private int count;
    private Date addedAt;

    public FridgeItem(String userId, String beerId, int count, Date addedAt) {
        this.userId = userId;
        this.beerId = beerId;
        this.count = count;
        this.addedAt = addedAt;
    }

    public FridgeItem() {
    }

    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getBeerId() {
        return this.beerId;
    }

    public int getCount() {
        return this.count;
    }

    public Date getAddedAt() {
        return this.addedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBeerId(String beerId) {
        this.beerId = beerId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FridgeItem)) return false;
        final FridgeItem other = (FridgeItem) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId))
            return false;
        final Object this$beerId = this.getBeerId();
        final Object other$beerId = other.getBeerId();
        if (this$beerId == null ? other$beerId != null : !this$beerId.equals(other$beerId))
            return false;
        final Object this$addedAt = this.getAddedAt();
        final Object other$addedAt = other.getAddedAt();
        return this$addedAt == null ? other$addedAt == null : this$addedAt.equals(other$addedAt);
    }

    private boolean canEqual(final Object other) {
        return other instanceof FridgeItem;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        final Object $beerId = this.getBeerId();
        result = result * PRIME + ($beerId == null ? 43 : $beerId.hashCode());
        final Object $addedAt = this.getAddedAt();
        result = result * PRIME + ($addedAt == null ? 43 : $addedAt.hashCode());
        return result;
    }

    @NonNull
    public String toString() {
        return "FridgeItem(id=" + this.getId() + ", userId=" + this.getUserId() + ", beerId=" + this.getBeerId() + ", addedAt=" + this.getAddedAt() + ")";
    }
}
