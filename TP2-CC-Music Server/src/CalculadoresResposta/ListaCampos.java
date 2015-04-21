package CalculadoresResposta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListaCampos {

    private ArrayList<Campo> lista;
    //private short totalSize;

    public ListaCampos(ArrayList<Campo> lista) {
        this.lista = lista;
    }

    public ListaCampos() {
        this.lista = new ArrayList<>();
    }

    public ListaCampos(byte[] data, short nCampos) {
        lista = new ArrayList<>();
        int i = 0, j = 0;

        byte tag;
        short size;
        byte[] dados;
        byte[] aux = new byte[2];

        while (j < nCampos) {

            tag = data[i];

            System.arraycopy(data, i + 1, aux, 0, 2);
            size = byteToShort(aux);

            if (size > 0) {
                dados = new byte[size];
                System.arraycopy(data, i + 3, dados, 0, size);

                addCampo(new Campo(tag, size, dados));
            } else {
                addCampo(new Campo(tag));
            }

            i += size + 3;
            j++;
        }
    }

    public ListaCampos(ListaCampos lc) {
        this.lista = lc.getCampos();
    }

    public ArrayList<Campo> getCampos() {
        return (ArrayList<Campo>) lista.clone();
    }

    public Campo getCampo(int posicao) throws ArrayIndexOutOfBoundsException {
        if (lista.size() < posicao) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return lista.get(posicao).clone();
    }

    public Campo getCampoByTag(byte tag) throws ArrayIndexOutOfBoundsException {
        for (Campo c : lista) {
            if (c.getTag() == tag) {
                return c;
            }
        }
        return null;
    }

    public boolean addCampo(Campo campo) {
        //totalSize += campo.getTotalSize();
        return this.lista.add(campo);
    }

    public short getTotalSize() {
        short t = 0;
        for (Campo c : lista) {
            t += c.getTotalSize();
        }
        return t;
    }

    public byte getNCampos() {
        return (byte) lista.size();
    }

    public byte[] generate() {
        byte[] ret = new byte[getTotalSize()];
        int currentSize = 0;

        for (Campo campo : lista) {
            System.arraycopy(campo.generate(), 0, ret, currentSize, campo.getTotalSize());
            currentSize = currentSize + campo.getTotalSize();
        }
        return ret;
    }

    public byte[] shortToByte(short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        return bytes;
    }

    public short byteToShort(byte[] data) {
        byte[] sizeBytes = {data[0], data[1]};
        return ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public byte[][] splitFile(File source, int splitSize) throws IOException{
        long totSize = source.length();
        int nParts = (int)(totSize / splitSize) + 1; //ex: splitSize = 48kBytes = 48*1024
        byte[][] ret = new byte[nParts][splitSize];
        InputStream input = null;
        int i = 0;
        
        try {
            input = new FileInputStream(source);
            while(input.read(ret[i], 0, splitSize) > 0)
                i++;
        }
        finally{
            input.close();
        }
        return ret;
    }
    
    @Override
    public String toString() {
        return "ListaCampos{" + "lista=" + lista + '}';
    }

    @Override
    protected ListaCampos clone() {
        return new ListaCampos(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ListaCampos) {
            ListaCampos lc = (ListaCampos) o;

            if (lc.getNCampos() != this.getNCampos()) {
                return false;
            }

            if (lc.getTotalSize() != this.getTotalSize()) {
                return false;
            }

            for (int i = 0; i < lc.getNCampos(); i++) {
                try {
                    if (!lc.getCampo(i).equals(this.lista.get(i))) {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
