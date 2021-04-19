import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe que possui os métodos que executam os algoritmos de Procura
 */
public class Algoritmo {
    private No solucao;         //Atributo que guarda a solução encontrada
    private int expansoes;      //Atributo que conta a quantidade de nós expandidos
    private int geracoes;       //Atributo que conta a quantidade de nós gerados
    private long tempo;         //Atributo que guarda o tempo de execução do algoritmo

    /**
     * Construtor do objeto Algoritmo
     * @param tabuleiro - valor do tabuleiro a testar
     */
    public Algoritmo(String tabuleiro){
        //Cria o caminho para o ficheiro com os dados do tabuleiro
        String instancia = "instancias/instancia";
        instancia = instancia.concat(tabuleiro);
        instancia = instancia.concat(".txt");
        //Cria um mapa do tabuleiro com os dados do ficheiro indicado
        Mapa mapa = new Mapa(instancia);
        //Cria o Nó inicial do problema
        this.solucao = new No(mapa, -1,-1);
        //Inicializa os atributos contadores
        this.expansoes = 0;
        this.geracoes = 1;
        this.tempo = 0;
    }

    /**
     * Função que executa os algoritmos de procura
     * @param algoritmo - recebe o nome do algoritmo que deve executar
     */
    public void executa(String algoritmo){
        try {
            //Inicializa o tempo
            this.updateTempo();
            //Caso seja para executar o algoritmo de "Largura Primeiro"
            if (algoritmo.compareTo("Largura") == 0) {
                this.updateSolucao(this.ProcuraLargura("A", this.solucao));
                //Verifica se obteve solução
                if (this.solucao != null)
                    if (!eSolucao()) {
                        this.eliminarSolucao();
                    }
            //Caso seja para executar o algoritmo de "Profundidade Primeiro"
            } else if (algoritmo.compareTo("Profundidade") == 0) {
                this.updateSolucao(this.ProcuraProfundidade("A", this.solucao));
                //Verifica se obteve solução
                if (this.solucao != null)
                    if (!eSolucao()) {
                        this.eliminarSolucao();
                    }
            //Caso seja para executar o algoritmo de "Proundidade Limitada"
            } else if (algoritmo.compareTo("Limitada") == 0) {
                //Solicita o limite ao utilizador
                Scanner op = new Scanner(System.in);
                System.out.print("Indique o Limite: ");
                int limite = op.nextInt();
                //Executa o algoritmo
                this.updateSolucao(this.ProcuraProfundidadeLimitada("A", this.solucao, limite));
                //Verifica se existe solução
                if (this.solucao != null)
                    if (!eSolucao()) {
                        this.eliminarSolucao();
                    }
            //Caso seja para executar o algoritmo de "Profundidade iterativa"
            } else if (algoritmo.compareTo("Iterativa") == 0) {
                //inicializa o limite a 0 e estado inicial do mapa
                int limite = 0;
                No aux = this.solucao;
                /*Inicia execução até encontrar solução
                * (não termina programa se nunca encontrar solução - mesmo para os mapas que não possuem resolução)
                */
                while(limite >= 0) {
                    this.updateSolucao(this.ProcuraProfundidadeLimitada("A", aux, limite));
                    //Incrementa limite
                    limite++;
                    //Verifica se obteve solução
                    if (this.solucao != null)
                        if (!eSolucao()) {
                            this.eliminarSolucao();
                        //Caso encontre solução atualiza limite para valor que permite terminar o ciclo
                        } else{
                            limite = -1;
                        }
                }
            }
            //Volta a atualizar o tempo
            this.updateTempo();
        } catch (StackOverflowError | OutOfMemoryError erro){
            System.out.println("Erro: Falta de Memória");
            eliminarSolucao();
        } catch (NullPointerException erro){
            System.out.println(erro.toString());
            eliminarSolucao();
        }
    }

