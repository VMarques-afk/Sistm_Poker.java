
public enum TipoMao {

    CARTA_ALTA(1),
    UM_PAR(2),
    DOIS_PARES(3),
    TRINCA(4),
    STRAIGHT(5),
    FLUSH(6),
    FULL_HOUSE(7),
    QUADRA(8),
    STRAIGHT_FLUSH(9),
    ROYAL_FLUSH(10);

    private final int forca;

    TipoMao(int forca) {
        this.forca = forca;
    }

    public int getForca() {
        return forca;
    }
}