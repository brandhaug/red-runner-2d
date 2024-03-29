package Game.GameObjects;

public enum TileType {
    GRASS("Grass", 1),
    GRASS_LEFT("GrassLeft", 1),
    GRASS_MID("GrassMid", 1),
    GRASS_RIGHT("GrassRight", 1),
    GRASS_TOP("GrassTop", 1),
    DIRT("Dirt", 1),
    DIRT_BOTTOM("DirtDown", 1),
    DIRT_LEFT("DirtLeft", 1),
    DIRT_RIGHT("DirtRight", 1),
    DIRT_RIGHT_CORNER("DirtRightCorner", 1),
    DIRT_LEFT_CORNER("DirtLeftCorner", 1),
    DIRT_COLUMN("DirtColumn", 1),
    SPIKE_UP("SpikeUp", 1);

    private String fileName;
    private int cols;

    /**
     * Sets the file name and the amount of columns
     *
     * @param fileName the file name
     * @param cols     amount of columns
     */
    TileType(String fileName, int cols) {
        this.fileName = fileName;
        this.cols = cols;
    }

    /**
     * Gets the file name
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the amount of columns
     *
     * @return cols
     */
    public int getCols() {
        return cols;
    }
}
