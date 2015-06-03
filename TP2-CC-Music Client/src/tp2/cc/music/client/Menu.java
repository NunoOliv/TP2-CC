package tp2.cc.music.client;

import Build.AcceptChallenge;
import Build.Group;
import Build.ListChallenge;
import Build.Login;
import Build.Logout;
import Build.MakeChallenge;
import Build.NextPackage;
import Build.Register;
import Build.Transmit;
import Exception.MissingPieciesException;
import Exception.NotOkException;
import Exception.UnknownTypeException;
import Exception.VersionMissmatchException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rafael Antunes
 * @author Nuno Oliveira
 * @author Rui Camposinhos
 */
public class Menu {

    private PrintStream out;
    private Scanner in;
    private Comunicador com;
    private short label;
    private Interpretador inter;
    private Desafio desafio;

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
                continue;
            }

            if (opcao > 2 || opcao < 0) {
                out.println("Intruduza uma opção válida!");
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
            in.nextLine();
            clearScreen();
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
                continue;
            }

            switch (opcao) {
                case (0):
                    logout();
                    return;
                case (1):
                    makeChallenge();
                    break;
                case (2):
                    listChallenge();
                    break;
                case (3):
                    acceptChallenge();
                    break;
                default:
                    out.println("Intruduza uma opção válida!");
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
            out.print("Nickname muito grande, não pode exceder 75 carateres!");
            return;
        }

        if (user.length() < 1) {
            out.print("Nickname inválido!");
            return;
        }

        out.print("Password: ");
        pass = in.nextLine();
        if (pass.length() > 75) {
            out.print("Password muito grande, não pode exceder 75 carateres!");
            return;
        }

        if (pass.length() < 1) {
            out.print("Password inválida!");
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

        if (alcunha.length() < 1) {
            out.print("Alcunha Inválida!");
            return;
        }

        out.print("Password: ");
        pass = in.nextLine();
        if (pass.length() > 255) {
            out.println("Password muito grande, não pode exceder 255 carateres!");
            return;
        }

        if (pass.length() < 1) {
            out.print("Password Inválida!");
            return;
        }

        out.print("Nome: ");
        nome = in.nextLine();
        if (nome.length() > 255) {
            out.println("Nome muito grande, não pode exceder 255 carateres!");
            return;
        }

        if (nome.length() < 1) {
            out.print("Nome Inválido!");
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

        clearScreen();
        out.println("*** Criar novo desafio ***");
        out.println();
        out.print("Introduza o nome do desafio: ");
        nome = in.nextLine();
        if (nome.length() > 255) {
            out.println("Nome muito grande, não pode exceder 255 carateres!");
            return;
        }

        if (nome.length() < 1) {
            out.print("Nome Inválido!");
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
            if (hour < 0 || hour > 24 || min < 0 || seg < 0 || min > 60 || seg > 60) {
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

        try {
            if (inter.checkMkChallenge(dados)) {
                out.println("Desafio criado!");
                desafio = new Desafio();
                desafio.setNome(nome);
                try {
                    desafio.setData(data);
                    desafio.setHora(hora);
                    desafio.dateSFtoIF();
                } catch (Exception ex) {
                    out.println("FATAL ERROR: Data inválida!");
                    System.exit(0);
                }
                waitForMatch();
                return;
            }
            out.println("Desafio não foi criado!");
        } catch (UnknownTypeException ex) {
            out.println("Fatal Eror: UnknownTypeException");
        } catch (VersionMissmatchException ex) {
            out.println("Fatal Eror: VersionMissmatchException");
        }
    }

    private void listChallenge() {
        clearScreen();
        out.println("*** Lista de Desafios ***");
        out.println();

        ListChallenge lc = new ListChallenge(label);
        label++;
        byte[] dados = lc.generate();
        dados = com.send(dados);
        ArrayList<Desafio> desafios;

        desafios = inter.checkLstChallenge(dados);
        if (desafios == null) {
            return;
        }
        for (Desafio d : desafios) {
            System.out.println("Nome: \"" + d.getNome() + "\"  Data: \"" + d.getData() + "\" Hora: \"" + d.getHora() + "\"");
        }
    }

    private void acceptChallenge() {
        String nome, data, hora;

        clearScreen();
        out.println("*** Entrar num Desafio ***");
        out.println();
        out.print("Introduza o nome do desafio: ");

        nome = in.nextLine();
        if (nome.length() > 255) {
            out.println("Nome muito grande, não pode exceder 255 carateres!");
            return;
        }
        if (nome.length() < 1) {
            out.print("Nome Inválido!");
            return;
        }

        AcceptChallenge ac = new AcceptChallenge(nome, label);
        label++;
        byte[] dados = ac.generate();

        dados = com.send(dados);

        try {
            String[] dataEhora = inter.checkAcceptChallenge(dados);
            data = dataEhora[0];
            hora = dataEhora[1];
            desafio = new Desafio(nome, data, hora);
            desafio.dateSFtoIF();
            out.print("Registado no desafio \"" + nome + "\" com sucesso!");
            waitForMatch();
        } catch (UnknownTypeException ex) {
            out.println("Fatal Eror: UnknownTypeException");
        } catch (VersionMissmatchException ex) {
            out.println("Fatal Eror: VersionMissmatchException");
        } catch (NotOkException ex) {
        } catch (Exception ex) {
            out.println("FATAL ERROR: " + ex.getMessage());
            System.exit(0);
        }
    }

    private synchronized void waitForMatch() {
        /*out.println("Tempo atual: " + Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1)
         + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "   " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
         + "-" + Calendar.getInstance().get(Calendar.MINUTE) + "-" + Calendar.getInstance().get(Calendar.SECOND));

         out.println("Tempo de Jogo: " + desafio.getAno() + "-" + desafio.getMes() + "-" + desafio.getDia()
         + "   " + desafio.getHor() + "-" + desafio.getMin() + "-" + desafio.getSeg());

         out.println("Diferença de Anos: " + (desafio.getAno() - Calendar.getInstance().get(Calendar.YEAR)));
         out.println("Diferença de Meses: " + (desafio.getMes() - (Calendar.getInstance().get(Calendar.MONTH) + 1)));
         out.println("Diferença de Dias: " + (desafio.getDia() - Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
         out.println("Diferença de Horas: " + (desafio.getHor() - Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
         out.println("Diferença de Minutos: " + (desafio.getMin() - Calendar.getInstance().get(Calendar.MINUTE)));
         out.println("Diferença de Segundos: " + (desafio.getSeg() - Calendar.getInstance().get(Calendar.SECOND)));*/
        out.println("À espera que o desafio começe...");

        int tempo = 0;

        tempo += (356 * 24 * 60 * 60) * (desafio.getAno() - Calendar.getInstance().get(Calendar.YEAR));
        tempo += (30 * 24 * 60 * 60) * (desafio.getMes() - (Calendar.getInstance().get(Calendar.MONTH) + 1));
        tempo += (24 * 60 * 60) * (desafio.getDia() - Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        tempo += (60 * 60) * (desafio.getHor() - Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        tempo += (60) * (desafio.getMin() - Calendar.getInstance().get(Calendar.MINUTE));
        tempo += desafio.getSeg() - Calendar.getInstance().get(Calendar.SECOND);
        out.println("Tempo de Espera: " + tempo + " segundos...");

        if (tempo < 0) {
            out.println("FATAL ERROR: Tempo negativo!");
            System.exit(0);
        }
        try {
            wait(tempo * 1000);

        } catch (InterruptedException ex) {
        }

        out.println("Espera Terminou! O jogo vai começar!");

        inDesafio();
    }

    private void inDesafio() {
        Transmit t;
        NextPackage np;
        Group g = new Group();
        byte[] piece;
        short i = 2;
        short pergunta = 1;
        Pergunta p = null;

        while (true) {
            t = new Transmit(label, pergunta, desafio.getNome());
            piece = com.send(t.generate());
            g.addPiece(piece);
            out.println("Recebido o pacote: 1");
            while (!g.isComplete()) {
                np = new NextPackage(i);
                i++;
                piece = com.send(np.generatePDU());
                g.addPiece(piece);
                out.println("Recebido o pacote: " + (i - 1));
            }
            out.println("Todos os pacotes foram recebidos.");

            try {
                p = inter.checkTransmit(g.generate().generatePDU(), desafio, pergunta);
                if (p == null) {
                    System.out.println("FATAL ERROR: Pergunta == null");
                    System.exit(0);
                }
            } catch (MissingPieciesException ex) {
                out.println("FATAL ERROR: Faltam alguns pacotes!");
                System.exit(0);
            }

            out.println("Pergunta " + pergunta + "!");
            out.println(p.getPergunta());
            out.println("1 - " + p.getResposta(1));
            out.println("2 - " + p.getResposta(2));
            out.println("3 - " + p.getResposta(3));
            in.nextLine();

            pergunta++;
        }

    }

}
