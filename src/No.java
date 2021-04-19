import java.util.ArrayList;

public class No {
    private Mapa estado;
    private int posPai;
    private ArrayList<No> sucessores;
    private int posicao;
    private int nivel;

    public No(Mapa estado, int posicao, int posPai){
        this.estado = estado;
        this.posicao = posicao;
        this.posPai = posPai;
        this.sucessores = new ArrayList<>();
        this.nivel= 0;
    }

    public void setNivel(int valor){
        this.nivel = valor;
    }

    public int getNivel(){
        return this.nivel;
    }

    public void gerarMutacao(int posicao, String letra, int posPai, No no){
        try{
            if (this.estado.getElementoCasa(posicao).compareTo(".")==0){
                No novo = new No(no.getEstado().getCopiaMapa(), posicao, posPai);
                novo.getEstado().setCasa(posicao, letra.toLowerCase());
                this.sucessores.add(novo);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    public void gerarMutacao(int posicao, String letra, int posPai, No no, int nivel){
        try{
            if (this.estado.getElementoCasa(posicao).compareTo(".")==0){
                No novo = new No(no.getEstado().getCopiaMapa(), posicao, posPai);
                novo.getEstado().setCasa(posicao, letra.toLowerCase());
                novo.setNivel(nivel);
                this.sucessores.add(novo);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    public void gerarSucessores(No no, String letra){
        try {
            ArrayList<Integer> vizinhos;
            int posicao;
            if (no.getPosicao() == -1) {
                Casa casa = no.getEstado().getCasa(letra);
                posicao = casa.getPosicao();
                vizinhos = casa.getVizinhos();
            } else {
                posicao = no.getPosicao();
                vizinhos = getVizinhos(no.getPosicao());
            }
            for (Integer i : vizinhos) {
                gerarMutacao(i, letra, posicao, no);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    public void gerarSucessores(No no, String letra, int nivel){
        try {
            ArrayList<Integer> vizinhos;
            int posicao;
            if (no.getPosicao() == -1) {
                Casa casa = no.getEstado().getCasa(letra);
                posicao = casa.getPosicao();
                vizinhos = casa.getVizinhos();
            } else {
                posicao = no.getPosicao();
                vizinhos = getVizinhos(no.getPosicao());
            }
            for (Integer i : vizinhos) {
                gerarMutacao(i, letra, posicao, no, nivel);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    public ArrayList<Character> getLetras(){
        return this.estado.getLetras();
    }

    public String getLastLetra(){
        return this.estado.getLastLetra();
    }


    public Mapa getEstado(){
        return this.estado;
    }

    public int getPosicao(){
        return this.posicao;
    }

    public int getPosPai() {
        return this.posPai;
    }

    public ArrayList<Integer> getVizinhos(int posicao){
        return this.getEstado().getCasa(posicao).getVizinhos();
    }

    public int getQuantidadeSucessores(){
        return this.sucessores.size();
    }

    public ArrayList<No> getSucessores(){
        ArrayList<No> lista = new ArrayList<>(this.sucessores);
        this.sucessores.clear();
        return lista;
    }

    public void resetPosicao(){
        this.posicao = -1;
    }

    public String toString(){
        return this.estado.toString();
    }

    public boolean caminhoValido(String letra,String elemento) throws NullPointerException {
        return this.estado.caminhoValido(letra, elemento);
    }
}