    /**
     * Função que valida se existe caminho luivre para as restantes letras superiores à letra fornecida
     * @param letra - Letra atual
     * @param no - No atual
     * @return  True - caso caminho livre | False - caso caminho ocupado
     * @throws NullPointerException - Excepção que pode retornar
     */
    private boolean caminhosLivres(char letra, No no) throws NullPointerException{
        ArrayList<Character> aux = new ArrayList<>();
        ArrayList<Character> temp = no.getLetras();
        /*
         * Verifica as letras que deve testar
         * Percorre a lista de letras  do No e caso seja superior à letra fornecida adiciona na lista auxiliar
         */
        for(char l : temp) {
            if (l > letra)
                aux.add(l);
        }
        /*
         * Verifica se para cada letra na lista auxiliar o caminho é valido
         * Valida caso exista casas vazias no tabuleiro entre as duas letras iguais
         */
        for(Character l: aux){
            boolean valido = no.caminhoValido(l.toString(), ".");
            if(!valido){
                return false;
            }
        }
        return true;
    }

    /**
     * Função que verifica se a solução encontrada é solução final do problema
     * @return True - caso seja solução | False - caso não seja solução
     */
    private boolean eSolucao(){
        boolean valido;
        //Percorre todas as letras do tabuleiro e verifica se existe um caminho válido a ligar os dois extremos
        for(Character letra : this.solucao.getLetras()){
            valido = solucao.caminhoValido(letra.toString(), letra.toString().toLowerCase());
            if(!valido){
                return false;
            }
        }
        return true;
    }

    /**
     * Algoritmo de Procura em Largura Primeiro
     * @param letra - recebe a letra que deve testar
     * @param no - recebe o nó atual
     * @return No com solução | Null - caso não tenha solução
     */
    private No ProcuraLargura(String letra, No no){
        try {
            //Coloca a posição do nó no estado inicial
            no.resetPosicao();
            //Cria uma lista auxiliar para os nós a expandir
            ArrayList<No> expandir = new ArrayList<>();
            expandir.add(no);
            /*
             * Executa enquanto existir nós para expandir
             * Caso encontre solução o ciclo é interrompido
             */
            while (expandir.size() != 0) {
                //Remove da lista de nós a expandir o nó que vai expandir - no inicial
                no = expandir.remove(0);
                //Verifica se o nó possui um caminho valido para a letra
                if (no.caminhoValido(letra, letra.toLowerCase())) {
                    /*
                     * Caso o caminho seja válido para a letra
                     * Verifica se as restantes letras ainda são possiveis de completar sem cruzar com a solução
                     */
                    if (caminhosLivres(letra.charAt(0), no)){
                        /*
                         * Caso seja possivel, executa recursivamente a função para a letra seguinte
                         */
                        if(letra.compareTo(no.getLastLetra())!=0){
                            int pos = no.getLetras().indexOf(letra.charAt(0));
                            No res = ProcuraLargura(no.getLetras().get(pos+1).toString(), no);
                            //Se obteve uma solução retorna o nó da solução encontrada
                            if (res != null)
                                return res;
                        /*
                         * Caso já tenha executado o algoritmo para todas as letras
                         * Retorna o nó da soluçãp encontrada
                        */
                        } else {
                            expandir.clear();
                            return no;
                        }
                    }
                /*
                 * Caso o nó que encontrou nãp seja a solução
                 * vai expandir o nó
                 */
                } else {
                    //Incrementa contador de expansões
                    this.incrementaExpansao();
                    //Cria os nós sucessores do nó
                    no.gerarSucessores(no, letra);
                    //Incrementa contador de nós gerados
                    incrementaGeracoes(no.getQuantidadeSucessores());
                    /*
                     * Como o algoritmo deve procurar primeiros os nós do mesmo nível da árvore de procura
                     * Os nós gerados são adicionados no final da lista de nós a expandir
                     */
                    expandir.addAll(no.getSucessores());
                }
            }
            //Não encontrou solução, retorna valor null
            return null;
        } catch (StackOverflowError erro){
            throw  new StackOverflowError();
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        } catch (NullPointerException erro) {
            throw new NullPointerException(erro.toString());
        }
    }

