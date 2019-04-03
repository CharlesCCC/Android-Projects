public class FlowTest {

    public void main(){
        int x = 1;
        int y = 5;
        x = y;

        while (x != 1){
            y = x*y;
            x=x+1;
        }

    }
}
