import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static jdk.nashorn.internal.runtime.JSType.toInteger;

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
            "10.txt"};
    private final String quantum_name = "quantum.txt";
    private ArrayList<String> readyProcess = new ArrayList<String>();
    private int quantum = 0;


    public Escalonador() {
        this.readFiles();
        this.readQuantum();

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
                readyProcess.add(readFile(url.getPath(), Charset.defaultCharset()));
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




    public ArrayList<String> getReadyProcess() {
        return readyProcess;
    }

    public int getQuantum() {
        return this.quantum;
    }
}
