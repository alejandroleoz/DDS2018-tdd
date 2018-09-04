package ar.edu.utn.dds.operacion;

public class Extraccion implements Operacion {

    private String cbu;
    private Double cantidadExtraida;
    private Double saldo;

    public Extraccion(String cbu, Double cantidadExtraida, Double saldo) {

        this.cbu = cbu;
        this.cantidadExtraida = cantidadExtraida;
        this.saldo = saldo;
    }

    public String getCbu() {
        return cbu;
    }

    public Double getCantidadExtraida() {
        return cantidadExtraida;
    }

    public Double getSaldo() {
        return saldo;
    }
}
