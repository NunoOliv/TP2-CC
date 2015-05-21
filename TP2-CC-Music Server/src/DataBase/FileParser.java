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
public class FileParser {
    private String musicDir;
    private String imageDir;
    private int nQuestions;
    private ArrayList<Pergunta> perguntas;


    public FileParser() {
        perguntas = new ArrayList<>();
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
        for(Pergunta p : this.perguntas)
            mPerguntas.add(p);
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
        for(Pergunta p : perguntas)
            this.perguntas.add(p);
    }
    
    
    public void parseDesafioWnum(String numDesafio) throws FileNotFoundException{
        //exemplo desafio-000001.txt
        String f = "desafio-";
        int l = numDesafio.length();
        int zeros = 6 - l;
        for(int i=0; i < zeros; i++){
            f.concat("0");
        }
        f.concat(numDesafio);
        f.concat(".txt");
        try{
        parseDesafioWfp(f);
        }catch(IOException e) {System.out.println(e.getMessage());}
    }
    
    public void parseDesafioWfp(String fileName) throws FileNotFoundException{
        
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null){
                sCurrentLine = sCurrentLine.trim();
                if(sCurrentLine.contains("music_DIR="))
                    setMusicDir(sCurrentLine.substring(11));
                else if(sCurrentLine.contains("images_DIR="))
                    setImageDir(sCurrentLine.substring(12));
                else if(sCurrentLine.contains("questions_#="))
                    setnQuestions(Integer.parseInt(sCurrentLine.substring(13)));
                else if (!sCurrentLine.equals("")){
	            LineParser parseLine = new LineParser(sCurrentLine);
                    this.perguntas.add(parseLine.getPergunta());  
                }
            }
        }catch(IOException e) {System.out.println(e.getMessage());}
    }
    
    
    
    
    
    

    
    
}
