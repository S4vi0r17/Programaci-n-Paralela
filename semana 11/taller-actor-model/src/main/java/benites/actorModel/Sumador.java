package benites.actorModel;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import scala.sys.Prop;

public class Sumador extends UntypedAbstractActor {

    private static final int N = 100000;
    private double[] arreglo;
    private ActorRef maestro;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        // System.out.println("Iniciando actor " + this.getSelf().path());
        instanciarArreglo();
        crearActores();
        maestro.tell(10, getSelf());
        maestro.tell(new Maestro.Mensaje(arreglo), getSelf());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Double) {
            System.out.println("El total es: " + (double) message);
            getContext().getSystem().terminate();
        }
    }

    private void crearActores() {
        maestro = getContext().actorOf(Props.create(Maestro.class), "maestro");
    }

    private void instanciarArreglo() {
        arreglo = new double[N];
        for (int i = 0; i < N; i++) {
            arreglo[i] = Math.random();
        }
    }
}
