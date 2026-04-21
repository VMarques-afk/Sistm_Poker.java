import java.util.List;

public class ResultadoMao implements Comparable<ResultadoMao> {

    private final TipoMao tipoMao;
    private final List<Valor> valoresDaMao;

    public ResultadoMao(TipoMao tipoMao, List<Valor> valoresDaMao) {
        this.tipoMao = tipoMao;
        this.valoresDaMao = valoresDaMao;
    }

    public TipoMao getTipoMao() {

        return tipoMao;
    }

    public List<Valor> getValoresDaMao() {

        return valoresDaMao;
    }


    @Override
    public int compareTo(ResultadoMao outro) {
        //compara força ad mão principal
        if (this.tipoMao.getForca() > outro.tipoMao.getForca()) {
            return 1;
        }
        if (this.tipoMao.getForca() < outro.tipoMao.getForca()) {
            return -1;
        }
        //encontra o menor da lista para evitar indexOutOfBounds
        int tamanhoMinimo = Math.min(this.valoresDaMao.size(), outro.valoresDaMao.size());

        //comapra valores apenas onde é seguro
        /*for (int i = 0; i < this.valoresDaMao.size(); i++)*/
        for (int i = 0; i < tamanhoMinimo; i++) {
            int nossoValor = this.valoresDaMao.get(i).getValorNumerico();
            int outroValor = outro.valoresDaMao.get(i).getValorNumerico();

            if (nossoValor > outroValor) {
                return 1;
            }
            if (nossoValor < outroValor) {
                return -1;
            }
        }
        //fallback de segurança se todos os kickers comparados empatarem
        //mas as listas tem tamanhos diferentes, a lista maior ganha.
        if (this.valoresDaMao.size() > outro.valoresDaMao.size()) {
            return 1;
        }
        if (this.valoresDaMao.size() < outro.valoresDaMao.size()) {
            return -1;
        }

        return 0;
    }

   /* @Override
    public String toString() {
        return tipoMao + " com valores " + valoresDaMao;
    }*/
}