package com.example.hieuphong_vu_comp304_003_test02.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Event.class,
                parentColumns = "eventId",
                childColumns = "eventId",onDelete = ForeignKey.CASCADE)
},indices = {@Index(value = {"userId"}),@Index(value = {"eventId"})})
public class Booking {
    @PrimaryKey(autoGenerate = true)
    private int bookingId;
    private int quantity;
    private int userId;
    private int eventId;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
