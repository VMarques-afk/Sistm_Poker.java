import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HandEvaluator {

    public ResultadoMao avaliarMao(List<Carta> cartas) {

        Map<Valor, Integer> contagemDeValores = getContagemDeValores(cartas);
        Map<Naipe, Integer> contagemDeNaipes = getContagemDeNaipes(cartas);

        List<Valor> valoresOrdenados = getValoresOrdenados(cartas);

        ResultadoMao resultado;

        resultado = identificarStraightFlush(contagemDeNaipes, cartas);
        if (resultado != null) return resultado;

        resultado = identificarQuadra(contagemDeValores, valoresOrdenados);
        if (resultado != null) return resultado;

        resultado = identificarFullHouse(contagemDeValores);
        if (resultado != null) return resultado;

        resultado = identificarFlush(contagemDeNaipes, cartas);
        if (resultado != null) return resultado;

        resultado = identificarStraight(contagemDeValores);
        if (resultado != null) return resultado;

        resultado = identificarTrinca(contagemDeValores, valoresOrdenados);
        if (resultado != null) return resultado;

        resultado = identificarDoisPares(contagemDeValores, valoresOrdenados);
        if (resultado != null) return resultado;

        resultado = identificarUmPar(contagemDeValores, valoresOrdenados);
        if (resultado != null) return resultado;


        return identificarCartaAlta(valoresOrdenados);
    }

    private Map<Valor, Integer> getContagemDeValores(List<Carta> cartas) {

        Map<Valor, Integer> contagem = new HashMap<>();
        for (Carta carta : cartas) {
            contagem.put(carta.getValor(), contagem.getOrDefault(carta.getValor(), 0) + 1);
        }
        return contagem;
    }

    private Map<Naipe, Integer> getContagemDeNaipes(List<Carta> cartas) {
        Map<Naipe, Integer> contagem = new HashMap<>();
        for (Carta carta : cartas) {
            contagem.put(carta.getNaipe(), contagem.getOrDefault(carta.getNaipe(), 0) + 1);
        }
        return contagem;
    }


    private List<Valor> getKickers(List<Valor> valoresOrdenados, List<Valor> valoresIgnorar, int numKickers) {
        List<Valor> kickers = new ArrayList<>();
        for (Valor v : valoresOrdenados) {
            if (!valoresIgnorar.contains(v)) {
                kickers.add(v);
                if (kickers.size() == numKickers) {
                    break;
                }
            }
        }

        return kickers;
    }

    private List<Valor> getValoresOrdenados(List<Carta> cartas) {
        return cartas.stream()
                .map(Carta::getValor)
                .sorted(Comparator.comparingInt(Valor::getValorNumerico).reversed())
                .collect(Collectors.toList());
    }

    private ResultadoMao identificarCartaAlta(List<Valor> valoresOrdenados) {

        int numKickers = Math.min(valoresOrdenados.size(), 5);
        List<Valor> kickers = valoresOrdenados.subList(0, numKickers);
        return new ResultadoMao(TipoMao.CARTA_ALTA, kickers);
    }


    private ResultadoMao identificarUmPar(Map<Valor, Integer> contagem, List<Valor> valoresOrdenados) {

        int paresEncontrados = 0;
        Valor valorDoPar = null;
        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == 2) {
                paresEncontrados++;
                valorDoPar = entry.getKey();
            }
        }


        if (paresEncontrados != 1 || contagem.containsValue(3)) {
            return null;
        }

        List<Valor> maoFinal = new ArrayList<>();
        maoFinal.add(valorDoPar);

        int kickersNecessarios = 3;
        int kickersDisponiveis = valoresOrdenados.size() - 2;
        int numKickers = Math.min(kickersNecessarios, kickersDisponiveis);
        if(numKickers > 0) {
            maoFinal.addAll(getKickers(valoresOrdenados, List.of(valorDoPar), numKickers));
        }
        return new ResultadoMao(TipoMao.UM_PAR, maoFinal);
    }


    private ResultadoMao identificarTrinca(Map<Valor, Integer> contagem, List<Valor> valoresOrdenados) {

         if (contagem.containsValue(3) && !contagem.containsValue(2)) {
        Valor valorDaTrinca = null;
        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == 3) {
                valorDaTrinca = entry.getKey();
                break;
            }
        }
        if (valorDaTrinca == null) return null;

        List<Valor> maoFinal = new ArrayList<>();

        maoFinal.add(valorDaTrinca);
        int kickersNecessarios = 2;
        int kickersDisponiveis = valoresOrdenados.size() - 3;
        int numKickers = Math.min(kickersNecessarios, kickersDisponiveis);
        if (numKickers > 0) {
            maoFinal.addAll(getKickers(valoresOrdenados, List.of(valorDaTrinca), numKickers));
        }
        return new ResultadoMao(TipoMao.TRINCA, maoFinal);

        } else {
            return null;
        }
    }

    private ResultadoMao identificarDoisPares(Map<Valor, Integer> contagem, List<Valor> valoresOrdenados) {

        List<Valor> valoresDosPares = new ArrayList<>();
        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == 2) {
                valoresDosPares.add(entry.getKey());
            }
        }

        if (valoresDosPares.size() < 2 || contagem.containsValue(3)) {
            return null;
        }

        valoresDosPares.sort(Comparator.comparingInt(Valor::getValorNumerico).reversed());

        List<Valor> maoFinal = new ArrayList<>();

        maoFinal.add(valoresDosPares.get(0));
        maoFinal.add(valoresDosPares.get(1));

        Valor kicker = null;
        for (Valor v : valoresOrdenados) {
        if (!valoresDosPares.contains(v)) {
            kicker = v;
            break;
            }
        }
        
        if (kicker != null) {
            maoFinal.add(kicker);   
        } else if (!valoresOrdenados.isEmpty() && valoresDosPares.size() > 2) {
            maoFinal.add(valoresDosPares.get(2));
        }

        return new ResultadoMao(TipoMao.DOIS_PARES, maoFinal);
    }

    private ResultadoMao identificarStraight(Map<Valor, Integer> contagem) {

        Set<Valor> valoresPresentes = contagem.keySet();

        List<Integer> valoresNumericos = new ArrayList<>();
        for (Valor v : valoresPresentes) {
            valoresNumericos.add(v.getValorNumerico());
        }

        Collections.sort(valoresNumericos);

        List<Valor> sequencia = new ArrayList<>();

        if (valoresNumericos.containsAll(List.of(2, 3, 4, 5, 14))) {
            sequencia = List.of(Valor.CINCO, Valor.QUATRO, Valor.TRES, Valor.DOIS, Valor.AS);
            return new ResultadoMao(TipoMao.STRAIGHT, sequencia);

        }

        for (int i = valoresNumericos.size() - 1; i >= 4; i--) {
            int v1 = valoresNumericos.get(i);
            int v2 = valoresNumericos.get(i - 1);
            int v3 = valoresNumericos.get(i - 2);
            int v4 = valoresNumericos.get(i - 3);
            int v5 = valoresNumericos.get(i - 4);

            if (v1 == v2 + 1 && v2 == v3 + 1 && v3 == v4 + 1 && v4 == v5 + 1) {

                sequencia = List.of(
                        Valor.values()[v1 - 2],
                        Valor.values()[v2 - 2],
                        Valor.values()[v3 - 2],
                        Valor.values()[v4 - 2],
                        Valor.values()[v5 - 2]
                );
                return new ResultadoMao(TipoMao.STRAIGHT, sequencia);
            }
        }
        return null;
    }

    private ResultadoMao identificarFlush(Map<Naipe, Integer> contagem,List<Carta> cartas) {

        Naipe naipeDoFlush = null;
        for (Map.Entry<Naipe, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() >= 5) {
                naipeDoFlush = entry.getKey();
                break;
            }
        }
        if (naipeDoFlush == null) {
            return null;
        }

        final Naipe naipeFinal = naipeDoFlush;
        List<Valor> valoresDoFlush = cartas.stream()
                .filter(carta -> carta.getNaipe() == naipeFinal)
                .map(Carta::getValor)
                .sorted(Comparator.comparingInt(Valor::getValorNumerico).reversed())
                .collect(Collectors.toList());

        int maxKickers = Math.min(valoresDoFlush.size(), 5);
        List<Valor> kickers = valoresDoFlush.subList(0, maxKickers);

        return new ResultadoMao(TipoMao.FLUSH, kickers);
    }

    private ResultadoMao identificarFullHouse(Map<Valor, Integer> contagem) {

        if (!contagem.containsValue(3) || !contagem.containsValue(2)) {
            return null;
        }

        Valor valorDaTrinca = null;
        Valor valorDoPar = null;

        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == 3) {
                valorDaTrinca = entry.getKey();
                break;
            }
        }

        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if ((entry.getValue() == 2 || entry.getValue() == 3) && entry.getKey() != valorDaTrinca) {
                if (valorDoPar == null || entry.getKey().getValorNumerico() > valorDoPar.getValorNumerico()) {
                    valorDoPar = entry.getKey();
                }
            }
        }

        if (valorDaTrinca == null || valorDoPar == null) {
            return null;
        }

        List<Valor> maoFinal = new ArrayList<>();
        maoFinal.add(valorDaTrinca);
        maoFinal.add(valorDoPar);

        return new ResultadoMao(TipoMao.FULL_HOUSE, maoFinal);
    }

    private ResultadoMao identificarQuadra(Map<Valor, Integer> contagem, List<Valor> valoresOrdenados) {
        if(!contagem.containsValue(4)) {
            return null;
        }

        Valor valorDaQuadra = null;
        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == 4) {
                valorDaQuadra = entry.getKey();
                break;
            }
        }

        List<Valor> maoFinal = new ArrayList<>();
        maoFinal.add(valorDaQuadra);

        int kickersNecessarios = 1;
        int kickersDisponiveis = valoresOrdenados.size() - 4;
        int numKickers = Math.min(kickersNecessarios,  kickersDisponiveis);
        if (numKickers > 0) {
            maoFinal.addAll(getKickers(valoresOrdenados, List.of(valorDaQuadra), numKickers));
        }

        return new ResultadoMao(TipoMao.QUADRA, maoFinal);
    }

    private ResultadoMao identificarStraightFlush(Map<Naipe, Integer> contagemNaipes, List<Carta> cartas){

        Naipe naipeDoFlush = null;
        for (Map.Entry<Naipe, Integer> entry : contagemNaipes.entrySet()){
            if(entry.getValue() >= 5) {
                naipeDoFlush = entry.getKey();
                break;
            }
        }
        if (naipeDoFlush == null) {
            return null;
        }

        final Naipe naipeFinal = naipeDoFlush;
        List<Carta> cartasDoFlush = cartas.stream()
                .filter(carta -> carta.getNaipe() == naipeFinal)
                .collect(Collectors.toList());

        List<Valor> sequencia = findStraightInList(cartasDoFlush);

        if (sequencia == null){
            return null;
        }

        if (sequencia.get(0) == Valor.AS && sequencia.get(1) == Valor.REI) {
            return new ResultadoMao(TipoMao.ROYAL_FLUSH, sequencia);
        } else {
            return new ResultadoMao(TipoMao.STRAIGHT_FLUSH, sequencia);
        }
    }


    private List<Valor> findStraightInList(List<Carta> cartasFiltradas) {
        if (cartasFiltradas.size() < 5) {
            return null;
        }

        List<Integer> valoresNumericos = cartasFiltradas.stream()
                .map(carta -> carta.getValor().getValorNumerico())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (valoresNumericos.containsAll(List.of(2, 3, 4, 5, 14))) {
            return List.of(Valor.CINCO, Valor.QUATRO, Valor.TRES, Valor.DOIS, Valor.AS);
        }

        for (int i = valoresNumericos.size() - 1; i >= 4; i--) {
            int v1 = valoresNumericos.get(i);
            int v2 = valoresNumericos.get(i - 1);
            int v3 = valoresNumericos.get(i - 2);
            int v4 = valoresNumericos.get(i - 3);
            int v5 = valoresNumericos.get(i - 4);

            if (v1 == v2 + 1 && v2 == v3 + 1 && v3 == v4 + 1 && v4 == v5 + 1 ) {
                return List.of(
                        Valor.values()[v1 - 2],
                        Valor.values()[v2 - 2],
                        Valor.values()[v3 - 2],
                        Valor.values()[v4 - 2],
                        Valor.values()[v5 - 2]
                );
            }
        }
        return null;
    }
}