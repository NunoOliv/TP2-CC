package DataBase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Desafio {

    private String nome;
    private ArrayList<Pergunta> perguntas;
    private GregorianCalendar hora;
    private TreeMap<String, User> jogadores;

    public Desafio() {
        perguntas = new ArrayList<>();
        hora = new GregorianCalendar();
        jogadores = new TreeMap<>();
    }

    public Desafio(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;
        this.jogadores = new TreeMap<>();
        hora = new GregorianCalendar();
    }

    public Desafio(int nMaxPerguntas) {
        if (nMaxPerguntas < 1) {
            perguntas = new ArrayList<>();
        } else {
            perguntas = new ArrayList<>(nMaxPerguntas);
        }
        this.jogadores = new TreeMap<>();
        hora = new GregorianCalendar();
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public GregorianCalendar getHora() {
        return hora;
    }

    public void setHora(GregorianCalendar hora) {
        this.hora = hora;
    }

    public void addJogador(User u) {
        jogadores.put(u.getAlcunha(), u);
    }

    public HashSet<User> getJogadores() {
        HashSet<User> resp = new HashSet<>();
        Iterator aux = jogadores.values().iterator();
        while (aux.hasNext()) {
            resp.add((User) aux.next());
        }
        return resp;
    }

    public Set<String> getAlcunhas() {
        return jogadores.keySet();
    }

    public User getJogador(String alcunha) {
        if (!jogadores.containsKey(alcunha)) {
            return null;
        }
        return jogadores.get(alcunha);
    }

    public int getNjogadores() {
        return jogadores.size();
    }

    public int getNPerguntas() {
        return perguntas.size();
    }

}
