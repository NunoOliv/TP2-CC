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

    private HashSet<User> clientes;

    public UserDB() {
        clientes = new HashSet<>();
    }

    public boolean addClient(User c) {
        if (clientes.contains(c)) {
            return false;
        }
        clientes.add(c);
        return true;
    }

    public boolean addCliente(String alcunha, String pass, String nome, InetAddress ip, int port) {
        for (User c : clientes) {
            if (c.getAlcunha().equals(alcunha)) {
                return false;
            }
        }
        clientes.add(new User(alcunha, pass, nome, ip, port));
        return true;
    }

    public ArrayList topRanked() {
        int p = 0, i;
        ArrayList<User> r = new ArrayList<>();
        User max = null;

        for (i = 0; i < clientes.size(); i++) {
            p = 0;
            max = null;
            for (User c : clientes) {
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

    public User login(String alcunha, String pass) {
        for (User c : clientes) {
            if (c.getAlcunha().equals(alcunha)&&c.getPass().equals(pass)) {
                return c;
            }
        }
        return null;
    }

    public String getNome(String alcunha){
        for (User c : clientes) {
            if (c.getAlcunha().equals(alcunha)) {
                return c.getNome();
            }
        }
        return null;
    }

    public User getCliente(InetAddress ip, int port) {
        for(User c:clientes){
            if(c.getEnderecoIP().equals(ip)&&c.getPort()==port) return c;
        }
        return null;
    }
}
