package ar.edu.utn.dds;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CajeroTest {

    private Cajero cajero;


    @Before
    public void before() {
        this.cajero = new Cajero();
    }

    @Test
    public void consultaDeSaldo() throws SesionException, BancoException {

        Double expectedSaldo = 25D;
        String expectedCBU = "123457656345658";

        // Mock banco
        BancoFacade bancoMock = mock(BancoFacade.class);
        when(bancoMock.getSaldo(anyString())).thenReturn(expectedSaldo);
        when(bancoMock.getCuentaDefault(anyString())).thenReturn(expectedCBU);

        // creo un spy del cajero para verificar llamadas a un metodo
        Cajero cajeroSpy = spy(cajero);

        // seteo el mock en el cajero
        cajeroSpy.setBanco(bancoMock);

        // ejecuto la consulta
        cajeroSpy.iniciarSesion();
        ConsultaSaldo consulta = cajeroSpy.consultarSaldo();
        cajeroSpy.finalizarSesion();

        // chequeo que el Cajero consulte el saldo del banco
        assertEquals(expectedCBU, consulta.getCbu());
        assertEquals(expectedSaldo, consulta.getSaldo());

        // chequeo la impresion del ticket
        verify(cajeroSpy, times(1)).imprimirComprobante(eq(consulta));
    }

    @Test(expected = SesionException.class)
    public void consultaDeSaldo_sinSesionAbierta() throws SesionException, BancoException {

        // ejecuto la consulta (debe lanzar exception ya que no hay sesion abierta)
        cajero.consultarSaldo();
    }

    @Test(expected = BancoException.class)
    public void consultaDeSaldo_bancoException() throws BancoException, SesionException {
        // Mock banco
        BancoFacade bancoMock = mock(BancoFacade.class);
        when(bancoMock.getSaldo(anyString())).thenThrow(new BancoException());
        when(bancoMock.getCuentaDefault(anyString())).thenThrow(new BancoException());

        // seteo el mock en el cajero
        cajero.setBanco(bancoMock);

        // ejecuto la consulta
        cajero.iniciarSesion();
        cajero.consultarSaldo();
    }

    @Test
    public void iniciarSesion() throws SesionException {
        cajero.iniciarSesion();
        assertTrue(cajero.haySesionAbierta());
    }

    @Test(expected = SesionException.class)
    public void iniciarSesion_haySesionAbierta() throws SesionException {

        // inicio una sesion
        cajero.iniciarSesion();

        // verifico que se inicio
        assertTrue(cajero.haySesionAbierta());

        // trato de iniciar sesion
        cajero.iniciarSesion();
    }

    @Test
    public void finalizarSesion() throws SesionException {
        // inicio
        cajero.iniciarSesion();

        // verifico que se inicio
        assertTrue(cajero.haySesionAbierta());

        // finalizo
        cajero.finalizarSesion();

        // verifico que se cerro
        assertTrue(cajero.haySesionAbierta());
    }

    @Test(expected = SesionException.class)
    public void finalizarSesion_sinSesion() throws SesionException {
        // finalizo
        cajero.finalizarSesion();
    }

}
