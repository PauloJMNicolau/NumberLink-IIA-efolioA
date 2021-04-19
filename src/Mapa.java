import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe que possui os métodos de gestão do tabuleiro
 */
public class Mapa {
    private ArrayList<Casa> casas;          //Lista com as várias posições no tabuleiro
    private int N;                          //Numero de Linhas do tabuleiro
    private int M;                          //Número de Colunas do tabuleiro
    private int K;                          //Número de letras no tabuleiro
    private ArrayList<Character> letras;    //Lista de Letras esperadas no tabuleiro

    /**
     * Construtor do objeto Mapa
     * @param nome - Nome do ficheiro a ler
     */
    public Mapa(String nome){
        try{
            //Abre o ficheiro para leitura
            File ficheiro = new File(nome);
            Scanner leitor = new Scanner(ficheiro);
            //inicializa  os atributos de lista
            this.casas = new ArrayList<>();
            this.letras = new ArrayList<>();
            //Inicializa variáveis auxiliares
            int pos = 0;
            int posMapa = 0;
            //Enquanto tiver linhas para ler, executa ciclo
            while(leitor.hasNextLine()){
                //Obtem a linha do ficheiro
                String linha = leitor.nextLine();
                //Se for a priemira linha
                if(pos == 0){
                    //Separa o valor da linha quando encontra espaços em branco
                    String[] dados = linha.split(" ", 0);
                    //Atribui os valores obtidos aos respetivos atributos
                    this.N = Integer.parseInt(dados[0]);
                    this.M = Integer.parseInt(dados[1]);
                    this.K = Integer.parseInt(dados[2]);
                    //Com base no valor de K vai criar uma sequencia de letras começando no "A"
                    char letra = 'A';
                    while(this.letras.size()< this.K) {
                        this.letras.add(letra);
                        letra++;
                    }
                //Se for a segunda linha do ficheiro ignora
                } else if (pos == 1){
                    pos++;
                    continue;
                //Se for as restantes linhas vai separar quando emcontra espaços em branco
                } else{
                    String[] dados = linha.split(" ", 0);
                    //Para cada valor obtido na linha vai criar uma casa do tabuleiro
                    for (String valor: dados) {
                        casas.add(new Casa(valor, posMapa++));
                    }
                }
                pos++;
            }
            //Termina a leitura do ficheiro
            leitor.close();
            //Adiciona os vizinhos nas casas
            adicionarVizinhos();
        } catch (FileNotFoundException erro){
            System.out.println("Erro: Ficheiro não encontrado!");
            System.exit(1);
        }
    }

    /**
     * Construtor por cópia do mapa
     * @param mapa - mapa a ser copiado
     */
    public Mapa(Mapa mapa){
        this.casas = mapa.getCasas();
        this.K = mapa.getK();
        this.N = mapa.getN();
        this.M = mapa.getM();
        this.letras = mapa.getLetras();
    }

