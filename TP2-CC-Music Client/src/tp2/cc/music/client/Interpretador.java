/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music.client;

import Exception.NotOkException;
import Exception.UnknownTypeException;
import Exception.VersionMissmatchException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Rafael
 */
public class Interpretador {

    public Interpretador() {

    }

    public void checkOK(byte[] data) throws UnknownTypeException, VersionMissmatchException, NotOkException {
        //Versão 0
        if (data[0] != 0) {
            //System.out.println("Versão incorreta. Pacote ignorado.");
            throw new VersionMissmatchException();
        }
        //System.out.println("Versão correta.");

        //Segurança 1
        if (data[1] == 0) {
            //System.out.println("Sem segurança.");
        } else {
            //System.out.println("Com segurança.");
        }

        //Label 2-3
        short label = getLabel(data);
        //System.out.println("Label: " + label);

        //Tipo 4
        if (data[4] != 0) {
            throw new UnknownTypeException();
        }

        //Nº Campos Seguintes 5
        if (data[5] < 1) {
            //System.out.println("Sem campos adicionais");
            throw new NotOkException();
        } else {
            int nCampos = data[5];
            //System.out.println("Numero de campos seguintes: " + nCampos);
        }

        //Tamanho Lista de campos 6-7
        short size = getSize(data);
        //System.out.println("Tamanho dos restantes campos: " + size);

        //Lista de campos Seguintes 8-255
        //255 - 8 = 247
        int j;
        byte[] aux = new byte[247];
        for (j = 0; j < 247; j++) {
            aux[j] = data[j + 8];
        }
        String resp = new String(aux);
        if (resp.equals("OK")) {
            //all is good
        }else{
            throw new NotOkException();
        }
        //System.out.println("Resposta a enviar: " + resp);

    }

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

}
