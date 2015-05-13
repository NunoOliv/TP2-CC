package tp2.cc.music.client;

import Exception.InvalidSetOfAnswersException;
import java.util.ArrayList;

class Pergunta {

    private String pergunta;
    private ArrayList<String> respostas;
    private int respCerta;
    private String musica; //path para a musica
    private String imagem; //path para a imagem

    public Pergunta(String pergunta, ArrayList<String> respostas, int respCerta, String musica, String imagem) {
        this.pergunta = pergunta;
        this.respostas = respostas;
        this.respCerta = respCerta;
        this.musica = musica;
        this.imagem = imagem;
    }

    public Pergunta(String pergunta) {
        this.pergunta = pergunta;
        this.respostas = new ArrayList<>(3);
        this.respCerta = -1;
        this.musica = null;
        this.imagem = null;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public int getRespCerta() {
        return respCerta;
    }

    /*public void setRespCerta(int respCerta) {
     this.respCerta = respCerta;
     }*/
    public String getMusica() {
        return musica;
    }

    public void setMusica(String musica) {
        this.musica = musica;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setRespostas(ArrayList<String> r, int respCerta) throws InvalidSetOfAnswersException {
        if (r.size() != 3 || respCerta > 3 || respCerta < 1) {
            throw new InvalidSetOfAnswersException();
        }
        this.respostas = r;
        this.respCerta = respCerta;
    }

    public ArrayList<String> getRespostas() {
        return this.respostas;
    }

    public boolean testaResposta(int tentativa) {
        return (tentativa == this.respCerta);
    }

}