    /**
     * Função que atribui os vizinhos a cada casa do tabuleiro
     */
    private void adicionarVizinhos(){
        //Para cada casa no tabuleiro
        for(int i = 0; i < casas.size(); i++) {
            //Se for o canto superior esquerdo
            if (i == 0) {
                casas.get(i).addVizinho(i+1);
                casas.get(i).addVizinho(i + this.M);
            //Se for o canto superior direito
            } else if (i == this.M - 1) {
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + this.M);
            //Se for o canto inferior esquerdo
            } else if (i == this.casas.size() - this.M) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i + 1);
            //Se for o canto inferior direito
            } else if (i == this.casas.size() - 1) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
            //Se for linha limite superior
            } else if (i <= this.M - 2) {
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + 1);
                casas.get(i).addVizinho(i + this.M);
            //Se for linha limite inferior
            } else if (i > this.casas.size() - this.M && i < this.casas.size() - 1) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + 1);
            //Se for linha limite esquerdo
            } else if ((i != this.casas.size() - this.M) && (i % this.M == 0)) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i + 1);
                casas.get(i).addVizinho(i + this.M);
            //Se for linha limite direito
            } else if ((i != this.M) && (i != this.casas.size() - 1) && (i % this.M == this.M - 1)) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + this.M);
            //Se for posição interior do tabuleiro
            } else {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + 1);
                casas.get(i).addVizinho(i + this.M);
            }
        }
    }

    /**
     * Getter de uma posição no tabuleiro por posição
     * @param posicao - posição a obter
     * @return - posição do tabuleiro
     */
    public Casa getCasa(int posicao){
        return this.casas.get(posicao);
    }

    /**
     * Getter da primeira posição no tabuleiro que possui a letra
     * @param letra - letra que deve procurar
     * @return Casa se encontrar | Null se não encontrar
     */
    public Casa getCasa(String letra){
        //Para cada casa no tabuleiro verifica se possui a letra como eleemnto
        for(Casa casa : this.casas){
            if(casa.getElemento().compareTo(letra)==0)
                return new Casa(casa);
        }
        return null;
    }

    /**
     * Getter da primeira posição no tabuleiro que possui a letra, depois da posição fornecida
     * @param letra - letra a procurar
     * @param posicao posição onde iniciar
     * @return Casa se encontrar | Null se não encontrar
     */
    public Casa getCasa(String letra, int posicao){
        //Para cada posição no tabuleiro após a posição fornecida verifica se a casa posuui a letra como elemento
        for (int i = posicao+1; i < this.casas.size(); i++){
            Casa casa = getCasa(i);
            if(casa.getElemento().compareTo(letra)==0)
                return new Casa(casa);
        }
        return null;
    }

    /**
     * Setter do elemnto da casa na posição fornecida
     * @param posicao - posição no tabuleiro da casa a alterar
     * @param elemento - elemento a adicionar
     */
    public void setCasa(int posicao, String elemento){
        this.casas.get(posicao).setElemento(elemento);
    }

    /**
     * Getter do atributo casas
     * @return copia da lista de posilções no tabuleiro
     */
    public ArrayList<Casa> getCasas(){
        ArrayList<Casa> temp = new ArrayList<>();
        for (Casa casa : this.casas){
            temp.add(new Casa(casa));
        }
        return temp;
    }

    /**
     * Getter do elemento da casa na posição fornecida
     * @param posicao - posição da casa no tabuleiro
     * @return Valor do elemento da casa
     */
    public String getElementoCasa(int posicao){
        return casas.get(posicao).getElemento();
    }

    /**
     * Getter do atributo lista de Letras no tabuleiro
     * @return copia da lista de letras
     */
    public ArrayList<Character> getLetras(){
        return new ArrayList<>(this.letras);
    }

    /**
     * Getter da ultima letra na lista de letras
     * @return letra do fim da lista
     */
    public String getLastLetra(){
        return this.letras.get(letras.size()-1).toString();
    }

    /**
     * Getter do atributo K
     * @return valor de K
     */
    public int getK(){
        return this.K;
    }

    /**
     * Getter do atributo M
     * @return valor de M
     */
    public int getM(){
        return this.M;
    }

    /**
     * Getter do atributo N
     * @return valor de N
     */
    public int getN(){
        return this.N;
    }

    /**
     * Getter do tabuleiro
     * @return copia do tabuleiro
     */
    public Mapa getCopiaMapa(){
        return new Mapa(this);
    }

    /**
     * Função que cria uma string do mapa para ser imprimida no ecrã
     * @return string com os dados do tabuleiro formatados em forma de matriz
     */
    public String toString(){
        String str = "";
        for(int i=0; i< this.casas.size(); i++){
            str = str.concat(this.casas.get(i).getElemento());
            if( i % this.M == this.M-1){
                str = str.concat("\n");
            } else {
                str = str.concat(" ");
            }
        }
        return str;
    }

    /**
     * Função que valida se existe um caminho valido entre as letras
     * Valida tanto caminhos vazios como caminhos de uma letra
     * @param letra letra que deve testar a existencia do caminho
     * @param elemento elemento que deve estar nas casas do caminho
     *                 -Para caminhos vaziod deve ser "."
     *                 -Para caminhos da eltra deve ser a letra minuscula
     * @return True no caso de caminho valido | False no caso de caminho inválido
     */
    public boolean caminhoValido(String letra, String elemento){
        try {
            //Obtem as duas casas do tabuleiro com a letra
            Casa casaOrigem = getCasa(letra);
            Casa casaDestino = getCasa(letra, casaOrigem.getPosicao());
            //Obtem os vizinhos da casa de origem
            ArrayList<Integer> vizinhos = casaOrigem.getVizinhos();
            //Se nos vizinhos da casa original existir a posição da casa de destino
            if (vizinhos.contains(casaDestino.getPosicao())) {
                return true;
            //Se nos vizinhos da casa de origem não existir a posição da casa de destino
            } else {
                //Cria uma lista de posições visitadas e adiciona a posição de origem
                ArrayList<Integer> visitados = new ArrayList<>();
                visitados.add(casaOrigem.getPosicao());
                //Percorre o caminho até chegar à casa de destino
                return percorrer(elemento, casaDestino.getPosicao(), vizinhos, visitados);
            }
        } catch (NullPointerException erro){
            throw new NullPointerException("Erro: Sequencia de Letras Inválida");
        }
    }

    /**
     * Função que percorre o caminho desde a origem até ao destino
     * @param elemento elemento que deve estar na casa
     * @param posDestino posição do destino
     * @param vizinhos lista de vizinhos
     * @param visitados lista de posições visitados
     * @return True caso seja valido o caminho | False caso seja caminho inválido
     */
    private boolean percorrer(String elemento, int posDestino, ArrayList<Integer> vizinhos, ArrayList<Integer> visitados){
        //Enquanto tiver vizinhos para verificar
        while(vizinhos.size()!= 0){
            //Remove a primeira posição da lista de vizinhos e adiciona na lista de visitados
            int posicao = vizinhos.remove(0);
            visitados.add(posicao);
            //Obtem a casa na posição
            Casa casa = getCasa(posicao);
            //Se a casa possui o elemento
            if(casa.getElemento().compareTo(elemento)==0) {
                //Obtem os vizinhos da casa
                ArrayList<Integer> temp = casa.getVizinhos();
                //verifica se nos vizinhos está a posição de destino
                if (temp.contains(posDestino)) {
                    return true;
                //Se não estiver a posição de destino
                } else {
                    //verifica se algum dos vizinhos já foi visitado
                    for(int i: visitados){
                        //caso exista remove da lista de vizinhos
                        if(temp.contains(i)){
                            temp.remove((Integer)i);
                        }
                    }
                    //Adiciona os vizinhos na lista
                    vizinhos.addAll(0, temp);
                }
            }
        }
        //Caso o caminho não seja valido
        return false;
    }
}
