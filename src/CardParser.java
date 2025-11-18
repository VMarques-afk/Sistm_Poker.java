public class CardParser {

    public static Carta parse(String textoCarta) {
        if (textoCarta == null || textoCarta.length() != 2) {
            return null;
        }

        String valorStr = textoCarta.substring(0, 1).toUpperCase();
        String naipeStr = textoCarta.substring(1, 2).toUpperCase();


        Valor valor = null;
        Naipe naipe = null;

        //TRADUZ A LETRA DO VALOR
        switch (valorStr) { //
            case "A": valor = Valor.AS; break;
            case "K": valor = Valor.REI; break;
            case "Q": valor = Valor.DAMA; break;
            case "J": valor = Valor.VALETE; break;
            case "T": valor = Valor.DEZ; break;
            case "9": valor = Valor.NOVE; break;
            case "8": valor = Valor.OITO; break;
            case "7": valor = Valor.SETE; break;
            case "6": valor = Valor.SEIS; break;
            case "5": valor = Valor.CINCO; break;
            case "4": valor = Valor.QUATRO; break;
            case "3": valor = Valor.TRES; break;
            case "2": valor = Valor.DOIS; break;
        }

        switch (naipeStr) {

            case "C": naipe = Naipe.COPAS; break;
            case "P": naipe = Naipe.PAUS; break;
            case "O": naipe = Naipe.OUROS; break;
            case "E": naipe = Naipe.ESPADAS; break;
        }
        if (valor != null && naipe != null) {
            return new Carta(naipe, valor);
        } else {
            return null;
        }
    }
}
