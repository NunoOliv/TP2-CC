/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rui Camposinhos
 */
public class Parser {

    private String musicDir;
    private String imageDir;
    private int nQuestions;
    private ArrayList<Pergunta> perguntas;

    public Parser() {
        this.imageDir = "";
        this.musicDir = "";
        this.nQuestions = 0;
        this.perguntas = new ArrayList<>();
    }

    public String getMusicDir() {
        return musicDir;
    }

    public String getImageDir() {
        return imageDir;
    }

    public int getnQuestions() {
        return nQuestions;
    }

    public ArrayList<Pergunta> getPerguntas() {
        ArrayList<Pergunta> mPerguntas = new ArrayList<>();
        for (Pergunta p : this.perguntas) {
            mPerguntas.add(p);
        }
        return mPerguntas;
    }

    public void setMusicDir(String musicDir) {
        this.musicDir = musicDir;
    }

    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    public void setnQuestions(int nQuestions) {
        this.nQuestions = nQuestions;
    }

    public void setPerguntas(ArrayList<Pergunta> perguntas) {
        for (Pergunta p : perguntas) {
            this.perguntas.add(p);
        }
    }

    public void parseDesafioWnum(int numDesafio) throws FileNotFoundException, IOException {
        //exemplo desafio-000001.txt
        String f = "desafio-";
        String n = Integer.toString(numDesafio);
        int l = n.length();

        int zeros = 6 - l;
        for (int i = 0; i < zeros; i++) {
            f = f.concat("0");
        }
        f = f.concat(n);
        f = f.concat(".txt");

        parseDesafioWfp(f);

    }

    public void parseDesafioWfp(String fileName) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            sCurrentLine = sCurrentLine.trim();

            if (sCurrentLine.contains("music_DIR=")) {
                setMusicDir(sCurrentLine.substring(11));
            }
            if (sCurrentLine.contains("images_DIR=")) {
                setImageDir(sCurrentLine.substring(12));
            }
            if (sCurrentLine.contains("questions_#=")) {
                setnQuestions(Integer.parseInt(sCurrentLine.substring(13)));
            }
            if (!sCurrentLine.equals("")) {
                perguntas.add(parseLine(sCurrentLine));
            }
        }

    }

    private Pergunta parseLine(String linha) {

        String[] tokens = linha.split(";");
        int length = tokens.length;
        Pergunta p;
        String music,image,pergunta;

        /*trim initial space*/
        for (int i = 0; i < length; i++) {
            if (tokens[i].startsWith(" ")) {
                tokens[i] = tokens[i].substring(1);
            }
        }

        /*sets da Pergunta*/
        music=tokens[0];
        image=tokens[1];
        pergunta=tokens[2];
        /*set das respostas*/
        ArrayList<String> lstRespostas = new ArrayList<>(3);
        lstRespostas.add(tokens[3]);
        lstRespostas.add(tokens[4]);
        lstRespostas.add(tokens[5]);
        
        int respCerta = Integer.parseInt(tokens[6]);
        p=new Pergunta(pergunta, lstRespostas, respCerta, music, image);
        
        return p;
    }

}
