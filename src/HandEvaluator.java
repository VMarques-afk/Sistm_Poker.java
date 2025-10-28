import jdk.jshell.spi.ExecutionControl;

import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {

    public ResultadoMao avaliarMao(List<Carta> cartas) {

        Map<Valor, Integer> contagemDeValores = getContagemDeValores(cartas);
        Map<Naipe, Integer> contagemDeNaipes = getContagemDeNaipes(cartas);

        List<Valor> valoresOrdenados = getValoresOrdenados(cartas);

        ResultadoMao resultado;

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

        /*resultado = identificarPar(contagemDeValores, valoresOrdenados);
        if (resultado != null) return resultado; */

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

    /*private List<Valor> getKickers(List<Valor> valoresOrdenados, Valor valorDaMao, int numKickers) {
        List<Valor> kickers = new ArrayList<>();
        for (Valor v : valoresOrdenados) {
            if (v != valorDaMao) {
                kickers.add(v);
                if (kickers.size() == numKickers) {
                    break;
                }
            }
        }
        return kickers;
    }
    removido pois gerava conflito no codigo
    */

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

        List<Valor> kickers = valoresOrdenados.subList(0, 5);
        return new ResultadoMao(TipoMao.CARTA_ALTA, kickers);
    }

    /*private List<Valor> getKickers(List<Valor> valoresOrdenados, Valor valorDaMao, int numKickers) {
        List<Valor> kickers = new ArrayList<>();
        for (Valor v : valoresOrdenados) {
            if (v != valorDaMao) {
                kickers.add(v);
                if (kickers.size() == numKickers) {
                    break;
                }
            }
        }
        return kickers;
    }
     */

    /*private ResultadoMao identificarUmPar(Map<Valor, Integer> contagem, List<Valor> valoresOrdenados) {
        if (!contagem.containsValue(2) || contagem.containsValue(3) || contagem.values().stream().filter(c-> c ==2).count() > 1) {
            return null;
        }

        Valor valorDoPar = null;
        for (Map.Entry<Valor, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == 2) {
                valorDoPar = entry.getKey();
                break;
            }
        }
        List<Valor> maoFinal = new ArrayList<>();
        maoFinal.add(valorDoPar);
        maoFinal.addAll(getKickers(valoresOrdenados, valorDoPar, 3));

        return new ResultadoMao(TipoMao.UM_PAR, maoFinal);
    }*/

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

        maoFinal.addAll(getKickers(valoresOrdenados, List.of(valorDoPar), 3));

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
        maoFinal.addAll(getKickers(valoresOrdenados, List.of(valorDaTrinca), 2));
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
        } else if (!valoresOrdenados.isEmpty()) {
            maoFinal.add(valoresDosPares.get(2));
        }

        return new ResultadoMao(TipoMao.DOIS_PARES, maoFinal);
    }
/*
        Valor parAlto = valoresDosPares.get(0);
        Valor parBaixo = valoresDosPares.get(1);

        List<Valor> maoFinal = new ArrayList<>();
        maoFinal.add(parAlto);
        maoFinal.add(parBaixo);

        List<Valor> tempValores = new ArrayList<>(valoresOrdenados);

        tempValores.remove(parAlto);
        tempValores.remove(parAlto);

        tempValores.remove(parBaixo);
        tempValores.remove(parBaixo);


        Map<Valor, Integer> remainingCoutns = new HashMap<>();
        for (Valor v : tempValores) {
            remainingCoutns.put(v, remainingCoutns.getOrDefault(v, 0) + 1);
        }

        Valor kicker = null;
        for (Valor v : tempValores) {
            if (remainingCoutns.get(v) == 1) {
                break;
            }
        }

        if (kicker == null && !tempValores.isEmpty()) {
            kicker = tempValores.get(0);
        }

        if (kicker != null) {
            maoFinal.add(kicker);

        }

        return new ResultadoMao(TipoMao.DOIS_PARES, maoFinal);

    }*/

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

        List<Valor> maoFinal = valoresDoFlush.subList(0, 5);

        return new ResultadoMao(TipoMao.FLUSH, maoFinal);
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

        maoFinal.addAll(getKickers(valoresOrdenados, List.of(valorDaQuadra), 1));

        return new ResultadoMao(TipoMao.QUADRA, maoFinal);
    }


}
