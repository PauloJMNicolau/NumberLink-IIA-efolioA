import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class Algoritmo {
    private No solucao;
    private int expansoes;
    private int geracoes;
    private long tempo;

    public Algoritmo(String tabuleiro){
        String instancia = "instancias/instancia";
        instancia = instancia.concat(tabuleiro);
        instancia = instancia.concat(".txt");
        Mapa mapa = new Mapa(instancia);
        this.solucao = new No(mapa, -1,-1);
        this.expansoes = 0;
        this.geracoes = 1;
        this.tempo = 0;
    }

    public void executa(String algoritmo){
        try {
            this.updateTempo();
            if (algoritmo.compareTo("Largura") == 0) {
                this.updateSolucao(this.ProcuraLargura("A", this.solucao));
                if (this.solucao != null)
                    if (!eSolucao()) {
                        this.eliminarSolucao();
                    }
            } else if (algoritmo.compareTo("Profundidade") == 0) {
                this.updateSolucao(this.ProcuraProfundidade("A", this.solucao));
                if (this.solucao != null)
                    if (!eSolucao()) {
                        this.eliminarSolucao();
                    }
            } else if (algoritmo.compareTo("Limitada") == 0) {
                Scanner op = new Scanner(System.in);
                System.out.print("Indique o Limite: ");
                int limite = op.nextInt();
                this.updateSolucao(this.ProcuraProfundidadeLimitada("A", this.solucao, limite));
                if (this.solucao != null)
                    if (!eSolucao()) {
                        this.eliminarSolucao();
                    }
            } else if (algoritmo.compareTo("Iterativa") == 0) {
                int limite = 0;
                No aux = this.solucao;
                while(limite >= 0) {
                    this.updateSolucao(this.ProcuraProfundidadeLimitada("A", aux, limite));
                    limite++;
                    if (this.solucao != null)
                        if (!eSolucao()) {
                            this.eliminarSolucao();
                        } else{
                            limite = -1;
                        }
                }
            }
            this.updateTempo();
        } catch (StackOverflowError | OutOfMemoryError erro){
            System.out.println("Erro: Falta de Memória");
            eliminarSolucao();
        } catch (NullPointerException erro){
            System.out.println(erro.toString());
            eliminarSolucao();
        }
    }

    private void incrementaExpansao(){
        this.expansoes++;
    }

    private void incrementaGeracoes(int valor){
        this.geracoes += valor;
    }

    private boolean caminhosLivres(char letra, No no) throws NullPointerException{
        ArrayList<Character> aux = new ArrayList<>();
        ArrayList<Character> temp = no.getLetras();
        for(char l : temp) {
            if (l > letra)
                aux.add(l);
        }
        for(Character l: aux){
            boolean valido = no.caminhoValido(l.toString(), ".");
            if(!valido){
                return false;
            }
        }
        return true;
    }

    private No ProcuraLargura(String letra, No no){
        try {
            no.resetPosicao();
            ArrayList<No> expandir = new ArrayList<>();
            expandir.add(no);
            while (expandir.size() != 0) {
                no = expandir.remove(0);
                if (no.caminhoValido(letra, letra.toLowerCase())) {
                    if (caminhosLivres(letra.charAt(0), no)){
                        if(letra.compareTo(no.getLastLetra())!=0){
                            int pos = no.getLetras().indexOf(letra.charAt(0));
                            No res = ProcuraLargura(no.getLetras().get(pos+1).toString(), no);
                            if (res != null)
                                return res;
                        } else {
                            expandir.clear();
                            return no;
                        }
                    }
                } else {
                    this.incrementaExpansao();
                    no.gerarSucessores(no, letra);
                    incrementaGeracoes(no.getQuantidadeSucessores());
                    expandir.addAll(no.getSucessores());
                }
            }
            return null;
        } catch (StackOverflowError erro){
            throw  new StackOverflowError();
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        } catch (NullPointerException erro) {
            throw new NullPointerException(erro.toString());
        }
    }

    private No ProcuraProfundidade(String letra, No no){
        try {
            no.resetPosicao();
            ArrayList<No> expandir = new ArrayList<>();
            expandir.add(0, no);
            while (expandir.size() != 0) {
                no = expandir.remove(0);
                if (no.caminhoValido(letra, letra.toLowerCase())) {
                    if (caminhosLivres(letra.charAt(0), no)){
                        if(letra.compareTo(no.getLastLetra())!=0){
                            int pos = no.getLetras().indexOf(letra.charAt(0));
                            No res = ProcuraProfundidade(no.getLetras().get(pos+1).toString(), no);
                            if(res != null){
                                return res;
                            }
                        } else {
                            expandir.clear();
                            return no;
                        }
                    }
                } else {
                    this.incrementaExpansao();
                    no.gerarSucessores(no, letra);
                    incrementaGeracoes(no.getQuantidadeSucessores());
                    expandir.addAll(0,no.getSucessores());
                }
            }
            return null;
        } catch (StackOverflowError erro){
            throw  new StackOverflowError();
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        } catch (NullPointerException erro){
            throw new NullPointerException(erro.toString());
        }
    }

    private No ProcuraProfundidadeLimitada(String letra, No no, int limite){
        try {
            no.setNivel(0);
            no.resetPosicao();
            ArrayList<No> expandir = new ArrayList<>();
            expandir.add(0, no);
            while (expandir.size() != 0) {
                no = expandir.remove(0);
                if (no.caminhoValido(letra, letra.toLowerCase())) {
                    if (caminhosLivres(letra.charAt(0), no)){
                        if(letra.compareTo(no.getLastLetra())!=0){
                            int pos = no.getLetras().indexOf(letra.charAt(0));
                            No res = ProcuraProfundidadeLimitada(no.getLetras().get(pos+1).toString(), no, limite);
                            if(res != null){
                                return res;
                            }
                        } else {
                            expandir.clear();
                            return no;
                        }
                    }
                } else {
                    if (no.getNivel() < limite) {
                        this.incrementaExpansao();
                        no.gerarSucessores(no, letra, no.getNivel()+1);
                        incrementaGeracoes(no.getQuantidadeSucessores());
                        expandir.addAll(0, no.getSucessores());
                    }
                }
            }
            return null;
        } catch (StackOverflowError erro){
            throw  new StackOverflowError();
        } catch (OutOfMemoryError erro){
            throw new OutOfMemoryError();
        } catch (NullPointerException erro){
            throw new NullPointerException(erro.toString());
        }
    }


    public No getSolucao(){
        return this.solucao;
    }

    public void updateSolucao(No novo){
        this.solucao = novo;
    }

    private void updateTempo(){
        this.tempo = Instant.now().toEpochMilli() - this.tempo;
    }

    public int getGeracoes(){
        return this.geracoes;
    }

    public int getExpansoes(){
        return this.expansoes;
    }

    public long getTempoMs(){
        return this.tempo;
    }
    public double getTempoM(){
        return (this.tempo/1000.0)/60;
    }

    public double getTempoS(){
        return this.tempo/1000.0;
    }

    private void eliminarSolucao(){
        solucao = null;
    }

    private boolean eSolucao(){
        boolean valido;
        for(Character letra : this.solucao.getLetras()){
            valido = solucao.caminhoValido(letra.toString(), letra.toString().toLowerCase());
            if(!valido){
                return false;
            }
        }
        return true;
    }


    public String toString(){
        if(this.solucao!= null)
            return this.solucao.toString();
        else
            return "Sem Solução";
    }
}
