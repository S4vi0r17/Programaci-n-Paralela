package benites.actorModel;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class Maestro extends UntypedAbstractActor {

    public static class Mensaje {
        public double[] arreglo;

        public Mensaje(double[] arreglo) {
            this.arreglo = arreglo;
        }
    }

    private ActorRef sumador;
    private ActorRef[] esclavos;
    private double total = 0;
    private int respuesta = 0;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            sumador = getSender();
            System.out.println("Creando " + (int) message + " esclavos");
            crearEsclavos((int) message);
        }
        if (message instanceof Mensaje) {
            Mensaje m = (Mensaje) message;
            mensajearEsclavos(m);
        }
        if (message instanceof Double) {
            respuesta += 1;
            total += (double) message;
            if (respuesta == esclavos.length) {
                sumador.tell(total, getSelf());
            }
        }
    }

    private void crearEsclavos(int N) {
        esclavos = new ActorRef[N];
        for (int i = 0; i < esclavos.length; i++) {
            esclavos[i] = getContext().actorOf(Props.create(Esclavo.class));
        }
    }

    private void mensajearEsclavos(Mensaje m) {
        int pedazito = m.arreglo.length / esclavos.length;
        for (int i = 0; i < esclavos.length; i++) {
            Esclavo.Mensaje mensaje = new Esclavo.Mensaje(m.arreglo, i * pedazito, (i + 1) * pedazito);
            esclavos[i].tell(mensaje, getSelf());
        }
    }
}
