package tp2.cc.music.client;

import Build.Register;
import Exception.NotOkException;
import Exception.UnknownTypeException;
import Exception.VersionMissmatchException;
import java.io.PrintStream;
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
        int opcao = -1;

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
                opcao = -1;
                continue;
            }

            if (opcao > 2 || opcao < 0) {
                out.println("Intruduza uma opção válida!");
                in.nextLine();
                clearScreen();
                opcao = -1;
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

    public void clearScreen() {
        int i = 0;
        while (i < 25) {
            out.println();
            i++;
        }
    }

    /**
     *
     */
    private void login() {
        /*String user;
         String pass;

         clearScreen();
         out.println("*** Login ***");
         out.println();
         out.print("\nEmail: ");
         user = in.nextLine();
         out.print("Password: ");
         pass = in.nextLine();

         try {
         core.login(user, pass);
         out.println("Autenticado com sucesso!");
         in.nextLine();
         clearScreen();
         menu2();
         } catch (EmailInvalidoException ex) {
         out.println("Email introduzido inválido!");
         in.nextLine();
         clearScreen();
         } catch (CamposInvalidosException ex) {
         out.println("Campos introduzidos inválidos!");
         in.nextLine();
         clearScreen();
         } catch (UserNaoExisteException ex) {
         out.println("Não existe nenhum utilizador com esse Email!");
         in.nextLine();
         clearScreen();
         } catch (PasswordMissmatchException ex) {
         out.println("Email e Password não correspondem!");
         in.nextLine();
         clearScreen();
         }*/
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
        if (alcunha.length() > 75) {
            out.print("Nickname muito grande, não pode exceder 75 carateres");
            return;
        }

        out.print("Password: ");
        pass = in.nextLine();
        if (pass.length() > 75) {
            out.print("Password muito grande, não pode exceder 75 carateres");
            return;
        }

        out.print("Nome: ");
        nome = in.nextLine();
        if (nome.length() > 75) {
            out.print("Nome muito grande, não pode exceder 75 carateres");
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
            out.println("Fatal Eror: NotOkException\nPossivelmente existe um utilizador com a mesma alcunha.");
        }
    }
}
