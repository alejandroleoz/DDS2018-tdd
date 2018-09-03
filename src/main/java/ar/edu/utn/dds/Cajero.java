package ar.edu.utn.dds;

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
}
