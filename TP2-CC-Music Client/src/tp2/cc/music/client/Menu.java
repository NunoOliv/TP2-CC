package tp2.cc.music.client;

import Build.Login;
import Build.Logout;
import Build.MakeChallenge;
import Build.Register;
import Exception.NotOkException;
import Exception.UnknownTypeException;
import Exception.VersionMissmatchException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 *
 * @author Rafael Antunes
 * @author Nuno Oliveira
 * @author Rui Pereira
 */
public class Menu {

    PrintStream out;
    Scanner in;
    Comunicador com;
    short label;
    Interpretador inter;

    public Menu(Comunicador comu, Interpretador interp) {
        out = System.out;
        in = new Scanner(System.in);
        this.com = comu;
        this.inter = interp;
        this.label = 1;
    }

    public void start() {
        int opcao;

        out.println();
        while (true) {
            in.nextLine();
            clearScreen();
            out.println("*** CC-Music ***");
            out.println();
            out.println("*** Menu Principal ***");
            out.println();
            out.println("1-Login");
            out.println("2-Registar");
            out.println("0-Sair\n");

            out.print("Opção: ");
            try {
                opcao = Integer.parseInt(in.nextLine());
            } catch (Exception e) {
                out.println("Intruduza uma opção válida!");
                in.nextLine();
                clearScreen();
                continue;
            }

            if (opcao > 2 || opcao < 0) {
                out.println("Intruduza uma opção válida!");
                in.nextLine();
                clearScreen();
                continue;
            }

            switch (opcao) {
                case (0):
                    com.end();
                    break;
                case (1):
                    login();
                    break;
                case (2):
                    register();
            }
        }
    }

    public void menu2() {
        int opcao;

        out.println();
        while (true) {
            out.println("*** CC-Music ***");
            out.println();
            out.println("*** Menu Principal ***");
            out.println();
            out.println("1-Criar novo desafio");
            out.println("2-Listar os desafios atuais");
            out.println("3-Entrar num desafio");
            out.println("0-Sair\n");

            out.print("Opção: ");
            try {
                opcao = Integer.parseInt(in.nextLine());
            } catch (Exception e) {
                out.println("Intruduza uma opção válida!");
                in.nextLine();
                clearScreen();
                continue;
            }

            switch (opcao) {
                case (0):
                    logout();
                    return;
                case (1):
                    makeChallenge();
                    in.nextLine();
                    clearScreen();
                    break;
                case (2):
                    out.println("Unsupported!");
                    in.nextLine();
                    clearScreen();
                    break;
                case (3):
                    out.println("Unsupported!");
                    in.nextLine();
                    clearScreen();
                    break;
                default:
                    out.println("Intruduza uma opção válida!");
                    in.nextLine();
                    clearScreen();
            }
        }
    }

    public void clearScreen() {
        int i = 0;
        while (i < 25) {
            out.println();
            i++;
        }
    }

    private void login() {
        String user;
        String pass;

        clearScreen();
        out.println("*** Login ***");
        out.println();
        out.print("\nNickname: ");
        user = in.nextLine();
        if (user.length() > 75) {
            out.print("Nickname muito grande, não pode exceder 75 carateres");
            return;
        }

        out.print("Password: ");
        pass = in.nextLine();
        if (pass.length() > 75) {
            out.print("Password muito grande, não pode exceder 75 carateres");
            return;
        }

        Login l = new Login(user, pass, label);
        byte[] data = l.generate();
        data = com.send(data);
        label++;
        boolean f = false;
        try {
            f = inter.checkLogin(data);
        } catch (UnknownTypeException ex) {
            out.println("Fatal Eror: UnknownTypeException");
        } catch (VersionMissmatchException ex) {
            out.println("Fatal Eror: VersionMissmatchException");
        } catch (NotOkException ex) {
            out.println("Fatal Eror: NotOkException");
        }
        if (f) {
            menu2();
        }
    }

    private void register() {
        String nome;
        String alcunha;
        String pass;

        clearScreen();
        out.println("*** Registar ***");
        out.println();
        out.println("Introduza os seus dados:");

        out.print("Nickname: ");
        alcunha = in.nextLine();
        if (alcunha.length() > 255) {
            out.println("Nickname muito grande, não pode exceder 255 carateres");
            return;
        }

        out.print("Password: ");
        pass = in.nextLine();
        if (pass.length() > 255) {
            out.println("Password muito grande, não pode exceder 255 carateres!");
            return;
        }

        out.print("Nome: ");
        nome = in.nextLine();
        if (nome.length() > 255) {
            out.println("Nome muito grande, não pode exceder 255 carateres!");
            return;
        }

        Register r = new Register(alcunha, pass, nome, label);
        byte[] data = r.generate();
        data = com.send(data);
        label++;
        try {
            inter.checkOK(data);
        } catch (UnknownTypeException ex) {
            out.println("Fatal Eror: UnknownTypeException");
        } catch (VersionMissmatchException ex) {
            out.println("Fatal Eror: VersionMissmatchException");
        } catch (NotOkException ex) {
            out.println("Fatal Eror: NotOkException");
        }
    }

    private void logout() {
        Logout l = new Logout(label);
        byte[] data = l.generate();
        data = com.send(data);
        label++;
        try {
            inter.checkOK(data);
        } catch (UnknownTypeException ex) {
            out.println("Fatal Eror: UnknownTypeException");
        } catch (VersionMissmatchException ex) {
            out.println("Fatal Eror: VersionMissmatchException");
        } catch (NotOkException ex) {
            out.println("Fatal Eror: NotOkException");
        }
    }

    private void makeChallenge() {
        String nome;
        String data;
        String hora;
        int ano, mes, dia, hour, min, seg;

        out.println("*** Criar novo desafio ***");
        out.println();
        out.print("Introduza o nome do desafio: ");
        nome = in.nextLine();
        if (nome.length() > 255) {
            out.println("Nome muito grande, não pode exceder 255 carateres!");
            return;
        }

        out.print("Introduza a data (AAMMDD): ");
        data = in.nextLine();
        if (data.length() != 6) {
            out.println("Formato inválido!");
            return;
        }

        try {
            Integer.parseInt(data);
            ano = 2000 + Integer.parseInt(data.substring(0, 2));
            mes = Integer.parseInt(data.substring(2, 4));
            dia = Integer.parseInt(data.substring(4));
            if (ano < 2015 || mes < 0 || dia < 0 || mes > 12 || dia > 31) {
                throw new Exception("Parâmetros inválidos!");
            }
        } catch (Exception e) {
            out.println("Carateres inválidos introduzidos!");
            return;
        }

        out.print("Introduza a hora (HHMMSS): ");
        hora = in.nextLine();
        if (hora.length() != 6) {
            out.println("Formato inválido!");
            return;
        }
        try {
            Integer.parseInt(hora);
            hour = Integer.parseInt(hora.substring(0, 2));
            min = Integer.parseInt(hora.substring(2, 4));
            seg = Integer.parseInt(hora.substring(4));
            if (hour < 0||hour>24 || min < 0 || seg < 0 || min > 60 || seg > 60) {
                throw new Exception("Parâmetros inválidos!");
            }
        } catch (Exception e) {
            out.println("Carateres inválidos introduzidos!");
            return;
        }
  
        MakeChallenge mkC = new MakeChallenge(nome, data, hora, label);
        byte[] dados = mkC.generate();
        dados = com.send(dados);
        label++;

        //falta acabar
    }
}
