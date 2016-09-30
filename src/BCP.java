import java.util.Arrays;
import java.util.List;

public class BCP {
    private int x = 0;
    private int y = 0;
    private int pc = -1;
    private int state = 0;
    private List<String> program;

    public BCP(String program) {
        this.program = this.digestProgram(program);
    }

    public List<String> digestProgram(String program) {
        return Arrays.asList(program.split("\n"));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<String> getProgram() {
        return program;
    }
}