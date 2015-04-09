/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

/**
 *
 * @author Rafael
 */
public class PDU {

    //private byte[] pdu;
    private byte versao;
    private byte seguranca;
    private short label;
    private byte tipo;
    private byte nCampos;
    private short tamanho;
    private byte[] lista;
    //private HashMap<Byte, Campo> campos;

    public PDU(byte[] pdu) {
        versao = pdu[0];
        seguranca = pdu[1];
        label = getLabel(pdu);
        tipo = pdu[4];
        nCampos = pdu[5];
        tamanho = getSize(pdu);
        lista = pduToLista(pdu);

    }

    public PDU(byte versao, byte seguranca, short label, byte tipo, byte nCampos, short tamanho, byte[] lista) {
        this.versao = versao;
        this.seguranca = seguranca;
        this.label = label;
        this.tipo = tipo;
        this.nCampos = nCampos;
        this.tamanho = tamanho;
        this.lista = lista;
        //this.campos = campos;
    }

    public PDU() {
        this.versao = 0;
        this.seguranca = 0;
        this.label = 0;
        this.tipo = 0;
        this.nCampos = 0;
        this.tamanho = 0;
        this.lista = new byte[247];
        //this.campos = new HashMap<>();
    }

    public byte[] generatePDU() {
        return new byte[255];
    }

    String s = new String(lista);

    public short getLabel(byte[] data) {
        byte[] labelBytes = {data[2], data[3]};
        short label = ByteBuffer.wrap(labelBytes).order(ByteOrder.BIG_ENDIAN).getShort();
        return label;
    }

    public void setLabel(byte[] data, short label) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(label).array();
        data[2] = bytes[0];
        data[3] = bytes[1];
    }

    public short getSize(byte[] data) {
        byte[] sizeBytes = {data[6], data[7]};
        short size = ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getShort();
        return size;
    }

    public void setSize(byte[] data, short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        data[6] = bytes[0];
        data[7] = bytes[1];
    }

    public byte getVersao() {
        return versao;
    }

    public byte getSeguranca() {
        return seguranca;
    }

    public short getLabel() {
        return label;
    }

    public byte getTipo() {
        return tipo;
    }

    public byte getnCampos() {
        return nCampos;
    }

    public short getTamanho() {
        return tamanho;
    }

    public byte[] getLista() {
        return lista;
    }

    
    public String getS() {
        return s;
    }

    public void setVersao(byte versao) {
        this.versao = versao;
    }

    public void setSeguranca(byte seguranca) {
        this.seguranca = seguranca;
    }

    public void setLabel(short label) {
        this.label = label;
    }

    public void setTipo(byte tipo) {
        this.tipo = tipo;
    }

    public void setnCampos(byte nCampos) {
        this.nCampos = nCampos;
    }

    public void setTamanho(short tamanho) {
        this.tamanho = tamanho;
    }

    public void setLista(byte[] lista) {
        this.lista = lista;
    }

    public void setLista() {
        this.lista = new byte[247];
    }

   
    public void setS(String s) {
        this.s = s;
    }

    public byte[] pduToLista(byte[] pdu) {
        int i;
        byte[] resp = new byte[247];
        for (i = 0; i < 247; i++) {
            resp[i] = pdu[i + 8];
        }
        return resp;
    }

    public void listaToPDU(byte[] data) {
        int i;
        for (i = 0; i < 247; i++) {
            data[i + 8] = lista[i];
        }
    }

    public byte[] getResposta() {
        byte[] resposta = new byte[255];
        int i;

        resposta[0] = versao;//versao
        resposta[1] = seguranca;//segurança
        setLabel(resposta, label);// label
        resposta[4] = tipo;// tipo=REPLY
        resposta[5] = nCampos;// campos seguintes
        setSize(resposta, tamanho);
        listaToPDU(resposta);
                
        return resposta;
    }

}
