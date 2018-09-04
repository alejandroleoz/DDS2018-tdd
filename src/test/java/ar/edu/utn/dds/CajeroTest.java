package ar.edu.utn.dds;

import ar.edu.utn.dds.exception.BancoException;
import ar.edu.utn.dds.exception.DineroException;
import ar.edu.utn.dds.exception.SesionException;
import ar.edu.utn.dds.operacion.ConsultaSaldo;
import ar.edu.utn.dds.operacion.Extraccion;
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

    // ================== SALDO ==================================================
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

    // ================== SESION ==================================================
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


    // ================== EXTRACCION ==================================================
    @Test
    public void extraer() throws BancoException, DineroException, SesionException {
        Cajero cajero = spy(this.cajero);

        Double cantidadARetirar = 200D;
        Double expectedSaldo = 500D;
        String expectedCBU = "123456786";

        // Mockeando metodos
        when(cajero.leerCantidadARetirar()).thenReturn(cantidadARetirar);
        when(cajero.hayDineroDisponible(anyDouble())).thenReturn(true);

        // mockeando banco
        BancoFacade bancoMock = mock(BancoFacade.class);
        when(bancoMock.getSaldo(anyString())).thenReturn(expectedSaldo);
        when(bancoMock.getCuentaDefault(anyString())).thenReturn(expectedCBU);
        when(bancoMock.extraer(eq(expectedCBU), eq(cantidadARetirar))).thenReturn(expectedSaldo);
        cajero.setBanco(bancoMock);

        // llamar al metodo
        cajero.iniciarSesion();
        Extraccion operacion = cajero.extraer();
        cajero.finalizarSesion();

        // chequear la Operacion resultante
        assertEquals(expectedCBU, operacion.getCbu());
        assertEquals(expectedSaldo, operacion.getSaldo());
        assertEquals(cantidadARetirar, operacion.getCantidadExtraida());

        // 1 vez para ver si hay algo de dinero, 1 vez para ver si hay la cantidad necesaria
        verify(cajero, times(1)).hayDineroDisponible(eq(1D));
        verify(cajero, times(1)).hayDineroDisponible(eq(cantidadARetirar));
        verify(cajero, times(1)).leerCantidadARetirar();
        verify(cajero, times(1)).imprimirComprobante(eq(operacion));
    }

    @Test(expected = DineroException.class)
    public void extraer_noHayDinero() throws BancoException, DineroException, SesionException {
        Cajero cajero = spy(this.cajero);

        // Mockeando metodos
        when(cajero.hayDineroDisponible(anyDouble())).thenReturn(false);

        // llamar al metodo
        cajero.iniciarSesion();
        cajero.extraer();
        cajero.finalizarSesion();
    }

    @Test(expected = DineroException.class)
    public void extraer_noHayCantidadSolicitada() throws BancoException, DineroException, SesionException {
        Cajero cajero = spy(this.cajero);

        Double cantidadARetirar = 200D;

        // Mockeando metodos
        when(cajero.leerCantidadARetirar()).thenReturn(cantidadARetirar);
        when(cajero.hayDineroDisponible(eq(1D))).thenReturn(true);
        when(cajero.hayDineroDisponible(eq(cantidadARetirar))).thenReturn(false);

        // llamar al metodo
        cajero.iniciarSesion();
        Extraccion operacion = cajero.extraer();
        cajero.finalizarSesion();
    }

    @Test(expected = BancoException.class)
    public void extraer_errorBanco() throws BancoException, DineroException, SesionException {
        Cajero cajero = spy(this.cajero);

        Double cantidadARetirar = 200D;
        Double expectedSaldo = 500D;
        String expectedCBU = "123456786";

        // Mockeando metodos
        when(cajero.leerCantidadARetirar()).thenReturn(cantidadARetirar);
        when(cajero.hayDineroDisponible(anyDouble())).thenReturn(true);

        // mockeando banco
        BancoFacade bancoMock = mock(BancoFacade.class);
        when(bancoMock.getSaldo(anyString())).thenReturn(expectedSaldo);
        when(bancoMock.getCuentaDefault(anyString())).thenReturn(expectedCBU);
        when(bancoMock.extraer(eq(expectedCBU), eq(cantidadARetirar))).thenThrow(new BancoException());
        cajero.setBanco(bancoMock);

        // llamar al metodo
        cajero.iniciarSesion();
        cajero.extraer();
        cajero.finalizarSesion();
    }

    @Test(expected = SesionException.class)
    public void extraer_noHaySesion() throws BancoException, DineroException, SesionException {
        // llamar al metodo
        cajero.extraer();
    }


}
