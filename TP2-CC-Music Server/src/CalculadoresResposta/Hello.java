package CalculadoresResposta;

public class Hello extends CalcResp {

    public Hello(byte[] pdu) {
        super(pdu);
    }

    public byte[] getResposta() {
        byte[] resposta = new byte[255];
        String ok = "OK";
        int i;

        resposta[0] = super.pdu[0];//versao
        resposta[1] = super.pdu[1];//segurança
        resposta[2] = 0;// label
        resposta[3] = 0;// label
        resposta[4] = 0;// tipo=REPLY
        resposta[5] = 1;// campos seguintes
        super.setSize(resposta, (short) ok.length());

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
