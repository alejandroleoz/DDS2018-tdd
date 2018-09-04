package ar.edu.utn.dds;

import ar.edu.utn.dds.exception.BancoException;

public class BancoFacade {

    public Double getSaldo(String cbu) throws BancoException {
        // todo implementar (llamar a servicio externo. Si falla lanzazr BancoException)
        return 0D;
    }

    // devuelve el CBU
    public String getCuentaDefault(String idCliente) throws BancoException {
        // todo implementar (llamar a servicio externo. Si falla lanzazr BancoException)
        return "";
    }

    public Double extraer(String cbu, Double cantidadARetirar) throws BancoException {
        // todo implementar (llamar al servicio externo. Si falla lanzar BancoException)
        return 0D;
    }
}
