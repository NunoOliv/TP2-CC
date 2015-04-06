package tp2.cc.music.client;

import java.util.Scanner;

public class TP2CCMusicClient {

    private static Comunicador com;
    private static Menu menu;
    private static Scanner in;
    private static Interpretador inter;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        inter=new Interpretador();
        com = new Comunicador(inter);
        menu=new Menu(com, inter);

        System.out.println("*** CC-Music ***");
        System.out.println();
        System.out.println("A tentar comunicar com o servidor...");
        com.start();
        System.out.println("Servidor encontrado!");
        in.nextLine();

        menu.start();

        com.end();
    }

}
