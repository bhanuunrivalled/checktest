import java.util.stream.Stream;

public class DifferentObjectTypes {
  public static void main(String[] args) {
    String normalString = "Hello";
    String normalString2 = "Hello";
    Object[] obj = {new String[]{"a", "b", "c"}, new int[]{1, 2, 3}};
    // Breakpoint!
    String normalString3 = "Hello";

  }

}
