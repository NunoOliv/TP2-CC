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
public class Cliente {

    public String nome;
    public String alcunha;
    public String pass;
    public InetAddress enderecoIP;
    public int port;
    public int pontuacao;
    public short nMensagensRecebidas;
    public short nMensagensEnviadas;
    //private ;

    public Cliente(String n, String a, String p) {
        nome = n;
        alcunha = a;
        pass = p;
        pontuacao=0;
        nMensagensRecebidas=0;
        nMensagensEnviadas=0;
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



}
