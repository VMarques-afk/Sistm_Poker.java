import java.util.Objects;

enum Naipe {
        PAUS,
        COPAS,
        ESPADAS,
        OUROS
    }

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
