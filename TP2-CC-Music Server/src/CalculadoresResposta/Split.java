package CalculadoresResposta;

import java.util.HashMap;

public class Split {

    private PDU dadosPDU;
    private byte[] dados;
    private HashMap<Integer, PDU> listaPDU;
    private short nPacotes;

    public Split(byte[] dados) {
        this.dados = dados;
        dadosPDU = new PDU(dados);
        this.listaPDU = new HashMap<>();
        nPacotes = 0;

        inicialize();
    }

    public short getNPacotes() {
        return this.nPacotes;
    }

    private void inicialize() {
        PDU pdu;
        ListaCampos lc;
        Campo c;
        int i = 0, j = 1;
        byte[] aux;

        while (i < dados.length) {
            nPacotes++;
            i += 48 * 1024;
        }

        System.out.println("Número de pacotes: " + nPacotes + " Tamanho do PDU: " + dados.length);

        i = 0;

        while (j <= nPacotes) {
            //System.out.println("A tratar do pacote " + j);
            lc = new ListaCampos();

            c = new Campo((byte) 17); //número do bloco
            aux = c.IntToByte(j);
            c.setDados(aux);
            lc.addCampo(c);

            if (j != nPacotes) {// se nao for o último
                c = new Campo((byte) 254);//a lista continua...
                lc.addCampo(c);
            }

            aux = new byte[49 * 1024];
            c = new Campo((byte) 19);// define-se 19 como dados gerais!
            if (j != nPacotes) {// se nao for o último
                //System.out.println("Não é o último.");
                System.arraycopy(dados, i, aux, 0, 48 * 1024);
                i += 48 * 1024;
                c.setDados(aux);
            } else {
                //System.out.println("É o último.");
                int x = 0;
                while (i < dados.length) {
                    aux[x] = dados[i];
                    i++;
                    x++;
                }
                c.setDados(aux);
            }
            lc.addCampo(c);

            //System.out.println("Tamanho do PDU: " + lc.getTotalSize());

            pdu = new PDU(this.dadosPDU.getVersao(),
                    this.dadosPDU.getSeguranca(),
                    this.dadosPDU.getLabel(),
                    this.dadosPDU.getTipo(),
                    lc.getNCampos(),
                    lc.getTotalSize(),
                    lc.generate());
            //System.out.println("PDU= " + pdu);
            listaPDU.put(j, pdu);
            j++;
            //System.out.println("Concluído!\n");
        }
    }

    public byte[][] generate() {
        byte[][] resp = new byte[nPacotes][];
        PDU p;
        int i = 0;
        System.out.println("A guardar os pacotes na matriz...");
        for (int j : listaPDU.keySet()) {

            p = listaPDU.get(j);
            resp[i] = p.generatePDU();
            i++;
        }
        System.out.println("Concluído!");
        return resp;
    }

}