    /**
     * Função do algoritmo de "Procura em Profundidade Primeiro"
     * @param letra - Letra atual que deve testar
     * @param no - nó inicial
     * @return No com solução | Null - caso não encontre
     */
    private No ProcuraProfundidade(String letra, No no){
        try {
            //Coloca a posição do nó no estado inicial
            no.resetPosicao();
            //Cria uma lista auxiliar para os nós a expandir
            ArrayList<No> expandir = new ArrayList<>();
            expandir.add(0, no);
            /*
             * Executa enquanto existir nós para expandir
             * Caso encontre solução o ciclo é interrompido
             */
            while (expandir.size() != 0) {
                //Remove da lista de nós a expandir o nó que vai expandir - nó inicial
                no = expandir.remove(0);
                //Verifica se o nó possui um caminho valido para a letra
                if (no.caminhoValido(letra, letra.toLowerCase())) {
                    /*
                     * Caso o caminho seja válido para a letra
                     * Verifica se as restantes letras ainda são possiveis de completar sem cruzar com a solução
                     */
                    if (caminhosLivres(letra.charAt(0), no)){
                        /*
                         * Caso seja possivel, executa recursivamente a função para a letra seguinte
                         */
                        if(letra.compareTo(no.getLastLetra())!=0){
                            int pos = no.getLetras().indexOf(letra.charAt(0));
                            No res = ProcuraProfundidade(no.getLetras().get(pos+1).toString(), no);
                            //Se obteve uma solução retorna o nó da solução encontrada
                            if(res != null){
                                return res;
                            }
                        /*
                         * Caso já tenha executado o algoritmo para todas as letras
                         * Retorna o nó da soluçãp encontrada
                        */
                        } else {
                            expandir.clear();
                            return no;
                        }
                    }
                /*
                 * Caso o nó que encontrou nãp seja a solução
                 * vai expandir o nó
                 */
                } else {
                    //Incrementa contador de expansões
                    this.incrementaExpansao();
                    //Cria os nós sucessores do nó
                    no.gerarSucessores(no, letra);
                    //Incrementa contador de nós gerados
                    incrementaGeracoes(no.getQuantidadeSucessores());
                    /*
                     * Como o algoritmo deve procurar primeiros os nós do mesmo ramo da árvore de procura
                     * Os nós gerados são adicionados no inicio da lista de nós a expandir de modo a serem testados primeiro
                     */
                    expandir.addAll(0,no.getSucessores());
                }
            }
            //Não encontrou solução, retorna valor null
            return null;
        } catch (StackOverflowError erro){
            throw  new StackOverflowError();
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        } catch (NullPointerException erro){
            throw new NullPointerException(erro.toString());
        }
    }

