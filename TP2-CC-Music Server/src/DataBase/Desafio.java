package DataBase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Desafio {

    private String nome;
    private String data, hora;
    private int ano, mes, dia, hor, min, seg;
    private ArrayList<Pergunta> perguntas;

    private TreeMap<String, User> jogadores;

    public Desafio() {
        perguntas = new ArrayList<>();
        jogadores = new TreeMap<>();
        this.nome = "";
        this.data = "";
        this.hora = "";
        this.ano = 0;
        this.mes = 0;
        this.dia = 0;
        this.hor = 0;
        this.min = 0;
        this.seg = 0;
    }

    public Desafio(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;
        this.jogadores = new TreeMap<>();
        this.nome = "";
        this.data = "";
        this.hora = "";
        this.ano = 0;
        this.mes = 0;
        this.dia = 0;
        this.hor = 0;
        this.min = 0;
        this.seg = 0;
    }

    public Desafio(int nMaxPerguntas) {
        if (nMaxPerguntas < 1) {
            perguntas = new ArrayList<>();
        } else {
            perguntas = new ArrayList<>(nMaxPerguntas);
        }
        this.jogadores = new TreeMap<>();
        this.nome = "";
        this.data = "";
        this.hora = "";
        this.ano = 0;
        this.mes = 0;
        this.dia = 0;
        this.hor = 0;
        this.min = 0;
        this.seg = 0;
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

    public String getData() {
        return data;
    }

    public void setData(String data) throws Exception {
        if (data.length() != 6) {
            throw new Exception("Data inválida!");
        }
        try {
            Integer.parseInt(data);
        } catch (Exception e) {
            throw new Exception("Data inválida!");
        }
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) throws Exception {
        if (hora.length() != 6) {
            throw new Exception("Hora inválida!");
        }
        try {
            Integer.parseInt(hora);
        } catch (Exception e) {
            throw new Exception("Hora inválida!");
        }
        this.hora = hora;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) throws Exception {
        if (ano < 2015) {
            throw new Exception("Ano inválido!");
        }
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) throws Exception {
        if (mes < 0 || mes > 12) {
            throw new Exception("Mês inválido!");
        }
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) throws Exception {
        if (dia < 0 || dia > 31) {
            throw new Exception("Dia inválido!");
        }
        this.dia = dia;
    }

    public int getHor() {
        return hor;
    }

    public void setHor(int hor) throws Exception {
        if (hor < 0 || hor > 24) {
            throw new Exception("Hora inválida!");
        }
        this.hor = hor;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) throws Exception {
        if (min < 0 || min > 60) {
            throw new Exception("Minuto inválido!");
        }
        this.min = min;
    }

    public int getSeg() {
        return seg;
    }

    public void setSeg(int seg) throws Exception {
        if (seg < 0 || seg > 60) {
            throw new Exception("Segundo inválido!");
        }
        this.seg = seg;
    }

    /**
     * data de String Format para Integer Format (vários inteiros)
     *
     * @return
     * @throws java.lang.Exception
     */
    public boolean dateSFtoIF() throws Exception {
        if (this.data.equals("") || this.hora.equals("")) {
            throw new Exception("Data ou Hora não definida no formato String!");
        }

        ano = 2000 + Integer.parseInt(data.substring(0, 2));
        mes = Integer.parseInt(data.substring(2, 4));
        dia = Integer.parseInt(data.substring(4));
        if (ano < 2015 || mes < 0 || dia < 0 || mes > 12 || dia > 31) {
            this.ano = 0;
            this.mes = 0;
            this.dia = 0;
            throw new Exception("Data inválida!");
        }

        hor = Integer.parseInt(hora.substring(0, 2));
        min = Integer.parseInt(hora.substring(2, 4));
        seg = Integer.parseInt(hora.substring(4));
        if (hor < 0 || hor > 24 || min < 0 || seg < 0 || min > 60 || seg > 60) {
            this.hor = 0;
            this.min = 0;
            this.seg = 0;
            throw new Exception("Hora inválida!");
        }

        return true;
    }

    public boolean dateIFtoSF() throws Exception {
        if (ano < 2015) {
            throw new Exception("Ano inválido!");
        }
        if (mes < 0 || mes > 12) {
            throw new Exception("Mês inválido!");
        }
        if (dia < 0 || dia > 31) {
            throw new Exception("Dia inválido!");
        }
        if (hor < 0 || hor > 24) {
            throw new Exception("Hora inválida!");
        }
        if (min < 0 || min > 60) {
            throw new Exception("Minuto inválido!");
        }
        if (seg < 0 || seg > 60) {
            throw new Exception("Segundo inválido!");
        }

        data = "";
        hora = "";

        if (ano < 10) {
            data = "0";
            data = data.concat(Integer.toString(ano));
        } else {
            data = Integer.toString(ano);
        }
        if (mes < 10) {
            data = data.concat("0");
        }
        data = data.concat(Integer.toString(mes));
        if (dia < 10) {
            data = data.concat("0");
        }

        if (hor < 10) {
            hora = "0";
            hora = hora.concat(Integer.toString(hor));
        } else {
            hora = Integer.toString(hor);
        }
        if (min < 10) {
            hora = hora.concat("0");
        }
        hora = hora.concat(Integer.toString(min));
        if (seg < 10) {
            hora = hora.concat("0");
        }
        hora = hora.concat(Integer.toString(seg));

        return true;
    }

}
