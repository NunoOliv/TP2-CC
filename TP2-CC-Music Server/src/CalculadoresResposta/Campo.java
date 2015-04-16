/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Rafael
 */
public class Campo {

    private byte tag;
    private short size;
    private byte[] dados;

    public Campo() {
        this.tag = -1;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo) {
        this.tag = campo;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo, short size, String dados) {
        this.tag = campo;
        this.size = size;
        this.dados = new byte[size];
        System.arraycopy(dados.getBytes(), 0, this.dados, 0, size);
    }

    public Campo(byte campo, short size, byte[] dados) {
        this.tag = campo;
        this.size = size;
        this.dados = new byte[size];
        System.arraycopy(dados, 0, this.dados, 0, size);
    }

    public Campo(byte[] data) {
        this.tag = data[0];
        byte[] aux = new byte[2];
        System.arraycopy(data, 1, aux, 0, 2);
        this.size = byteToShort(aux);
        this.dados = new byte[size];
        System.arraycopy(data, 3, this.dados, 0, size);
    }

    public Campo(Campo c) {
        this.tag = c.getTag();
        this.size = c.getSize();
        this.dados = c.getDados();
    }

    public byte getTag() {
        return tag;
    }

    public void setTag(byte campo) {
        this.tag = campo;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public byte[] getDados() {
        if (this.dados == null) {
            return null;
        }
        byte[] resp = new byte[this.size];
        System.arraycopy(this.dados, 0, resp, 0, this.size);
        return resp;
    }

    public void setDados(byte[] dados, short size) {
        setSize(size);
        this.dados = new byte[size];
        System.arraycopy(dados, 0, this.dados, 0, size);
        this.dados = dados;
    }

    public short getTotalSize() {
        return (short) (this.size + 3);
    }

    public byte[] generate() {
        byte[] resp = new byte[this.size + 3];
        resp[0] = this.tag;
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

    public short byteToShort(byte[] data) {
        byte[] sizeBytes = {data[0], data[1]};
        return ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public String toString() {
        return "Campo{" + "campo=" + tag + ", size=" + size + ", dados=" + dados + '}';
    }

    public Campo clone() {
        return new Campo(this);
    }

    public boolean equals(Object o) {
        if (o instanceof Campo) {
            Campo c = (Campo) o;
            if (this.tag != c.getTag()) {
                return false;
            }
            if (this.size != c.getSize()) {
                return false;
            }
            if (this.dados.equals(c.getDados())) {
                return true;
            }
        }
        return false;
    }

}
