/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music.client;

/**
 *
 * @author Rafael
 */
public class TP2CCMusicClient {

    private static Comunicador com;

    public static void main(String[] args) {
        com = new Comunicador();
        com.start();

    }

}
