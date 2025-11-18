import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class DrawAnalysis{

    private final Set<Carta> outsFlush;
    private final Set<Carta> outsStraight;
    private final Set<Carta> outsParaParTrincaQuadra;
    private final Set<Carta> outsParaFullHouse;

    public  DrawAnalysis() {
        this.outsFlush = new HashSet<>();
        this.outsStraight = new HashSet<>();
        this.outsParaParTrincaQuadra = new HashSet<>();
        this.outsParaFullHouse = new HashSet<>();
    }


    public void addOutsFlush(Carta c) {outsFlush.add(c);}
    public void addOutStraight(Carta c) {outsStraight.add(c);}
    public void addOutParaParTrincaQuadra(Carta c) {outsParaParTrincaQuadra.add(c);}
    public void addOutFullHouse(Carta c) {outsParaFullHouse.add(c);}

    public int getTotalUnicoOuts() {
        Set<Carta> todosOsOuts = new HashSet<>();
        todosOsOuts.addAll(outsFlush);
        todosOsOuts.addAll(outsStraight);
        todosOsOuts.addAll(outsParaParTrincaQuadra);
        todosOsOuts.addAll(outsParaFullHouse);
        return todosOsOuts.size();
    }

    public String getResumo() {
        if (getTotalUnicoOuts() == 0) {
            return "Sem outs claros.";
        }

        List<String> resumos = new ArrayList<>();

        if (!outsFlush.isEmpty()) {
            resumos.add(outsFlush.size() + "outs para Flush");
        }

        if (!outsStraight.isEmpty()) {
            resumos.add(outsStraight.size() + "outs para Straight");
        }

        if (!outsParaParTrincaQuadra.isEmpty()) {
            resumos.add(outsParaParTrincaQuadra.size() + "outs para Par/Trinca/Quadra");
        }

          if (!outsParaFullHouse.isEmpty()) {
            resumos.add(outsParaFullHouse.size() + "outs para Full House");
        }
          return String.join(" | ", resumos);
    }

}