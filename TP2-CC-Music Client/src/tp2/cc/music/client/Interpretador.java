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

    public boolean checkLogin(byte[] data) throws VersionMissmatchException, UnknownTypeException, NotOkException {
        if (data[0] != 0) {
            throw new VersionMissmatchException();
        }

        //Segurança 1
        if (data[1] == 0) {
            //System.out.println("Sem segurança.");
        } else {
            //System.out.println("Com segurança.");
        }

        short label = getLabel(data);

        if (data[4] != 0) {
            throw new UnknownTypeException();
        }

        if (data[5] != 1) {
            throw new NotOkException();
        }

        short size = getSize(data);

        if ((data[8] & 0xff) == 255) {
            int j;
            byte[] aux = new byte[size];
            for (j = 0; j < size; j++) {
                aux[j] = data[j + 9];
            }
            String resp = new String(aux);
            System.out.println("Erro: " + resp);
            return false;
        }
        if (data[8] == 0) {
            int j;
            byte[] aux = new byte[size];
            for (j = 0; j < size; j++) {
                aux[j] = data[j + 9];
            }
            String resp = new String(aux);
            System.out.println("Bem vindo " + resp+"!");
            return true;
        }
        return false;
    }

    public boolean checkOK(byte[] data) throws UnknownTypeException, VersionMissmatchException, NotOkException {

        if (data[0] != 0) {
            throw new VersionMissmatchException();
        }

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
        if (data[5] != 1) {
            throw new NotOkException();
        }

        //Tamanho Lista de campos 6-7
        short size = getSize(data);
        //System.out.println("Tamanho dos restantes campos: " + size);

        //Lista de campos Seguintes 8-255
        //255 - 8 = 247
        //System.out.println("data[8]: " + (data[8]& 0xff));
        if ((data[8] & 0xff) == 255) {
            int j;
            byte[] aux = new byte[size];
            for (j = 0; j < size; j++) {
                aux[j] = data[j + 9];
            }
            String resp = new String(aux);
            System.out.println("Erro: " + resp);
            return false;
        }
        if (data[8] == 0) {
            System.out.println("OK!");
            return true;
        }

        return false;
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
