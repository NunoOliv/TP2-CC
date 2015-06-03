package CalculadoresResposta;

import DataBase.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import sun.misc.IOUtils;

public class Transmit {

    private PDU pdu;
    private ListaCampos lc;
    private Desafio d;
    private short pergunta;
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
        pergunta = c.byteToShort(c.getDados());

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
        c.setDados(p.getPergunta().getBytes(), (short) p.getPergunta().getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 13); //1ª resposta
        c.setDados(p.getResposta(1).getBytes(), (short) p.getResposta(1).getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 13); //2ª resposta
        c.setDados(p.getResposta(2).getBytes(), (short) p.getResposta(2).getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 13); //3ª resposta
        c.setDados(p.getResposta(3).getBytes(), (short) p.getResposta(3).getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 14); //resposta certa
        c.setDados(c.shortToByte((short) p.getRespCerta()), (short) c.shortToByte((short) p.getRespCerta()).length);
        lc.addCampo(c);

        c = new Campo((byte) 16); //imagem
        aux = convertFileToByte("imagens/" + p.getImagem());
        System.out.println("Tamasnho da imagem: " + aux.length + " Em formato short: " + ((short) aux.length));
        c.setDados(aux, (short) aux.length);
        lc.addCampo(c);
        System.out.println("Imagem transformada em array de bytes!");

        c = new Campo((byte) 18); //musica
        aux = convertFileToByte("musica/" + p.getMusica());
        System.out.println("Tamasnho da imusica: " + aux.length + " Em formato short: " + ((short) aux.length));
        c.setDados(aux, (short) aux.length);
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
        campo.setDados(erro.getBytes(), (short) erro.getBytes().length);
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
