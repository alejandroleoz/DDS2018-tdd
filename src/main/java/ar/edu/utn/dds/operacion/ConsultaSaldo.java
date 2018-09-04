package ar.edu.utn.dds.operacion;

public class ConsultaSaldo implements Operacion {

    private String cbu;
    private Double saldo;


    public ConsultaSaldo(String cbu, Double saldo) {
        this.cbu = cbu;
        this.saldo = saldo;
    }

    public String getCbu() {
        return cbu;
    }

    public Double getSaldo() {
        return saldo;
    }
}
