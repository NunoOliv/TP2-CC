/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import java.util.ArrayList;

/**
 *
 * @author Rafael
 */
public class Split {

    private final PDU dadosPDU;
    private final byte[] dados;
    private ArrayList<PDU> listaPDU;
    private short nPacotes;

    public Split(byte[] dados) {
        this.dados = dados;
        dadosPDU = new PDU(dados);
        this.listaPDU = new ArrayList<>();
        nPacotes=0;
        inicialize();
    }
    
    public short getNPacotes(){
        return this.nPacotes;
    }
    
    private void inicialize() {
        PDU pdu;
        ListaCampos lc;
        Campo c;
        int i = 0;
        short j = 0;
        byte[] aux;

        while (i < dados.length) {
            nPacotes++;
            i += 48 * 1024;
        }

        i = 0;

        while (j < nPacotes) {
            lc = new ListaCampos();

            c = new Campo((byte) 17);
            aux = c.shortToByte(j);
            c.setDados(aux, (byte) aux.length);
            lc.addCampo(c);

            if (j != nPacotes - 1) {// se nao for o último
                c = new Campo((byte) 254);//a lista continua...
                lc.addCampo(c);
            }

            c = new Campo((byte) 19);// define-se 19 como dados gerais!
            if (j != nPacotes - 1) {// se nao for o último
                System.arraycopy(dados, i, aux, 0, 48 * 1024);
                i += 48 * 1024;
                c.setDados(aux, (byte) aux.length);
            } else {
                int x = 0;
                while (i < dados.length) {
                    aux[x] = dados[i];
                    i++;
                    x++;
                }
                c.setDados(aux, (byte) aux.length);
            }

            pdu = new PDU(this.dadosPDU.getVersao(),
                    this.dadosPDU.getSeguranca(),
                    this.dadosPDU.getLabel(),
                    this.dadosPDU.getTipo(),
                    lc.getNCampos(),
                    lc.getTotalSize(),
                    lc.generate());
            listaPDU.add(j, pdu);
            j++;
        }
    }
    
    public byte[][] generate(){
        byte[][] resp = null;
        int i=0;
        while(i<nPacotes){
            resp[i]=listaPDU.get(i).generatePDU();
        }
        return resp;
    }

}
