/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;
import java.util.ArrayList;

/**
 *
 * @author Rui Camposinhos
 */
public class LineParser {
    private Pergunta pergunta;

    public LineParser() {
        this.pergunta = new Pergunta();
    }

    public LineParser(String line) {
        this();
        this.setPergunta(line);
    }
    
    private void setPergunta(String p){
        String delims = "[;]";
        String[] tokens = p.split(delims);
        int length = tokens.length;
        
        /*trim initial space*/
        for(int i=0; i < length; i++)
            if(tokens[i].startsWith(" "))
                tokens[i] = tokens[i].substring(1);
        
        /*sets da Pergunta*/
        this.pergunta.setMusica(tokens[0]);
        this.pergunta.setImagem(tokens[1]);
        this.pergunta.setPergunta(tokens[2]);
        /*set das respostas*/
        ArrayList<String> lstRespostas = new ArrayList<>(3);
        lstRespostas.add(tokens[3]);
        lstRespostas.add(tokens[4]);
        lstRespostas.add(tokens[5]);
        int respCerta = Integer.parseInt(tokens[6]);
        this.pergunta.setRespostas(lstRespostas, respCerta);
    }

    public Pergunta getPergunta() {
        return pergunta;
    }
    
    
    
    
    
}
