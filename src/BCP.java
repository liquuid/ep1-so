import java.util.Arrays;
import java.util.List;
import java.util.jar.Pack200;

public class BCP {
    private int x = 0;
    private int y = 0;
    private int pc = 0;
    private int penalty = 0;
    //  pronto = 0, executando = 1, bloqueado = 2
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

    public String getCurrentInstruction(){
        return program.get(pc);
    }

    public void incrementPC(){
        pc = pc + 1;
        //System.out.println("incrementando PC " + pc );
    }

    public String getName() {
        return program.get(0);
    }


    public void setState(int state) {
        this.state = state;
    }

    public List<String> getProgram() {
        return program;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty() {
        this.penalty = 2;
    }

    public void reducePenalty(){
        if (penalty > 0) {
            penalty = penalty - 1;
        } else {
            penalty = 0;
        }
    }

}