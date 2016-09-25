import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class TestCore {
    private Escalonador escalonador;

    @Before
    public void setUp(){
        this.escalonador = new Escalonador();
    }
    @Test
    public void dummyTest(){
        Core core = new Core();
        assertEquals("hello", core.hello());
    }

    @Test
    public void testFilesList(){

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

        assertArrayEquals(lista , escalonador.getLista());
    }

    @Test
    public void testQuantumFileName(){
        assertEquals("quantum.txt", escalonador.getQuantumFileName());
    }

    @Test
    public void testLoadFiles(){
        assertEquals( escalonador.getReadyProcess().size() , 10 );
    }

    @Test
    public void testIsRightFileLoaded(){
        assertEquals( escalonador.getReadyProcess().get(0) , "TESTE-1\n" +
                "X=8\n" +
                "COM\n" +
                "COM\n" +
                "COM\n" +
                "E/S\n" +
                "Y=10\n" +
                "X=2\n" +
                "COM\n" +
                "E/S\n" +
                "SAIDA\n" );
    }


}
