import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mapa {
    private ArrayList<Casa> casas;
    private int N;
    private int M;
    private int K;
    private ArrayList<Character> letras;

    public Mapa(String nome){
        try{
            File ficheiro = new File(nome);
            Scanner leitor = new Scanner(ficheiro);
            this.casas = new ArrayList<>();
            this.letras = new ArrayList<>();
            int pos = 0;
            int posMapa = 0;
            while(leitor.hasNextLine()){
                String linha = leitor.nextLine();
                if(pos == 0){
                    String[] dados = linha.split(" ", 0);
                    this.N = Integer.parseInt(dados[0]);
                    this.M = Integer.parseInt(dados[1]);
                    this.K = Integer.parseInt(dados[2]);
                    char letra = 'A';
                    while(this.letras.size()< this.K) {
                        this.letras.add(letra);
                        letra++;
                    }
                } else if (pos == 1){
                    pos++;
                    continue;
                } else{
                    String[] dados = linha.split(" ", 0);
                    for (String valor: dados) {
                        casas.add(new Casa(valor, posMapa++));
                    }
                }
                pos++;
            }
            leitor.close();
            adicionarVizinhos();
        } catch (FileNotFoundException erro){
            System.out.println("Erro: Ficheiro não encontrado!");
            System.exit(1);
        }
    }

    public Mapa(Mapa mapa){
        this.casas = mapa.getCasas();
        this.K = mapa.getK();
        this.N = mapa.getN();
        this.M = mapa.getM();
        this.letras = mapa.getLetras();
    }

    private void adicionarVizinhos(){
        for(int i = 0; i < casas.size(); i++) {
            if (i == 0) {
                casas.get(i).addVizinho(i+1);
                casas.get(i).addVizinho(i + this.M);
            } else if (i == this.M - 1) {
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + this.M);
            } else if (i == this.casas.size() - this.M) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i + 1);
            } else if (i == this.casas.size() - 1) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
            } else if (i <= this.M - 2) {
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + 1);
                casas.get(i).addVizinho(i + this.M);
            } else if (i > this.casas.size() - this.M && i < this.casas.size() - 1) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + 1);
            } else if ((i != this.casas.size() - this.M) && (i % this.M == 0)) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i + 1);
                casas.get(i).addVizinho(i + this.M);
            } else if ((i != this.M) && (i != this.casas.size() - 1) && (i % this.M == this.M - 1)) {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + this.M);
            } else {
                casas.get(i).addVizinho(i - this.M);
                casas.get(i).addVizinho(i - 1);
                casas.get(i).addVizinho(i + 1);
                casas.get(i).addVizinho(i + this.M);
            }
        }
    }

    public Casa getCasa(int posicao){
        return this.casas.get(posicao);
    }

    public Casa getCasa(String letra){
        for(Casa casa : this.casas){
            if(casa.getElemento().compareTo(letra)==0)
                return new Casa(casa);
        }
        return null;
    }

    public Casa getCasa(String letra, int posicao){
        for (int i = posicao+1; i < this.casas.size(); i++){
            Casa casa = getCasa(i);
            if(casa.getElemento().compareTo(letra)==0)
                return new Casa(casa);
        }
        return null;
    }

    public void setCasa(int posicao, String elemento){
        this.casas.get(posicao).setElemento(elemento);
    }

    public ArrayList<Casa> getCasas(){
        ArrayList<Casa> temp = new ArrayList<>();
        for (Casa casa : this.casas){
            temp.add(new Casa(casa));
        }
        return temp;
    }

    public String getElementoCasa(int posicao){
        return casas.get(posicao).getElemento();
    }

    public ArrayList<Character> getLetras(){
        return new ArrayList<>(this.letras);
    }

    public int getK(){
        return this.K;
    }

    public int getM(){
        return this.M;
    }

    public int getN(){
        return this.N;
    }

    public Mapa getCopiaMapa(){
        return new Mapa(this);
    }


    public boolean caminhoValido(String letra, String elemento){
        try {
            Casa casaOrigem = getCasa(letra);
            Casa casaDestino = getCasa(letra, casaOrigem.getPosicao());
            ArrayList<Integer> vizinhos = casaOrigem.getVizinhos();
            if (vizinhos.contains(casaDestino.getPosicao())) {
                return true;
            } else {
                ArrayList<Integer> visitados = new ArrayList<>();
                visitados.add(casaOrigem.getPosicao());
                return percorrer(letra, elemento, casaDestino.getPosicao(), vizinhos, visitados);
            }
        } catch (NullPointerException erro){
            throw new NullPointerException("Erro: Sequencia de Letras Inválida");
        }
    }

    private boolean percorrer(String letra, String elemento, int posDestino, ArrayList<Integer> vizinhos, ArrayList<Integer> visitados){
        while(vizinhos.size()!= 0){
            int posicao = vizinhos.remove(0);
            visitados.add(posicao);
            Casa casa = getCasa(posicao);
            if(casa.getElemento().compareTo(elemento)==0) {
                ArrayList<Integer> temp = casa.getVizinhos();
                if (temp.contains(posDestino)) {
                    return true;
                } else {
                    for(int i: visitados){
                        if(temp.contains(i)){
                            temp.remove((Integer)i);
                        }
                    }
                    vizinhos.addAll(0, temp);
                }
            }
        }
        return false;
    }

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

    public String getLastLetra(){
        return this.letras.get(letras.size()-1).toString();
    }
}
