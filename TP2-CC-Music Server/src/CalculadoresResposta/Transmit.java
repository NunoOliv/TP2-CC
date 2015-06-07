package CalculadoresResposta;

import DataBase.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Transmit {

    private PDU pdu;
    private ListaCampos lc;
    private Desafio d;
    private int pergunta;
    private ArrayList<Desafio> desafios;
    private Pergunta p;

    public Transmit(byte[] dados, ArrayList<Desafio> desafios) {
        this.pdu = new PDU(dados);
        this.lc = new ListaCampos(pdu.getLista(), pdu.getnCampos());
        this.desafios = desafios;
        this.d = null;

        inicia();
    }

    private void inicia() {
        byte[] aux;

        Campo c = lc.getCampoByTag((byte) 7);
        if (c == null) {
            System.out.println("Pedido recebido está mal-formado!");
            generateError("Pedido mal-formado!");
            return;
        }
        String nome = new String(c.getDados());
        for (Desafio des : desafios) {
            if (des.getNome().equals(nome)) {
                d = des;
            }
        }
        if (d == null) {
            System.out.println("FATAL ERROR: Desafio não existe!");
            generateError("Desafio não existe!");
            return;
        }

        c = lc.getCampoByTag((byte) 10); //pergunta
        pergunta = c.byteToInt(c.getDados());

        if (d.getNPerguntas() < pergunta) {
            System.out.println("Pedido de número de pergunta inexistente no desafio!");
            generateError("Número de pergunta inexistente no desafio!");
            return;
        }

        System.out.println("Pedido para a pergunta " + pergunta + " do desafio \"" + nome + "\".");

        p = d.getPergunta(pergunta);

        System.out.println("Pergunta: " + p.getPergunta() + " Respostas: " + p.getRespostas().toString()
                + " Resposta Certa: " + p.getRespCerta() + " Path da Imagem: " + p.getImagem() + " Path da Musica: " + p.getMusica());

        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((byte) 0);
        pdu.setTipo((byte) 0);

        lc = new ListaCampos();

        c = new Campo((byte) 11); //pergunta
        c.setDados(p.getPergunta().getBytes());
        lc.addCampo(c);

        c = new Campo((byte) 13); //1ª resposta
        c.setDados(p.getResposta(1).getBytes());
        lc.addCampo(c);

        c = new Campo((byte) 13); //2ª resposta
        c.setDados(p.getResposta(2).getBytes());
        lc.addCampo(c);

        c = new Campo((byte) 13); //3ª resposta
        c.setDados(p.getResposta(3).getBytes());
        lc.addCampo(c);

        c = new Campo((byte) 14); //resposta certa
        c.setDados(c.IntToByte(p.getRespCerta()));
        lc.addCampo(c);

        c = new Campo((byte) 16); //imagem
        aux = convertFileToByte("imagens/" + p.getImagem());
        System.out.println("Tamanho da imagem: " + aux.length);
        c.setDados(aux);
        lc.addCampo(c);
        System.out.println("Imagem transformada em array de bytes!");

        c = new Campo((byte) 18); //musica
        aux = convertFileToByte("musica/" + p.getMusica());
        System.out.println("Tamanho da imusica: " + aux.length);
        c.setDados(aux);
        lc.addCampo(c);
        System.out.println("Musica transformada em array de bytes!");

        pdu.setTamanho(lc.getTotalSize());
        pdu.setnCampos(lc.getNCampos());
        pdu.setLista(lc.generate());

        System.out.println("PDU criado!");
    }

    public byte[] getResposta() {
        return this.pdu.generatePDU();
    }

    private void generateError(String erro) {

        lc = new ListaCampos();

        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((short) 0); //visto q não se conhece o user
        pdu.setTipo((byte) 0);

        Campo campo = new Campo((byte) 255);
        campo.setDados(erro.getBytes());
        lc.addCampo(campo);

        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());
    }

    private byte[] convertFileToByte(String filePath) {

        FileInputStream fileInputStream = null;

        File file = new File(filePath);

        byte[] bFile = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

            //testes....
            /*
             for (int i = 0; i < bFile.length; i++) {
             System.out.print((char) bFile[i]);
             }*/
        } catch (Exception e) {
            System.out.println("FATAL ERROR: Alguma coisa correu mal!");
            System.exit(0);
        }
        return bFile;
    }
}
