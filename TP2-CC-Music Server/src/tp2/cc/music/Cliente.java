/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music;

/**
 *
 * @author Rafael
 */
public class Cliente {

    private String nome;
    private String alcunha;
    private String pass;
    private ;

    public Cliente(String n, String a, String p) {
        nome = n;
        alcunha = a;
        pass = p;
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

}
