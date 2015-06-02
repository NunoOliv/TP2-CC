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

    ArrayList<Desafio> checkLstChallenge(byte[] dados) {
        pdu = new PDU(dados);
        lista = new ListaCampos(pdu.getLista(), pdu.getnCampos());
        ArrayList<Desafio> r = new ArrayList<>();
        int i = 0;
        Desafio d;

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

}
