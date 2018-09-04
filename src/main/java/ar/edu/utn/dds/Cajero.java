package ar.edu.utn.dds;

import ar.edu.utn.dds.exception.BancoException;
import ar.edu.utn.dds.exception.DineroException;
import ar.edu.utn.dds.exception.SesionException;
import ar.edu.utn.dds.operacion.ConsultaSaldo;
import ar.edu.utn.dds.operacion.Extraccion;
import ar.edu.utn.dds.operacion.Operacion;

public class Cajero {

    private BancoFacade banco;
    private Sesion sesion;

    /**
     * Realiza una consulta de saldo
     *
     * @return una {@link Operacion} de tipo {@link ConsultaSaldo} con la informacion solicitada
     */
    public ConsultaSaldo consultarSaldo() throws SesionException, BancoException {
        if (!this.haySesionAbierta()) {
            throw new SesionException("Abra una sesion antes de operar");
        }
        String cbu = banco.getCuentaDefault(sesion.getIdCliente());
        Double saldo = banco.getSaldo(cbu);
        ConsultaSaldo operacion = new ConsultaSaldo(cbu, saldo);
        this.imprimirComprobante(operacion);
        return operacion;
    }

    public Extraccion extraer() throws SesionException, DineroException, BancoException {
        if (!this.haySesionAbierta()) {
            throw new SesionException("Abra una sesion antes de operar");
        }

        // chequea que haya algo de dinero
        if (!hayDineroDisponible(1D)) {
            throw new DineroException("El cajero en este momento no entrega dinero");
        }

        Double cantidadARetirar = leerCantidadARetirar();

        // chequea que haya la cantidad suficiente
        if (!hayDineroDisponible(cantidadARetirar)) {
            throw new DineroException("El cajero en este momento no dispone de esa cantidad");
        }

        // realizar la transaccion
        String cbu = banco.getCuentaDefault(sesion.getIdCliente());
        Double saldo = banco.extraer(cbu, cantidadARetirar);

        // entregar el dinero
        entregarDinero(cantidadARetirar);

        // generar el comprobante de operacion
        Extraccion operacion = new Extraccion(cbu, cantidadARetirar, saldo);
        imprimirComprobante(operacion);

        return operacion;
    }

    /**
     * Inicia una sesion en el cajero
     *
     * @return
     */
    public void iniciarSesion() throws SesionException {
        if (haySesionAbierta()) {
            throw new SesionException("No se puede iniciar una nueva sesion");
        }
        this.sesion = new Sesion();
    }

    /**
     * Finaliza una sesion en el cajero
     */
    public void finalizarSesion() throws SesionException {
        if (!haySesionAbierta()) {
            throw new SesionException("No hay sesion abierta");
        }
        sesion.finalizar();
    }

    public BancoFacade getBanco() {
        return banco;
    }

    public void setBanco(BancoFacade banco) {
        this.banco = banco;
    }

    public Boolean haySesionAbierta() {
        return this.sesion != null;
    }

    public void imprimirComprobante(Operacion operacion) {
        // todo implementar
    }

    public Boolean hayDineroDisponible(Double cantidad) {
        // todo implementar
        return true;
    }

    Double leerCantidadARetirar() {
        // todo implementar
        return 0D;
    }

    void entregarDinero(Double cantidad) {
        // todo implementar
    }
}
