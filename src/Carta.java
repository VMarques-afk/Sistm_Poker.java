import java.util.Objects;

enum Naipe {
        PAUS,
        COPAS,
        ESPADAS,
        OUROS
    }
   /* enum Valor {
        DOIS, TRES, QUATRO, CINCO, SEIS, SETE, OITO, NOVE, DEZ,
        VALETE,
        DAMA,
        REI,
        AS
    }*/

    public class Carta {
        private final Naipe naipe;
        private final Valor valor;

        public Carta(Naipe naipe, Valor valor) {
            this.naipe = naipe;
            this.valor = valor;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Carta carta = (Carta) o;
            return naipe == carta.naipe && valor == carta.valor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(naipe, valor);
        }

        @Override
        public String toString() {
            return valor + " de " + naipe;
        }

        public  Naipe getNaipe() {
            return this.naipe;
        }

         public Valor getValor() {
            return valor;
        }
    }


/* public enum Valor {

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
    AS(14); //AS é o mais alto

    private final int valorNumerico;

    Valor(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
} */