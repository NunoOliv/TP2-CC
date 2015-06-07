/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music.client;

import Build.Campo;
import Build.ListaCampos;
import Build.PDU;
import Exception.NotOkException;
import Exception.UnknownTypeException;
import Exception.VersionMissmatchException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rafael
 */
public class Interpretador {

    private PDU pdu;
    private ListaCampos lista;

    public Interpretador() {

    }

    public boolean checkLogin(byte[] data) throws VersionMissmatchException, UnknownTypeException, NotOkException {

        pdu = new PDU(data);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());

        if (pdu.getVersao() != 0) {
            throw new VersionMissmatchException();
        }

        if (pdu.getTipo() != 0) {
            throw new UnknownTypeException();
        }

        if (pdu.getnCampos() != 1) {
            throw new NotOkException();
        }

        // (unsignedByte & 0xff) como ler unsigned byte.
        Campo c = lista.getCampo(0);
        if ((c.getTag() & 0xff) == 255) {
            System.out.println("Erro: " + new String(c.getDados()));
            return false;
        }
        if (c.getTag() == 1) {
            System.out.println("Bem vindo " + new String(c.getDados()) + "!");
            return true;
        }
        return false;
    }

    public boolean checkOK(byte[] data) throws UnknownTypeException, VersionMissmatchException, NotOkException {

        pdu = new PDU(data);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());

        if (pdu.getVersao() != 0) {
            throw new VersionMissmatchException();
        }

        if (pdu.getTipo() != 0) {
            throw new UnknownTypeException();
        }

        if (pdu.getnCampos() != 1) {
            throw new NotOkException();
        }
        //System.out.println(lista.toString());
        Campo c = lista.getCampo(0);
        if ((c.getTag() & 0xff) == 255) {
            System.out.println("Erro: " + new String(c.getDados()));
            return false;
        }
        if (c.getTag() == 0) {
            System.out.println("OK!");
            return true;
        }

        return false;
    }

    public boolean checkMkChallenge(byte[] dados) throws VersionMissmatchException, UnknownTypeException {

        pdu = new PDU(dados);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());

        if (pdu.getVersao() != 0) {
            throw new VersionMissmatchException();
        }

        if (pdu.getTipo() != 0) {
            throw new UnknownTypeException();
        }

        Campo c = lista.getCampo(0);
        if ((c.getTag() & 0xff) == 255) {
            System.out.println("Erro: " + new String(c.getDados()));
            return false;
        }
        int i = 0;
        while (i < lista.getNCampos()) {
            c = lista.getCampo(i);
            if (c.getTag() == 7 || c.getTag() == 4 || c.getTag() == 5) {
                //all is good
            } else {
                //unexpected field
                System.out.println("Erro: Recebido campo inesperado!");
                return false;
            }
            i++;
        }

        return true;
    }

    public ArrayList<Desafio> checkLstChallenge(byte[] dados) {
        pdu = new PDU(dados);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());
        ArrayList<Desafio> r = new ArrayList<>();
        int i = 0;
        Desafio d;

        if (pdu.getnCampos() == 0) {
            System.out.println("Não há desafios a decorrer neste momento!");
            return null;
        }

        Campo c = lista.getCampo(0);
        if ((c.getTag() & 0xff) == 255) {
            System.out.println("Erro: " + new String(c.getDados()));
            return null;
        }

        while (i < lista.getNCampos()) {
            d = new Desafio();

            c = lista.getCampo(i);
            i++;
            if (c.getTag() != 7) {
                System.out.println("Alguma coisa correu mal: Recebido pacote não esperado!");
                return null;
            }
            d.setNome(new String(c.getDados()));

            c = lista.getCampo(i);
            i++;
            if (c.getTag() != 4) {
                System.out.println("Alguma coisa correu mal: Recebido pacote não esperado!");
                return null;
            }
            try {
                d.setData(new String(c.getDados()));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return null;
            }

            c = lista.getCampo(i);
            i++;
            if (c.getTag() != 5) {
                System.out.println("Alguma coisa correu mal: Recebido pacote não esperado!");
                return null;
            }
            try {
                d.setHora(new String(c.getDados()));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return null;
            }

            r.add(d);
        }

        return r;
    }

    public String[] checkAcceptChallenge(byte[] dados) throws VersionMissmatchException, UnknownTypeException, NotOkException {
        String[] resp = new String[2];

        pdu = new PDU(dados);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());

        if (pdu.getVersao() != 0) {
            throw new VersionMissmatchException();
        }

        if (pdu.getTipo() != 0) {
            throw new UnknownTypeException();
        }

        //System.out.println(lista.toString());
        Campo c = lista.getCampo(0);
        if ((c.getTag() & 0xff) == 255) {
            System.out.println("Erro: " + new String(c.getDados()));
            throw new NotOkException();
        }
        if (c.getTag() == 4) { //data
            resp[0] = new String(c.getDados());
        } else {
            System.out.println("Erro: Recebido campo inesperado!");
            throw new NotOkException();
        }

        c = lista.getCampo(1);
        if (c.getTag() == 5) {//hora
            resp[1] = new String(c.getDados());
        } else {
            System.out.println("Erro: Recebido campo inesperado!");
            throw new NotOkException();
        }

        return resp;
    }

    public Pergunta checkTransmit(byte[] dados, Desafio desafio, int pergunta) {
        pdu = new PDU(dados);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());
        Pergunta p = null;
        FileOutputStream fos = null;
        int tamanhoSoFar=0;

        //System.out.println(lista.toString());
        Campo c = lista.getCampoByTag((byte) 255); //erro
        if (c != null) {
            System.out.println("Erro: " + new String(c.getDados()));
            return null;
        }
        //tamanhoSoFar+=c.getTotalSize();
        c = lista.getCampoByTag((byte) 11); //pergunta
        p = new Pergunta(new String(c.getDados()));
