/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
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
    private short tamanho;/* max size = 2^16 - 1 = 65535 (~64 kBytes)*/
    private byte[] lista; /* tamanho variável!*/
    //private HashMap<Byte, Campo> campos;

    public PDU(byte[] pdu) {
        versao = pdu[0];
        seguranca = pdu[1];
        label = getLabel(pdu);
        tipo = pdu[4];
        nCampos = pdu[5];
        tamanho = getSize(pdu);
        lista = new byte[this.tamanho];
        /*copia conteudo do pdu desde a pos.8 adiante*/
        System.arraycopy(pdu, 8, this.lista, 0, this.tamanho);
    }
    
    public PDU(PDU pdu){
        this.versao = pdu.getVersao();
        this.seguranca = pdu.getSeguranca();
        this.label = pdu.getLabel();
        this.tipo = pdu.getTipo();
        this.nCampos = pdu.getnCampos();
        this.tamanho = pdu.getTamanho();
        this.lista = pdu.getLista();
    }

    public PDU(byte versao, byte seguranca, short label, byte tipo, byte nCampos, short tamanho, byte[] lista) {
        this.versao = versao;
        this.seguranca = seguranca;
        this.label = label;
        this.tipo = tipo;
        this.nCampos = nCampos;
        this.tamanho = tamanho;
        this.lista = new byte[tamanho];
        System.arraycopy(lista, 0, this.lista, 0, tamanho);
    }

    public PDU() {
        this.versao = 0;
        this.seguranca = 0;
        this.label = 0;
        this.tipo = 0;
        this.nCampos = 0;
        this.tamanho = 0;
        this.lista = null;/*não é conhecido o tamanho*/
    }
    


    public byte[] generatePDU() {
         byte[] res = new byte[8+getTamanho()];
         res[0] = getVersao();
         res[1] = getSeguranca();
         
         /*conversao da label em byte[]*/
         ByteBuffer resLabel = ByteBuffer.allocate(2);
         resLabel.putShort(getLabel());
         System.arraycopy(resLabel.array(), 0, res, 2, 2);
         
         res[4] = getTipo();
         res[5] = getnCampos();
         
         /*conversao do tamanho em byte[]*/    
         ByteBuffer resTamanho = ByteBuffer.allocate(2);
         resTamanho.putShort(getTamanho());
         System.arraycopy(resTamanho.array(), 0, res, 6, 2);
         
         /*conversao dos bytes da lista*/
         System.arraycopy(this.lista, 0, res, 8, getTamanho());
         
         return res;
    }

    private short getLabel(byte[] data) {
        byte[] labelBytes = {data[2], data[3]};
        short label = ByteBuffer.wrap(labelBytes).order(ByteOrder.BIG_ENDIAN).getShort();
        return label;
    }

    public void setLabel(byte[] data, short label) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(label).array();
        data[2] = bytes[0];
        data[3] = bytes[1];
    }

    private short getSize(byte[] data) {
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
        byte[] l = new byte[this.tamanho];
        System.arraycopy(this.lista, 0, l, 0, tamanho);
        return l;
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

    public void setLista(byte[] list, int size) {
        this.lista = new byte[size];
        System.arraycopy(list, 0, this.lista, 0, size);
    }

    /*empty list*/
    public void setLista(int size) {
        this.lista = new byte[size];
    }

    @Override
    public String toString() {
        return "PDU{" + "versao=" + versao + ", seguranca=" + seguranca + ", "
                + "label=" + label + ", tipo=" + tipo + ", nCampos=" + nCampos 
                + ", tamanho=" + tamanho + ", lista=" + Arrays.toString(lista) 
                + '}';
    }

    @Override
    public boolean equals (Object obj){
        if(this == obj) return true; 
        if((obj == null) || (this.getClass() != obj.getClass())) return false;
        PDU e = (PDU) obj;
        boolean cond; 
        cond = (this.seguranca == e.getSeguranca())&& 
               (this.versao == e.getVersao())&&
               (this.label == e.getLabel())&&
               (this.tipo == e.getTipo())&&
               (this.nCampos == e.getnCampos())&&
               (this.tamanho == e.getTamanho())&&
               (Arrays.equals(this.lista, e.getLista()));
        return cond;
    }
    
    

    @Override
    public PDU clone() {
        return new PDU(this); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
   

    /*deprecated? -> use system.arraycopy*/
    private byte[] pduToLista(byte[] pdu) {
        int i;
        int n = getSize(pdu);
        byte[] resp = new byte[n];
        for (i = 0; i < n; i++) {
            resp[i] = pdu[i + 8];
        }
        return resp;
    }
   
    /*deprecated? -> use system.arraycopy*/
    public void listaToPDU(byte[] data) {
        int i;
        for (i = 0; i < 247; i++) {
            data[i + 8] = lista[i];
        }
    }
    
    /*deprecated? -> use generatePDU()*/
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
