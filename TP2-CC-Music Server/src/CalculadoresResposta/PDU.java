package CalculadoresResposta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class PDU {

    //private byte[] pdu;
    private byte versao;
    private byte seguranca;
    private short label;
    private byte tipo;
    private byte nCampos;
    private int tamanho;

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
        //copia conteudo do pdu desde a pos.8 adiante
        System.arraycopy(pdu, 10, this.lista, 0, this.tamanho);
    }

    public PDU(PDU pdu) {
        this.versao = pdu.getVersao();
        this.seguranca = pdu.getSeguranca();
        this.label = pdu.getLabel();
        this.tipo = pdu.getTipo();
        this.nCampos = pdu.getnCampos();
        this.tamanho = pdu.getTamanho();
        this.lista = pdu.getLista();
    }

    public PDU(byte versao, byte seguranca, short label, byte tipo, byte nCampos, int tamanho, byte[] lista) {
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
        byte[] res = new byte[10 + getTamanho()];
        res[0] = getVersao();
        res[1] = getSeguranca();

        setLabel(res, getLabel());

        res[4] = getTipo();
        res[5] = getnCampos();

        setSize(res, getTamanho());
        if (tamanho > 0) {
            System.arraycopy(this.lista, 0, res, 10, getTamanho());
        }
        return res;
    }

    private short getLabel(byte[] data) {
        byte[] labelBytes = {data[2], data[3]};
        return ByteBuffer.wrap(labelBytes).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public void setLabel(byte[] data, short label) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(label).array();
        data[2] = bytes[0];
        data[3] = bytes[1];
    }

    private int getSize(byte[] data) {
        byte[] sizeBytes = {data[6], data[7], data[8], data[9]};
        return ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public void setSize(byte[] data, int size) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(size).array();
        data[6] = bytes[0];
        data[7] = bytes[1];
        data[8] = bytes[2];
        data[9] = bytes[3];
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

    public int getTamanho() {
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

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public void setLista(byte[] a) {
        this.lista = a;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }
        PDU e = (PDU) obj;
        boolean cond;
        cond = (this.seguranca == e.getSeguranca())
                && (this.versao == e.getVersao())
                && (this.label == e.getLabel())
                && (this.tipo == e.getTipo())
                && (this.nCampos == e.getnCampos())
                && (this.tamanho == e.getTamanho())
                && (Arrays.equals(this.lista, e.getLista()));
        return cond;
    }

    @Override
    public PDU clone() {
        return new PDU(this); //To change body of generated methods, choose Tools | Templates.
    }

    /*deprecated? -> use system.arraycopy*/
    /*private byte[] pduToLista(byte[] pdu) {
     int i;
     int n = getSize(pdu);
     byte[] resp = new byte[n];
     for (i = 0; i < n; i++) {
     resp[i] = pdu[i + 8];
     }
     return resp;
     }*/

    /*deprecated? -> use system.arraycopy*/
    public void listaToPDU(byte[] data) {
        int i;
        for (i = 0; i < 247; i++) {
            data[i + 8] = lista[i];
        }
    }

}
