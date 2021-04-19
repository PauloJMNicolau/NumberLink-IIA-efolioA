import java.util.ArrayList;

/**
 * Classe que possui os métods que gerem os dados de uma posição do tabuleiro
 */
public class Casa {
    private String elemento;                //Atributo que guarda o elemnto da posição do tabuleiro
    private int posicao;                    //Atributo que guarda a posição da casa no tabuleiro
    private ArrayList<Integer> vizinhos;    //Lista de posições das casas vizinhas

    /**
     * Construtor do objeto Casa no tabuleiro
     * @param elemento - valor do elemento
     * @param posicao - valor da posição
     */
    public Casa(String elemento, int posicao){
        this.elemento = elemento;
        this.posicao = posicao;
        vizinhos = new ArrayList<>();
    }

    /**
     * Construtor por copia do objeto Casa no tabuleiro
     * @param casa - Posição do tabuleiro a copiar
     */
    public Casa(Casa casa){
        this.elemento = casa.getElemento();
        this.posicao = casa.getPosicao();
        this.vizinhos = casa.getVizinhos();
    }

    /**
     * Getter do elemento na posição do tabuleiro
     * @return - elemento na posição do tabuleiro
     */
    public String getElemento(){
        return this.elemento;
    }

    /**
     * Settter do elemnto na posição do tabuleiro
     * @param caracter - caracter a colocar na posição
     */
    public void setElemento(String caracter){
        this.elemento = caracter;
    }

    /**
     * Getter do atributo posição
     * @return valor da posição
     */
    public int getPosicao(){
        return this.posicao;
    }

    /**
     * Getter do atributo vizinhos
     * @return - retorna uma copia da lista de posições vizinhas
     */
    public ArrayList<Integer> getVizinhos(){
        return new ArrayList<>(this.vizinhos);
    }

    /**
     * Função que adiciona elementos na lista de vizinhos
     * @param posicao - valor de posição a adicionar
     */
    public void addVizinho(int posicao){
        this.vizinhos.add(posicao);
    }

}