    /**
     * Função do Algoritmo "Procura em Profundidade Limitada
     * @param letra - Letra atual a testar
     * @param no - nó atual a testar
     * @param limite - limite máximo de profundidade
     * @return No com solução | Null caso não encontre solução
     */
    private No ProcuraProfundidadeLimitada(String letra, No no, int limite){
        try {
            //Inicializa o valor do nível do nó a 0
            no.setNivel(0);
            //Coloca a posição do nó no estado inicial
            no.resetPosicao();
            //Cria uma lista auxiliar para os nós a expandir
            ArrayList<No> expandir = new ArrayList<>();
            expandir.add(0, no);
            /*
             * Executa enquanto existir nós para expandir
             * Caso encontre solução o ciclo é interrompido
             */
            while (expandir.size() != 0) {
                //Remove da lista de nós a expandir o nó que vai expandir - nó inicial
                no = expandir.remove(0);
                //Verifica se o nó possui um caminho valido para a letra
                if (no.caminhoValido(letra, letra.toLowerCase())) {
                    /*
                     * Caso o caminho seja válido para a letra
                     * Verifica se as restantes letras ainda são possiveis de completar sem cruzar com a solução
                     */
                    if (caminhosLivres(letra.charAt(0), no)){
                        /*
                         * Caso seja possivel, executa recursivamente a função para a letra seguinte
                         */
                        if(letra.compareTo(no.getLastLetra())!=0){
                            int pos = no.getLetras().indexOf(letra.charAt(0));
                            No res = ProcuraProfundidadeLimitada(no.getLetras().get(pos+1).toString(), no, limite);
                            //Se obteve uma solução retorna o nó da solução encontrada
                            if(res != null){
                                return res;
                            }
                        /*
                         * Caso já tenha executado o algoritmo para todas as letras
                         * Retorna o nó da soluçãp encontrada
                         */
                        } else {
                            expandir.clear();
                            return no;
                        }
                    }
                } else {
                    /*
                     * Caso o nó que encontrou nãp seja a solução
                     * vai expandir o nó, mas apenas expande se ainda não chegou ao limite
                     */
                    if (no.getNivel() < limite) {
                        //Incrementa contador de expansões
                        this.incrementaExpansao();
                        //Cria os nós sucessores do nó
                        no.gerarSucessores(no, letra, no.getNivel()+1);
                        //Incrementa contador de nós gerados
                        incrementaGeracoes(no.getQuantidadeSucessores());
                        /*
                         * Como o algoritmo deve procurar primeiros os nós do mesmo ramo da árvore de procura
                         * Os nós gerados são adicionados no inicio da lista de nós a expandir de modo a serem testados primeiro
                         */
                        expandir.addAll(0, no.getSucessores());
                    }
                }
            }
            //Não encontrou solução, retorna valor null
            return null;
        } catch (StackOverflowError erro){
            throw  new StackOverflowError();
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        } catch (NullPointerException erro){
            throw new NullPointerException(erro.toString());
        }
    }

    /**
     * Getter do atributo solução
     * @return solucao
     */
    public No getSolucao(){
        return this.solucao;
    }

    /**
     * Setter do atributo solução
     * @param novo - nova nó que vai substituir o atual
     */
    public void updateSolucao(No novo){
        this.solucao = novo;
    }

    /**
     * Setter que elimina o valor do atributo solucao
     */
    private void eliminarSolucao(){
        solucao = null;
    }

    /**
     * Getter do atributo contador nós gerados
     * @return valor de nós gerados
     */
    public int getGeracoes(){
        return this.geracoes;
    }

    /**
     * Getter do atributo contador de nós expandidos
     * @return valor de nós expandidos
     */
    public int getExpansoes(){
        return this.expansoes;
    }

    /**
     * Setter do contador do tempo
     * Obtem o tempo atual do sistema operativo e subtrai-lhe o valor atual do atributo
     */
    private void updateTempo(){
        this.tempo = Instant.now().toEpochMilli() - this.tempo;
    }

    /**
     * Getter do atributo tempo em Milisegundos
     * @return valor do tempo em milisegundos
     */
    public long getTempoMs(){
        return this.tempo;
    }

    /**
     * Getter do atributo tempo convertido em Segundos
     * @return valor de tempo convertido em segundos
     */
    public double getTempoS(){
        return this.tempo/1000.0;
    }

    /**
     * Getter do atributo tempo convertido em valor de minuto e percentagem de segundos
     * @return valor de tempo convertido em minutos com percentagem de segundos
     */
    public double getTempoM(){
        return (this.tempo/1000.0)/60;
    }

    /**
     * Função que converte solução numa string para imprimir no ecrã
     * @return Mostra tabuleiro de solução
     */
    public String toString(){
        if(this.solucao!= null)
            return this.solucao.toString();
        else
            return "Sem Solução";
    }

    /**
     * Função que incrementa o contador de nós expandidos
     */
    private void incrementaExpansao(){
        this.expansoes++;
    }

    /**
     * Função que incrementa o contador dos nós gerados
     * @param valor - quantidade a adicionar
     */
    private void incrementaGeracoes(int valor){
        this.geracoes += valor;
    }
}
