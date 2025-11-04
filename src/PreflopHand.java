public class PreflopHand {

    private final Valor valorAlto;
    private final Valor valorBaixo;
    private final boolean isSuited;
    private final boolean isPair;
    private final String notacao;


    public PreflopHand( Carta c1, Carta c2) {
        Valor v1 = c1.getValor();
        Valor v2 = c2.getValor();

        this.isSuited = (c1.getNaipe() == c2.getNaipe());
        this.isPair = (v1 == v2);

        if (v1.getValorNumerico() > v2.getValorNumerico()) {
            this.valorAlto = v1;
            this.valorBaixo = v2;
        } else {
            this.valorAlto = v2;
            this.valorBaixo = v1;
        }
        this.notacao = costruirNotacao();
    }

    private String costruirNotacao() {

        String vAltoStr = this.valorAlto.getNotacao();
        String vBaixoStr = this.valorBaixo.getNotacao();

        if (this.isPair) {
            return vAltoStr + vBaixoStr;
        } else if (this.isSuited) {
            return vAltoStr + vBaixoStr + "s";
        } else {
            return vAltoStr + vBaixoStr + "o";
        }

    }

    public String getNotacao() {
        return this.notacao;
    }

    @Override
    public String toString() {
        return this.notacao;
    }
}
