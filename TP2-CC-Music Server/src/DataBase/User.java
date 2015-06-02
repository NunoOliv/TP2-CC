/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.net.InetAddress;

/**
 *
 * @author Rafael
 */
public class User {

    private String nome;
    private String alcunha;
    private String pass;
    private InetAddress enderecoIP;
    private int port;
    private int pontuacao;
    private short nMensagensRecebidas;
    private short nMensagensEnviadas;
    private boolean sessaoAtiva;
    //private short label=0;
    //private ;

    public User(String n, String a, String p) {
        this.nome = n;
        this.alcunha = a;
        this.pass = p;
        this.pontuacao = 0;
        this.nMensagensRecebidas = 0;
        this.nMensagensEnviadas = 1;
        this.sessaoAtiva = false;
    }

    public User(String alcunha, String pass, String nome, InetAddress ip, int port) {
        this.nome = nome;
        this.alcunha = alcunha;
        this.pass = pass;
        this.enderecoIP = ip;
        this.port = port;
        this.pontuacao = 0;
        this.nMensagensRecebidas = 0;
        this.nMensagensEnviadas = 1;
        this.sessaoAtiva = false;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isSessaoAtiva() {
        return sessaoAtiva;
    }

    public void setSessaoAtiva(boolean sessaoAtiva) {
        this.sessaoAtiva = sessaoAtiva;
    }

    /**
     * @return the alcunha
     */
    public String getAlcunha() {
        return alcunha;
    }

    /**
     * @param alcunha the alcunha to set
     */
    public void setAlcunha(String alcunha) {
        this.alcunha = alcunha;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the enderecoIP
     */
    public InetAddress getEnderecoIP() {
        return enderecoIP;
    }

    /**
     * @param enderecoIP the enderecoIP to set
     */
    public void setEnderecoIP(InetAddress enderecoIP) {
        this.enderecoIP = enderecoIP;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the pontuacao
     */
    public int getPontuacao() {
        return pontuacao;
    }

    /**
     * @param pontuacao the pontuacao to set
     */
    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public short getnMensagensRecebidas() {
        return nMensagensRecebidas;
    }

    public void setnMensagensRecebidas(short nMensagensRecebidas) {
        this.nMensagensRecebidas = nMensagensRecebidas;
    }

    public short getnMensagensEnviadas() {
        return nMensagensEnviadas;
    }

    public void setnMensagensEnviadas(short nMensagensEnviadas) {
        this.nMensagensEnviadas = nMensagensEnviadas;
    }

    public void incrementaMensagensEnviadas() {
        this.nMensagensEnviadas++;
    }

    public void incrementaMensagensRecebidas() {
        this.nMensagensRecebidas++;
    }

    public boolean equals(User u) {
        return this.alcunha.equals(u.getAlcunha());
    }

}
