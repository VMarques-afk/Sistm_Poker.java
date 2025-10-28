import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho {

    private final List<Carta> cartas;


    public Baralho() {
        this.cartas = new ArrayList<>();

        for (Naipe naipe : Naipe.values()) {

            for (Valor valor : Valor.values()) {

                this.cartas.add(new Carta(naipe, valor));
            }
        }
    }

    public void embaralhar() {
        Collections.shuffle(this.cartas);
    }


    public Carta distribuir() {
        return this.cartas.remove(0);
    }

    public int tamanho() {
        return this.cartas.size();
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(this.cartas);
    }
}