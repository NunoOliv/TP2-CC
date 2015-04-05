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
public class CalcResp {

    public byte[] pdu;
    public short label;

    public CalcResp(byte[] pdu) {
        this.pdu = pdu;
    }

    public static void fillSize(byte[] sendData, short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        sendData[6] = bytes[0];
        sendData[7] = bytes[1];
    }

}
