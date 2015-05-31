package tp2.cc.music.client;

import java.util.ArrayList;

public class Desafio {

    private String nome;
    private String data, hora;
    private ArrayList<Pergunta> perguntas;

    public Desafio() {
        this.perguntas = new ArrayList<>();
        this.nome = "";
        this.data = "";
        this.hora = "";
    }

    public Desafio(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;
        this.nome = "";
        this.data = "";
        this.hora = "";
    }

    public Desafio(int nMaxPerguntas) {
        if (nMaxPerguntas < 1) {
            perguntas = new ArrayList<>();
        } else {
            perguntas = new ArrayList<>(nMaxPerguntas);
        }
        this.nome = "";
        this.data = "";
        this.hora = "";
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

}
