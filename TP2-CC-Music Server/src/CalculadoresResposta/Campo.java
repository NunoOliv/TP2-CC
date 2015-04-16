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

    private byte campo;
    private short size;
    private byte[] dados;

    public Campo() {
        this.campo = -1;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo) {
        this.campo = campo;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo, short size, String dados) {
        this.campo = campo;
        this.size = size;
        System.arraycopy(dados.getBytes(), 0, this.dados, 0, size);
    }

    public Campo(byte campo, short size, byte[] dados) {
        this.campo = campo;
        this.size = size;
        System.arraycopy(dados, 0, this.dados, 0, size);
    }

    public Campo(byte[] data) {
        this.campo = data[0];
        byte[] aux = new byte[2];
        System.arraycopy(data, 1, aux, 0, 2);
        this.size = byteToShort(aux);
        System.arraycopy(data, 3, this.dados, 0, size);
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

    public short byteToShort(byte[] data) {
        byte[] sizeBytes = {data[0], data[1]};
        return ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    @Override
    public String toString() {
        return "Campo{" + "campo=" + campo + ", size=" + size + ", dados=" + dados + '}';
    }

 
    @Override
    public Campo clone() {
        Campo resp;
        if (size > 0) {
            resp = new Campo(this.campo);
        }else{
            resp = new Campo(this.campo,this.size,this.dados);
        }
        return resp;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Campo){
            Campo c = (Campo) o;
            if(this.campo!=c.getCampo()){
                return false;
            }
            if(this.size!=c.getSize()){
                return false;
            }
            if(this.dados.equals(c.getDados())){
                return true;
            }
        }
        return false;
    }

}
