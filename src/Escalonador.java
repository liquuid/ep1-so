import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Escalonador {
    static private final String[] lista = {
            "01.txt",
            "02.txt",
            "03.txt",
            "04.txt",
            "05.txt",
            "06.txt",
            "07.txt",
            "08.txt",
            "09.txt",
            "10.txt"
    };
    static private final String quantum_name = "quantum.txt";
    static private ArrayList<String> readyProcessInput = new ArrayList<String>();
    static private int quantum = 0;
    static Queue<BCP> readyProcesses = new LinkedList<BCP>();
    static Queue<BCP> blockedProcesses = new LinkedList<BCP>();
    static LinkedList<BCP> tabelaProcesso = new LinkedList<BCP>();
    static int contadorTrocas = 0;
    static int contadorInstrucoes = 0;
    static double mediaInstrucoes =0.0;
    static int totalInstrucoes = 0;

    public Escalonador() {
        this.readFiles();
        this.readQuantum();

    }

    private static void decrementPenalty() {
        LinkedList<BCP> temp = new LinkedList<BCP>();

        temp.addAll(blockedProcesses);

        for (BCP bcp : temp) {
            bcp.reducePenalty();
            if (bcp.getPenalty() == 0) {
                blockedProcesses.remove(bcp);
                readyProcesses.add(bcp);
            }
        }

    }


    static public String[] getLista() {
        return lista;
    }

    static public String getQuantumFileName() {
        return quantum_name;
    }

    static String readFile(String path, Charset encoding)
            throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);

    }

    private static void readFiles() {
        int i = 0;

        for (String file_name : lista) {
            try {
                //URL url = Escalonador.class.getResource(file_name);
                readyProcessInput.add(readFile("processos/" + file_name , Charset.defaultCharset()));
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readQuantum() {
        try {
           // URL url = "processos/quantum.txt";  //Escalonador.class.getResource(quantum_name);
            quantum = Integer.parseInt(readFile("processos/quantum.txt", Charset.defaultCharset()).replace("\n", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static public ArrayList<String> getReadyProcessInput() {
        return readyProcessInput;
    }

    public int getQuantum() {
        return quantum;
    }

    public static void main(String[] args) {
        readFiles();
        readQuantum();

        FileWriter arq = null;
        try {
            arq = new FileWriter("logfile-" + quantum + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter logWrite = new PrintWriter(arq);

        //logWrite.printf("+--Resultado--+%n");
        //for (i=1; i<=10; i++) {
        ////}
        //logWrite.printf("+-------------+%n");


        for (String string : readyProcessInput) {
            logWrite.println("Carregando: " + string.split("\n")[0]);
            BCP bcp = new BCP(string);
            bcp.setPc(1);
            bcp.setState(0);
            readyProcesses.add(bcp);
            tabelaProcesso.add(bcp);
        }
        int num_instrucoes = 1;
        BCP bcp = readyProcesses.element();
        bcp.setState(1);
        logWrite.println("Executando " + bcp.getName());

        loop:
        while (tabelaProcesso.size() != 0) {
            //System.out.println("Executando " + bcp.getName());

            String instrucao = bcp.getCurrentInstruction();

            switch (instrucao) {
                case "COM":
                    num_instrucoes = num_instrucoes + 1;
                    contadorInstrucoes += 1;
                    break;
                case "SAIDA":
                    num_instrucoes = 1;
                    if (readyProcesses.size() > 0) {
                        readyProcesses.remove();
                    }
                    logWrite.println(bcp.getName() + " Terminado. X=" + bcp.getX() + " Y=" + bcp.getY());
                    tabelaProcesso.remove(bcp);
                    decrementPenalty();

                    if (readyProcesses.size() > 0) {
                        bcp = readyProcesses.element();
                        logWrite.println("Executando " + bcp.getName());
                    }
                    contadorTrocas += 1;
                    contadorInstrucoes += 1;
                    continue loop;
                case "E/S":
                    decrementPenalty();
                    bcp.setState(2);
                    bcp.setPenalty();
                    bcp.incrementPC();
                    readyProcesses.remove();
                    blockedProcesses.add(bcp);
                    logWrite.println("Iniciando E/S de " + bcp.getName());
                    mediaInstrucoes = mediaInstrucoes + num_instrucoes;
                    num_instrucoes = 1;
                    if (readyProcesses.size() > 0) {
                        bcp = readyProcesses.element();
                        continue loop;
                    }
                    logWrite.println("Executando " + bcp.getName());
                    contadorInstrucoes += 1;
                    contadorTrocas += 1;
                    continue loop;

                default:
                    String param = instrucao.split("=")[0];
                    contadorInstrucoes += 1;
                    if (param.equals("X")) {
                        bcp.setX(Integer.parseInt(instrucao.split("=")[1]));
                        num_instrucoes = num_instrucoes + 1;
                    }
                    if (param.equals("Y")) {
                        bcp.setY(Integer.parseInt(instrucao.split("=")[1]));
                        num_instrucoes = num_instrucoes + 1;
                    }
            }

            bcp.incrementPC();
            if (num_instrucoes >= quantum ) {
                decrementPenalty();
                //  pronto = 0, executando = 1, bloqueado = 2
                bcp.setState(0);
                readyProcesses.remove();
                mediaInstrucoes = mediaInstrucoes + num_instrucoes;

                num_instrucoes = 1;
                readyProcesses.add(bcp);
                logWrite.println("Interrompendo " + bcp.getName() + " apos 3 instrucoes");
                bcp = readyProcesses.element();
                bcp.setState(1);
                logWrite.println("Executando " + bcp.getName());
                contadorTrocas += 1;


            }
            totalInstrucoes += 1;
        }

        logWrite.println("MEDIA DE TROCAS: " + contadorTrocas/10.0);
        logWrite.println("MEDIA DE INSTRUCOES: " + mediaInstrucoes/contadorTrocas);

        logWrite.println("Quantum: " + quantum);
        try {
            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
