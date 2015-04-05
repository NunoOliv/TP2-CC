/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

/**
 *
 * @author Rafael
 */
public class UserDB {

    private HashSet<Cliente> clientes;

    public UserDB() {
        clientes = new HashSet<>();
    }

    public void addClient(Cliente c) {
        clientes.add(c);
    }

    public ArrayList topRanked() {
        int p = 0, i;
        ArrayList<Cliente> r = new ArrayList<>();
        Cliente max = null;

        for (i = 0; i < clientes.size(); i++) {
            p=0;
            max=null;
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

}
