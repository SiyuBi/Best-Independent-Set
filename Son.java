
public class Son extends Father{
    String name="aaaaaassdam";
    public void say_hi() {
        System.out.println("Hi, I am a son, my name is: "+this.name);
    }

    public static void main(String[] args) {Grand[] objs = new Grand[2];
        objs[0]=new Father();
        objs[1]=new Son();
        objs[0].say_hi();
        objs[1].say_hi();}
}
