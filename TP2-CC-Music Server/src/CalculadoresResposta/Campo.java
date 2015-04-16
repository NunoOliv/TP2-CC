/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import java.nio.ByteBuffer;

/**
 *
 * @author Rafael
 */
public class Campo {

    private byte campo;
    private short size;
    private byte[] dados;

    public Campo() {
        this.campo = -1;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo, short size, String dados) {
        this.campo = campo;
        this.size = size;
        this.dados = dados.getBytes();
    }

    public Campo(byte campo, short size, byte[] dados) {
        this.campo = campo;
        this.size = size;
        this.dados = dados;
    }

    public byte getCampo() {
        return campo;
    }

    public void setCampo(byte campo) {
        this.campo = campo;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public byte[] getDados() {
        byte[] resp = new byte[this.size];
        System.arraycopy(this.dados, 0, resp, 0, this.size);
        return resp;
    }

    public void setDados(byte[] dados, short size) {
        setSize(size);
        System.arraycopy(dados, 0, this.dados, 0, size);
        this.dados = dados;
    }

    public short getTotalSize() {
        return (short) (this.size + 3);
    }

    public byte[] generate() {
        byte[] resp = new byte[this.size + 3];
        resp[0] = this.campo;
        byte[] buff = shortToByte(this.size);
        resp[1] = buff[0];
        resp[2] = buff[1];
        if (this.dados != null) {
            System.arraycopy(this.dados, 0, resp, 3, this.size);
        }
        return resp;
    }

    public byte[] shortToByte(short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        return bytes;
    }

}
