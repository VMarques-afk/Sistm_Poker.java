import java.util.ArrayList;
import java.util.List;

public class RangeBuilder {

    public static List<List<Carta>> construirRange(String rangeText) {
        List<List<Carta>> rangeFinal = new ArrayList<>();

        String[] partes = rangeText.split(",");

        for (String parte : partes) {
            String maoStr = parte.trim().toUpperCase();
            if (maoStr.length() < 2) continue;

            char v1Char = maoStr.charAt(0);
            char v2Char = maoStr.charAt(1);

            Valor valor1 = charParaValor(v1Char);
            Valor valor2 = charParaValor(v2Char);

            if (valor1 == null || valor2 == null) continue;

            if (valor1 == valor2) {
                Naipe[] naipes = Naipe.values();
                for (int i = 0; i < naipes.length; i++) {
                    for (int j = i + 1; j < naipes.length; j++) {
                        List<Carta> mao = new ArrayList<>();
                        mao.add(new Carta(naipes[i], valor1));
                        mao.add(new Carta(naipes[j], valor1));
                        rangeFinal.add(mao);
                    }
                }
            } else if (maoStr.length() == 3 && maoStr.charAt(2) == 'S') {
                for (Naipe n : Naipe.values()) {
                    List<Carta> mao = new ArrayList<>();
                    mao.add(new Carta(n, valor1));
                    mao.add(new Carta(n, valor2));
                    rangeFinal.add(mao);
                }
            } else if (maoStr.length() == 3 && maoStr.charAt(2) == 'O') {
                for (Naipe n1 : Naipe.values()) {
                    for (Naipe n2 : Naipe.values()) {
                        if (n1 != n2) {
                            List<Carta> mao = new ArrayList<>();
                            mao.add(new Carta(n1, valor1));
                            mao.add(new Carta(n2, valor2));
                            rangeFinal.add(mao);
                        }
                    }
                }
            }
        }
        return rangeFinal;
    }

    private static Valor charParaValor(char c) {
        switch (c) {
            case 'A':
                return Valor.AS;
            case 'K':
                return Valor.REI;
            case 'Q':
                return Valor.DAMA;
            case 'J':
                return Valor.VALETE;
            case 'T':
                return Valor.DEZ;
            case '9':
                return Valor.NOVE;
            case '8':
                return Valor.OITO;
            case '7':
                return Valor.SETE;
            case '6':
                return Valor.SEIS;
            case '5':
                return Valor.CINCO;
            case '4':
                return Valor.QUATRO;
            case '3':
                return Valor.TRES;
            case '2':
                return Valor.DOIS;
            default:
                return null;
        }
    }
}
