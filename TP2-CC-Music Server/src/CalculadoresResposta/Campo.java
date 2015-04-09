/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

/**
 *
 * @author Rafael
 */
public class Campo {
    private byte campo;
    private byte size;
    private String dados;

    public Campo(byte campo, byte size, String dados) {
        this.campo = campo;
        this.size = size;
        this.dados = dados;
    }

    public byte getCampo() {
        return campo;
    }

    public byte getSize() {
        return size;
    }

    public String getDados() {
        return dados;
    }

    public void setCampo(byte campo) {
        this.campo = campo;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }
    
    
    
}
