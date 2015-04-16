/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Rafael
 */
public class UserDB {

    private HashSet<Cliente> clientes;

    public UserDB() {
        clientes = new HashSet<>();
    }

    public boolean addClient(Cliente c) {
        if (clientes.contains(c)) {
            return false;
        }
        clientes.add(c);
        return true;
    }

    public boolean addCliente(String alcunha, String pass, String nome, InetAddress ip, int port) {
        for (Cliente c : clientes) {
            if (c.getAlcunha().equals(alcunha)) {
                return false;
            }
        }
        clientes.add(new Cliente(alcunha, pass, nome, ip, port));
        return true;
    }

    public ArrayList topRanked() {
        int p = 0, i;
        ArrayList<Cliente> r = new ArrayList<>();
        Cliente max = null;

        for (i = 0; i < clientes.size(); i++) {
            p = 0;
            max = null;
            for (Cliente c : clientes) {
                if (r.contains(c)) {
                    continue;
                }
                if (c.getPontuacao() > p) {
                    p = c.getPontuacao();
                    max = c;
                }

            }
            r.add(max);
        }
        return r;
    }

    public Cliente login(String alcunha, String pass) {
        for (Cliente c : clientes) {
            if (c.getAlcunha().equals(alcunha)&&c.getPass().equals(pass)) {
                return c;
            }
        }
        return null;
    }

    public String getNome(String alcunha){
        for (Cliente c : clientes) {
            if (c.getAlcunha().equals(alcunha)) {
                return c.getNome();
            }
        }
        return null;
    }

    public Cliente getCliente(InetAddress ip, int port) {
        for(Cliente c:clientes){
            if(c.getEnderecoIP().equals(ip)&&c.getPort()==port) return c;
        }
        return null;
    }
}
