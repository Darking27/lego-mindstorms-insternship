package lineFollower.v2;

public enum ColorID {
    BLACK, RED, WHITE, BLUE;
    
    public static ColorID getColorID(int value) {
        switch(value) {
            case 0:
                return RED;
            case 1:
                return BLUE;
            case 2:
                return WHITE;
            case 7:
                return BLACK;
            default:
                return null;
        }
    }
}
