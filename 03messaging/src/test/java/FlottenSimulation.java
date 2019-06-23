import org.junit.Test;
import telematik.TelematikEinheit;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FlottenSimulation {
    private static Executor exec = Executors.newCachedThreadPool();

    @Test
    public void start (){
        //telematik.TelematikEinheit e1 = new telematik.TelematikEinheit();
        //telematik.TelematikEinheit e2 = new telematik.TelematikEinheit();
        //telematik.TelematikEinheit e3 = new telematik.TelematikEinheit();

        exec.execute(() -> TelematikEinheit.main(null));
        exec.execute(() -> TelematikEinheit.main(null));
        exec.execute(() -> TelematikEinheit.main(null));

    }
}
