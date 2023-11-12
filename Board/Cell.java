package Board;
import java.util.List;

//Represents a cell on a board
public class Cell {
    private List<Integer> coords;
    private Content content;
    private int num;

    //When any board makes a cell, it must assign coords, and a number to it
    public Cell(List<Integer> coords, int num) {
        this.coords = coords;
        this.num = num;
    }

    //Can get coords, but not set them
    public List<Integer> getCoords() {
        return coords;
    }

    //Can get num but not set it
    public int getNum() {
        return num;
    }

    //Can get content
    public Content getContent() {
        return content;
    }

    //Content is more dynamic, so it can be set
    public void setContent(Content content) {
        this.content = content;
    }

    //TEMPORARY
    @Override
    public String toString() {
        return "Cell{" +
                "coords=" + coords +
                ", content=" + content +
                ", num=" + num +
                '}';
    }
}