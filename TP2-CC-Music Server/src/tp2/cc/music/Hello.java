package tp2.cc.music;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Hello {

    byte[] pdu;

    short label;

    public Hello(byte[] pdu) {
        this.pdu = pdu;
    }


    /*
     private short getLabel() {
     byte[] labelBytes = {pdu[2], pdu[3]};
     return ByteBuffer.wrap(labelBytes).order(ByteOrder.BIG_ENDIAN).getShort();
     }
     */
    private static void fillSize(byte[] sendData, short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        sendData[6] = bytes[0];
        sendData[7] = bytes[1];
    }

    public byte[] getResposta() {
        byte[] resposta = new byte[255];
        String ok = "OK";
        int i;

        resposta[0] = pdu[0];//versao
        resposta[1] = pdu[1];//segurança
        resposta[2] = pdu[2];//label
        resposta[3] = pdu[3];
        resposta[4] = 0;// tipo=REPLY
        resposta[5] = 1;//campos seguintes
        fillSize(resposta, (short) ok.length());

        byte[] aux = ok.getBytes();
        /*System.out.print("aux: " + aux[0]);
        System.out.print(aux[1]);
        System.out.println();*/

        for (i = 0; (i < 255 - 8 && i < aux.length); i++) {
            resposta[i + 8] = aux[i];
        }

        /*System.out.print("Resp: ");

        for (i = 0; i < resposta.length; i++) {
            System.out.print(resposta[i]);
        }
        System.out.println();*/
        return resposta;
    }
}
