import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Escalonador {
    private final String[] lista = {
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
    private final String quantum_name = "quantum.txt";
    private ArrayList<String> readyProcessInput = new ArrayList<String>();
    private int quantum = 0;
    Queue<BCP> readyProcesses = new LinkedList<BCP>();
    Queue<BCP> blockedProcesses = new LinkedList<BCP>();
    LinkedList<BCP> tabelaProcesso = new LinkedList<BCP>();

    public Escalonador() {
        this.readFiles();
        this.readQuantum();

    }

    private void decrementPenalty(){
        LinkedList<BCP> temp = new LinkedList<BCP>();

        temp.addAll(blockedProcesses);

        for (BCP bcp: temp){
            bcp.reducePenalty();
            if (bcp.getPenalty() == 0){
                blockedProcesses.remove(bcp);
                readyProcesses.add(bcp);
            }
        }

    }


    public String[] getLista() {
        return lista;
    }

    public String getQuantumFileName() {
        return quantum_name;
    }

    static String readFile(String path, Charset encoding)
            throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);

    }

    private void readFiles() {
        int i = 0;

        for (String file_name : lista) {
            try {
                URL url = getClass().getResource(file_name);
                readyProcessInput.add(readFile(url.getPath(), Charset.defaultCharset()));
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readQuantum() {
        try {
            URL url = getClass().getResource(this.quantum_name);
            this.quantum = Integer.parseInt(readFile(url.getPath(), Charset.defaultCharset()).replace("\n", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getReadyProcessInput() {
        return readyProcessInput;
    }

    public int getQuantum() {
        return this.quantum;
    }

    public void run() {
        for (String string : this.readyProcessInput) {
            System.out.println("Carregando: " + string.split("\n")[0]);
            BCP bcp = new BCP(string);
            bcp.setPc(1);
            bcp.setState(0);
            readyProcesses.add(bcp);
            tabelaProcesso.add(bcp);
        }
        int num_instrucoes = 1;
        BCP bcp = readyProcesses.element();
        bcp.setState(1);

        loop:
        while (tabelaProcesso.size() != 0) {
            System.out.println("Executando " + bcp.getName());

            String instrucao = bcp.getCurrentInstruction();

            switch (instrucao) {
                case "COM":
                    num_instrucoes = num_instrucoes + 1;
                    //System.out.println("COM");
                    break;
                case "SAIDA":
                    num_instrucoes = 1;
                    readyProcesses.remove();
                    System.out.println(bcp.getName() + " Terminado. X=" + bcp.getX()+" Y=" + bcp.getY());
                    tabelaProcesso.remove(bcp);
                    decrementPenalty();

                    if(readyProcesses.size()>0)
                        bcp = readyProcesses.element();

                    continue loop;
                case "E/S":
                    decrementPenalty();
                    bcp.setState(2);
                    bcp.setPenalty();
                    bcp.incrementPC();
                    readyProcesses.remove();
                    blockedProcesses.add(bcp);
                    System.out.println("Interrompendo " + bcp.getName() + " apos " + num_instrucoes + " [E/S]");
                    num_instrucoes = 1;
                    bcp = readyProcesses.element();
                    //System.out.println("E/S");
                    continue loop;

                default:
                    String param = instrucao.split("=")[0];

                    if (param.equals("X")) {
                        bcp.setX(Integer.parseInt(instrucao.split("=")[1]));
                        num_instrucoes = num_instrucoes + 1;
                    }
                    if (param.equals("Y")) {
                        bcp.setY(Integer.parseInt(instrucao.split("=")[1]));
                        num_instrucoes = num_instrucoes + 1;
                    }
            }

            System.out.println(instrucao);
            bcp.incrementPC();
            if (num_instrucoes > quantum){
                decrementPenalty();
                //  pronto = 0, executando = 1, bloqueado = 2
                bcp.setState(0);
                readyProcesses.remove();
                num_instrucoes = 1;
                readyProcesses.add(bcp);
                System.out.println("Interrompendo " + bcp.getName() + " apos 3 instrucoes");
                bcp = readyProcesses.element();
                bcp.setState(1);


            }
            //break;
        }

        System.out.println("MEDIA DE TROCAS: " + "xx");
        System.out.println("MEDIA DE INSTRUCOES: " + "xx");
        System.out.println("Quantum: " + quantum);
    }
}
