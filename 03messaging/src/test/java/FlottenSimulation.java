import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FlottenSimulation {
    private static Executor exec = Executors.newCachedThreadPool();

    @Test
    public void start (){
        //TelematikEinheit e1 = new TelematikEinheit();
        //TelematikEinheit e2 = new TelematikEinheit();
        //TelematikEinheit e3 = new TelematikEinheit();

        exec.execute(() -> TelematikEinheit.main(null));
        exec.execute(() -> TelematikEinheit.main(null));
        exec.execute(() -> TelematikEinheit.main(null));

    }
}
