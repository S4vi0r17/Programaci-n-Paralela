package benites.actorModel;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

public class Esclavo extends UntypedAbstractActor {

    public static class Mensaje {
        public double[] arreglo;
        public int inicio;
        public int fin;

        public Mensaje(double[] arreglo, int inicio, int fin) {
            this.arreglo = arreglo;
            this.inicio = inicio;
            this.fin = fin;
        }
    }

    private ActorRef maestro;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Mensaje) {
            Mensaje m = (Mensaje) message;
            System.out.println("esclavo: " + getSelf().path());
            double t = 0;
            for (int i = m.inicio; i < m.fin; i++) {
                t += m.arreglo[i];
            }
            getSender().tell(t, getSelf());
        }
    }
}
