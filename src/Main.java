import java.util.Scanner;

/**
 * Classe que possui os métodos iniciais do programa
 */
public class Main {
    private Algoritmo dados; //Atributo que guarda os dados de execução do programa

    /**
     * Função para mostrar a solução encontrada (caso encontre)
     * e os dados de execução do programa
     */
    public void mostrarDados(){
        if (dados.getSolucao()!= null && dados.getTempoS() / 60 > 1){
            System.out.println("Foi encontrada a seguinte solução, mas ultrapassou o tempo limite:");
            System.out.println(dados.toString());
        } else if (dados.getSolucao() != null) {
            System.out.println("Solução encontrada: ");
            System.out.println(dados.toString());
        } else {
            System.out.println("Não foi encontrada solução");
        }
        System.out.format("Nós Gerados: %d\n", dados.getGeracoes());
        System.out.format("Nós Expandidos: %d\n", dados.getExpansoes());
        System.out.format("Tempo gasto (milissegundos): %dms\n", dados.getTempoMs());
        System.out.format("Tempo gasto (segundo): %fs\n", dados.getTempoS());
        System.out.format("Tempo gasto (minuto): %fmin\n", dados.getTempoM());
    }

    /**
     * Função que solicita ao utilizador qual o tabuleiro e algoritmo que pretende testar
     */
    public void executar(){
        System.out.print("Selecione um Tabuleiro de 1 a 12.\nTabuleiro: ");
        Scanner op = new Scanner(System.in);
        String tabuleiro = op.nextLine();
        this.dados = new Algoritmo(tabuleiro);
        System.out.println("Escolha o algoritmo:\n\t1-Largura Primeiro\n\t2-Profundidade Primeiro\n\t" +
                "3-Profundidade Limitada\n\t4-Profundidade Iterativa");
        System.out.print("Opção: ");
        int opcao = op.nextInt();
        switch (opcao) {
            case 1:
                dados.executa("Largura");
                break;
            case 2:
                dados.executa("Profundidade");
                break;
            case 3:
                dados.executa("Limitada");
                break;
            case 4:
                dados.executa("Iterativa");
                break;
        }
        mostrarDados();
    }

    /**
     * Função Inicial do Programa
     * Executa enquanto o utilizador indicar que quer continuar
     * @param args - Não utilizado
     */
    public static void main(String[] args){
        Main exec = new Main();
        Scanner ler = new Scanner(System.in);
        int fim = 1;
        while(fim != 0) {
            exec.executar();
            System.out.println("Continuar? [0 - Não] [1 - Sim]");
            fim = ler.nextInt();
            if (fim != 0 && fim != 1)
                fim = 0;
        }
    }
}