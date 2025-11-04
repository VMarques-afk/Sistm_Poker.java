public enum Valor {

    DOIS(2),
    TRES(3),
    QUATRO(4),
    CINCO(5),
    SEIS(6),
    SETE(7),
    OITO(8),
    NOVE(9),
    DEZ(10),
    VALETE(11),
    DAMA(12),
    REI(13),
    AS(14); // Ás é o mais alto

    private final int valorNumerico;

    Valor(int valorNumerico) {

        this.valorNumerico = valorNumerico;
    }
    public int getValorNumerico() {
        return valorNumerico;
    }

public String getNotacao() {

        switch (this) {
            case AS: return "A";
            case REI: return "K";
            case DAMA: return "Q";
            case VALETE: return"J";
            case DEZ: return "T";
            case NOVE: return "9";
            case OITO: return "8";
            case SETE: return "7";
            case SEIS: return "6";
            case CINCO: return "5";
            case QUATRO: return "4";
            case TRES: return "3";
            case DOIS: return "2";
            default: return "?"; //nunca deve acontecer
        }
    }

}