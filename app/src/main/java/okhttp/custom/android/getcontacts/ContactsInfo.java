package okhttp.custom.android.getcontacts;

public class ContactsInfo {

    private int contactId;
    private String photo;
    private String name;
    private String phone;
    private boolean stared;

    private int inviteState;
    private long lastInviteTime;
    private long lastSkipTime;

    private boolean isFirstSkip;

    @NotNull
    private long currentUserId;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public int getInviteState() {
        return inviteState;
    }

    public void setInviteState(int inviteState) {
        this.inviteState = inviteState;
    }

    public long getLastInviteTime() {
        return lastInviteTime;
    }

    public void setLastInviteTime(long lastInviteTime) {
        this.lastInviteTime = lastInviteTime;
    }

    public long getLastSkipTime() {
        return lastSkipTime;
    }

    public void setLastSkipTime(long lastSkipTime) {
        this.lastSkipTime = lastSkipTime;
    }

    public boolean isFirstSkip() {
        return isFirstSkip;
    }

    public void setFirstSkip(boolean firstSkip) {
        isFirstSkip = firstSkip;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public String toString() {
        return "ContactsInfo{" +
                "contactId=" + contactId +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", stared=" + stared +
                ", inviteState=" + inviteState +
                ", lastInviteTime=" + lastInviteTime +
                ", lastSkipTime=" + lastSkipTime +
                ", isFirstSkip=" + isFirstSkip +
                ", currentUserId=" + currentUserId +
                '}';
    }
}
