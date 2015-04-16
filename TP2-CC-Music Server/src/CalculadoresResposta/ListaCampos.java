/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import java.util.ArrayList;

/**
 *
 * @author Rafael
 */
public class ListaCampos {
    private final ArrayList<Campo> lista;
    private short totalSize;

    public ListaCampos(ArrayList<Campo> lista) {
        this.lista = lista;
    }

    public ListaCampos() {
        this.lista = new ArrayList<>();
    }
    
    public boolean addCampo(Campo campo) {
        totalSize += campo.getTotalSize();
        return this.lista.add(campo);
    }

    public short getTotalSize() {
        return totalSize;
    }
    
    public byte getNCampos() {
        return (byte)lista.size();
    }
    
    public byte[] generate() {
        byte[] ret = new byte[totalSize];
        int currentSize = 0;
        
        for(Campo campo : lista ) {
            System.arraycopy(campo.generate(), 0, ret, currentSize, campo.getTotalSize() );
            currentSize = currentSize+campo.getTotalSize();
        }
        return ret; 
    }
    
    
    
    
}
