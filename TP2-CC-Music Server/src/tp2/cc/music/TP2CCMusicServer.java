/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;

/**
 *
 * @author Rafael
 */
public class TP2CCMusicServer {

    private static HashSet<Cliente> clientes;
    private static ServerUDP serverUDP;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException {
        serverUDP=new ServerUDP();
        clientes= new HashSet<>();
        serverUDP.start();
        //serverUDP.stop();
    }

}
