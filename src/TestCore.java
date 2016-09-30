import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestCore {
    private Escalonador escalonador;

    @Before
    public void setUp() {
        this.escalonador = new Escalonador();
    }

    @Test
    public void dummyTest() {
        Core core = new Core();
        assertEquals("hello", core.hello());
    }

    @Test
    public void testFilesList() {

        String[] lista = {
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

        assertArrayEquals(lista, escalonador.getLista());
    }

    @Test
    public void testQuantumFileName() {
        assertEquals("quantum.txt", escalonador.getQuantumFileName());
    }

    @Test
    public void testQuantum() {
        assertTrue(escalonador.getQuantum() != 0);
    }


    @Test
    public void testLoadFiles() {
        assertEquals(escalonador.getReadyProcess().size(), 10);
    }

    @Test
    public void testIsRightFileLoaded() {
        assertEquals(escalonador.getReadyProcess().get(0), "TESTE-1\n" +
                "X=8\n" +
                "COM\n" +
                "COM\n" +
                "COM\n" +
                "E/S\n" +
                "Y=10\n" +
                "X=2\n" +
                "COM\n" +
                "E/S\n" +
                "SAIDA\n");
    }

    @Test
    public void testIsRightFileLoadedNotrepeated() {
        assertNotEquals(escalonador.getReadyProcess().get(1), "TESTE-1\n" +
                "X=8\n" +
                "COM\n" +
                "COM\n" +
                "COM\n" +
                "E/S\n" +
                "Y=10\n" +
                "X=2\n" +
                "COM\n" +
                "E/S\n" +
                "SAIDA\n");
    }

    @Test
    public void testIsSecondFileNotEmpty() {
        assertNotEquals(escalonador.getReadyProcess().get(1).length(), 0);
    }

    @Test
    public void testBCPConstruction() {
        BCP bcp = new BCP(escalonador.getReadyProcess().get(0));

        assertTrue(bcp.getX() == 0);
        assertTrue(bcp.getY() == 0);
        assertTrue(bcp.getPc() == -1);
        assertTrue(bcp.getState() == 0);
        assertTrue(bcp.getProgram().size() > 0);

    }

    @Test
    public void testBCPProgramDigest() {
        BCP bcp = new BCP(escalonador.getReadyProcess().get(0));
        String string = "1\n2\n3\n";
        List<String> list;
        list = bcp.digestProgram(string);
        assertTrue(list.size() == 3);
        assertEquals(list.get(0), "1");
        assertEquals(list.get(1), "2");
        assertEquals(list.get(2), "3");

    }


}
