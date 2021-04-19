import java.util.ArrayList;

/**
 * Classe que possui os métodos que executam as funções da árvore de pesquisa
 */
public class No {
    private Mapa estado;                //Atributo Mapa do tabuleiro
    private int posPai;                 //Posição do nó Pai
    private ArrayList<No> sucessores;   //Lista de nós sucessores
    private int posicao;                //Atributo posição do nó
    private int nivel;                  //Atributo nivel do nó na árvore

    /**
     * Construtor do Nó da arvore
     * @param estado - estado do nó
     * @param posicao - posicao
     * @param posPai - posicao  do nó pai
     */
    public No(Mapa estado, int posicao, int posPai){
        this.estado = estado;
        this.posicao = posicao;
        this.posPai = posPai;
        this.sucessores = new ArrayList<>();
        this.nivel= 0;
    }

    /**
     * Setter do atributo nivel
     * @param valor - valor a atribuir
     */
    public void setNivel(int valor){
        this.nivel = valor;
    }

    /**
     * Getter do atributo nivel na aŕvore de pesquisa
     * @return valor do atributo nivel
     */
    public int getNivel(){
        return this.nivel;
    }

    /**
     * Getter do atributo posicao
     * @return valor do atributo posição
     */
    public int getPosicao(){
        return this.posicao;
    }

    /**
     * Setter do atributo posicao que coloca o valor  no valor inicial
     */
    public void resetPosicao(){
        this.posicao = -1;
    }

    /**
     * Função que retorna a lista de letras no tabuleiro
     * @return Lista de letras no tabuleiro
     */
    public ArrayList<Character> getLetras(){
        return this.estado.getLetras();
    }

    /**
     * Função que retorna o valor da ultima letra na lista de letras
     * @return valor do ultimo elemento da lista de letras
     */
    public String getLastLetra(){
        return this.estado.getLastLetra();
    }

    /**
     * Getter do atributo estado (mapa do tabuleiro)
     * @return retorna o mapa do tabuleiro
     */
    public Mapa getEstado(){
        return this.estado;
    }

    /**
     * Função que executa a função de validação do caminho
     * @param letra letra a procurar
     * @param elemento elemento que deve existir no caminho
     * @return True caso seja valido | False caso seja inválido
     * @throws NullPointerException Excepção
     */
    public boolean caminhoValido(String letra,String elemento) throws NullPointerException {
        return this.estado.caminhoValido(letra, elemento);
    }

    /**
     * Getter que retorna a lista de vizinhos de uma casa
     * @param posicao posição da casa
     * @return lista de vizinhos
     */
    public ArrayList<Integer> getVizinhos(int posicao){
        return this.getEstado().getCasa(posicao).getVizinhos();
    }

    /**
     * Getter que retorna a quantidade de nós sucessores do nó
     * @return valor da quantidade de sucessores do nó
     */
    public int getQuantidadeSucessores(){
        return this.sucessores.size();
    }

    /**
     * Gettter que retorna a lista de sucessores do nó removendo-os do nó de origem
     * @return lista de sucessores do nó
     */
    public ArrayList<No> getSucessores(){
        ArrayList<No> lista = new ArrayList<>(this.sucessores);
        this.sucessores.clear();
        return lista;
    }

    /**
     * Função que executa a função que retorna os dados do mapa numa string
     * @return string com os dados do mapa para imprimir no ecrã
     */
    public String toString(){
        return this.estado.toString();
    }

    /**
     * Função que cria cada novo nó sucessor
     * @param posicao - posição casa a alterar
     * @param letra - letra a colocar na casa
     * @param posPai - posição do nó pai
     * @param no - nó onde deve copiar o mapa
     */
    public void gerarMutacao(int posicao, String letra, int posPai, No no){
        try{
            //verifica se o elemento da casa do tabuleiro está vazio
            if (this.estado.getElementoCasa(posicao).compareTo(".")==0){
                //Cria um novo nó
                No novo = new No(no.getEstado().getCopiaMapa(), posicao, posPai);
                //Altera o elemnto da caso na posição indicada e adiciona na lista de cucessores
                novo.getEstado().setCasa(posicao, letra.toLowerCase());
                this.sucessores.add(novo);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    /**
     * Função que cria cada novo nó sucessor com nivel
     * @param posicao - posição casa a alterar
     * @param letra - letra a colocar na casa
     * @param posPai - posição do nó pai
     * @param no - nó onde deve copiar o mapa
     * @param nivel nvivel do nó
     */
    public void gerarMutacao(int posicao, String letra, int posPai, No no, int nivel){
        try{
            //verifica se o elemento da casa do tabuleiro está vazio
            if (this.estado.getElementoCasa(posicao).compareTo(".")==0){
                //Cria um novo no
                No novo = new No(no.getEstado().getCopiaMapa(), posicao, posPai);
                //Altera o elemento da cas na posição indicada e o nivel do no e adiciona o novo nó na lista de sucessores
                novo.getEstado().setCasa(posicao, letra.toLowerCase());
                novo.setNivel(nivel);
                this.sucessores.add(novo);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    /**
     * Função que cria os sucessores do nó
     * @param no nó que deve replicar
     * @param letra - letra a alterar
     */
    public void gerarSucessores(No no, String letra){
        try {
            ArrayList<Integer> vizinhos;
            int posicao;
            //verifica se é o nó inicial
            if (no.getPosicao() == -1) {
                //Obtem a primeira casa do tabuleiro que contêm a letra
                Casa casa = no.getEstado().getCasa(letra);
                //Obtem a posição e os vizinhos dessa casa
                posicao = casa.getPosicao();
                vizinhos = casa.getVizinhos();
            //Se não for o nó inicial
            } else {
                //Obtêm a posição e os vizinhos da casa do nó fornecido
                posicao = no.getPosicao();
                vizinhos = getVizinhos(no.getPosicao());
            }
            //Para cada vizinho vai gerar um novo nó sucessor
            for (Integer i : vizinhos) {
                gerarMutacao(i, letra, posicao, no);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

    /**
     * Função que cria os sucessores do nó com nivel
     * @param no no que deve replicar
     * @param letra letra que deve alterar
     * @param nivel nivel do no
     */
    public void gerarSucessores(No no, String letra, int nivel){
        try {
            ArrayList<Integer> vizinhos;
            int posicao;
            //verifica se é o nó inicial
            if (no.getPosicao() == -1) {
                //Obtem a primeira casa do tabuleiro que contêm a letra
                Casa casa = no.getEstado().getCasa(letra);
                //Obtem a posição e os vizinhos dessa casa
                posicao = casa.getPosicao();
                vizinhos = casa.getVizinhos();
            //Se não for o nó inicial
            } else {
                //Obtêm a posição e os vizinhos da casa do nó fornecido
                posicao = no.getPosicao();
                vizinhos = getVizinhos(no.getPosicao());
            }
            //Para cada vizinho vai gerar um novo nó sucessor
            for (Integer i : vizinhos) {
                gerarMutacao(i, letra, posicao, no, nivel);
            }
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        }
    }

}
