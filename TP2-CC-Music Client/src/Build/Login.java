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
public class Login {

    byte[] pdu;
    byte[] alcunha;
    byte[] pass;
    short label;

    public Login(String alcunha, String pass, short label) {
        pdu = new byte[255];
        this.alcunha = alcunha.getBytes();
        this.pass = pass.getBytes();
        this.label = label;
    }

    public byte[] generate() {
        pdu[0] = 0;//versao
        pdu[1] = 0;//segurança
        setLabel(pdu, label);
        pdu[4] = 3;//tipo=LOGIN;
        pdu[5] = 2;//nCampos
        setSize(pdu, (short) 150);

        int i;
        //meter alcunha
        for (i = 8; (i < 8 + 75 && i < alcunha.length + 8); i++) {
            pdu[i] = alcunha[i - 8];
        }
        //meter pass
        for (i = 8 + 75; (i < 8 + 75 + 75 && i < pass.length + 8 + 75); i++) {
            pdu[i] = pass[i - 8 - 75];
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
