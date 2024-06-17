package dev.vixid.vsm.config.core;

import com.google.gson.annotations.Expose;
import java.util.UUID;

public class Position {
    @Expose
    private int x;
    @Expose
    private int y;
    private UUID uuid = UUID.randomUUID();

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(double x, double y) {
        this((int)x, (int)y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public UUID getUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
            return this.uuid;
        }
        return this.uuid;
    }
}