tamanhoSoFar+=c.getTotalSize();
        c = lista.getCampo(1); //1ª resposta
        System.out.println("Resp1: " + new String(c.getDados()));
        p.addResposta(new String(c.getDados()));
tamanhoSoFar+=c.getTotalSize();
        c = lista.getCampo(2);//2ª resposta
        System.out.println("Resp2: " + new String(c.getDados()));
        p.addResposta(new String(c.getDados()));
tamanhoSoFar+=c.getTotalSize();
        c = lista.getCampo(3);//3ª resposta
        System.out.println("Resp3: " + new String(c.getDados()));
        p.addResposta(new String(c.getDados()));
tamanhoSoFar+=c.getTotalSize();
        c = lista.getCampo(4);//respost Certa
        System.out.println("Resposta Certa: " + c.byteToInt(c.getDados()));
        p.setRespCerta(c.byteToInt(c.getDados()));
tamanhoSoFar+=c.getTotalSize();
        c = lista.getCampo(5); //imagem
        System.out.println("Tag: " + c.getTag());
        try {
            fos = new FileOutputStream("image.jpg");
            fos.write(c.getDados());
            fos.close();
        } catch (IOException ex) {
            System.out.println("FATAL ERROR: FileOutputStream Falhou na imagem!");
            System.exit(0);
        }
tamanhoSoFar+=c.getTotalSize();
        System.out.println("Leitura da imagem terminou com sucesso! Acabou +/- na posicao "+tamanhoSoFar);

        c = lista.getCampoByTag((byte) 18); //musica tag:18
        System.out.println("Campo Musica: " + c);
        System.out.println("Tag: " + c.getTag());
        try {
            fos = new FileOutputStream("music.mp3");
            fos.write(c.getDados());
            fos.close();
        } catch (IOException ex) {
            System.out.println("FATAL ERROR: FileOutputStream falhou na musica!");
            System.exit(0);
        }

        System.out.println("Leitura da musica terminou com sucesso!");

        return p;
    }
}
