package tp2.cc.music.client;

import java.util.ArrayList;

public class Desafio {

    private ArrayList<Pergunta> perguntas;

    public Desafio() {
        perguntas = new ArrayList<>();
    }

    public Desafio(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public Desafio(int nMaxPerguntas) {
        if (nMaxPerguntas < 1) {
            perguntas = new ArrayList<>();
        } else {
            perguntas = new ArrayList<>(nMaxPerguntas);
        }
    }

    public boolean addPergunta(Pergunta p) {
        if (p != null) {
            return false;
        }
        this.perguntas.add(p);
        return true;
    }

    public ArrayList<Pergunta> getPerguntas() {
        return perguntas;
    }

    public int getNPerguntas() {
        return perguntas.size();
    }

}
