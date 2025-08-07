package dev.steelbookdb.steelbookapi.steelbook;

public enum DiskFormat {
    FOUR_K_ULTRA_HD("4K Ultra HD"),
    BLU_RAY_DISC("Blu-ray Disc"),
    DVD("DVD");

    private final String displayValue;

    DiskFormat(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
