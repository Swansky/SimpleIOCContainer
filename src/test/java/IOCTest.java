import fr.swansky.ioccontainer.SwansIOC;
import fr.swansky.swansAPI.exception.InstanceCreationException;

public class IOCTest {

    public static void main(String[] args) {
        try {
            SwansIOC swansIOC = SwansIOC.CreateIOC(SwansIOC.class);
            swansIOC.CreateIOC();

        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }
    }
}
