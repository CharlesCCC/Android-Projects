public class HelloWorld {
    public static void main(String[] args) {
        int x = 1;
        int y = 5;
        x = y;

        if (x != 1) {
            y = x * y;
            x = x + 1;
        }
        else{
            x = x + 2;
        }

        if(y!=1){
            x = x+3;
        }


        for (int i = 0; i < 10; i++) {
            System.out.println("x = " + x);
        }

        System.out.println(x+y);
    }
}