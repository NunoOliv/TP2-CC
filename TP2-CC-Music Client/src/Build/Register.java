/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Build;

import java.nio.ByteBuffer;

/**
 *
 * @author Rafael
 */
public class Register {

    byte[] pdu;
    byte[] nome;
    byte[] alcunha;
    byte[] pass;
    short label;

    public Register(String alcunha, String pass, String nome, short label) {
        pdu = new byte[255];
        this.alcunha = alcunha.getBytes();
        this.pass = pass.getBytes();
        this.nome = nome.getBytes();
        this.label = label;
    }

    public byte[] generate() {
        pdu[0] = 0;//versao
        pdu[1] = 0;//segurança
        setLabel(pdu, label);
        pdu[4] = 2;//tipo=REGISTER;
        pdu[5] = 3;//nCampos
        setSize(pdu, (short) 75);

        int i;
        //meter alcunha
        for (i = 8; (i < 8 + 75 && i < alcunha.length + 8); i++) {
            pdu[i] = alcunha[i - 8];
        }
        //meter pass
        for (i = 8 + 75; (i < 8 + 75 + 75 && i < pass.length + 8 + 75); i++) {
            pdu[i] = pass[i - 8 - 75];
        }
        //meter nome
        for (i = 8 + 75 + 75; (i < 8 + 75 + 75 && i < nome.length + 8 + 75 + 75); i++) {
            pdu[i] = nome[i - 8 - 75 - 75];
        }

        return pdu;
    }

    public void setLabel(byte[] data, short label) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(label).array();
        data[2] = bytes[0];
        data[3] = bytes[1];
    }

    public void setSize(byte[] data, short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        data[6] = bytes[0];
        data[7] = bytes[1];
    }
}
