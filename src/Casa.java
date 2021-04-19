import java.util.ArrayList;

public class Casa {
    private String elemento;
    private int posicao;
    private ArrayList<Integer> vizinhos;

    public Casa(String elemento, int posicao){
        this.elemento = elemento;
        this.posicao = posicao;
        vizinhos = new ArrayList<>();
    }

    public Casa(Casa casa){
        this.elemento = casa.getElemento();
        this.posicao = casa.getPosicao();
        this.vizinhos = casa.getVizinhos();
    }

    public String getElemento(){
        return this.elemento;
    }

    public void setElemento(String caracter){
        this.elemento = caracter;
    }

    public int getPosicao(){
        return this.posicao;
    }

    public void setPosicao(int posicao){
        if (posicao >= 0)
            this.posicao = posicao;
    }

    public ArrayList<Integer> getVizinhos(){
        return new ArrayList<>(this.vizinhos);
    }

    public void addVizinho(int posicao){
        this.vizinhos.add(posicao);
    }

}
