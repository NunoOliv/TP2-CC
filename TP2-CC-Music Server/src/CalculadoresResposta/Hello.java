package CalculadoresResposta;

public class Hello extends CalcResp{


    public Hello(byte[] pdu) {
        super(pdu);
    }

    /*
     private short getLabel() {
     byte[] labelBytes = {pdu[2], pdu[3]};
     return ByteBuffer.wrap(labelBytes).order(ByteOrder.BIG_ENDIAN).getShort();
     }
     */



    public byte[] getResposta() {
        byte[] resposta = new byte[255];
        String ok = "OK";
        int i;

        resposta[0] = super.pdu[0];//versao
        resposta[1] = super.pdu[1];//segurança
        resposta[2] = super.pdu[2];//label
        resposta[3] = super.pdu[3];
        resposta[4] = 0;// tipo=REPLY
        resposta[5] = 1;//campos seguintes
        super.fillSize(resposta, (short) ok.length());

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
