package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;
  private Movimiento movimiento;
  private Movimiento otroMovimiento;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    movimiento = new Movimiento(LocalDate.now(),1000,true);
    otroMovimiento = new Movimiento(LocalDate.now(),1200,false);
  }

  @Test
  void agregarMovimientoDepositoACuenta() {
    cuenta.poner(1550);
    movimiento.agregateA(cuenta);
    assertEquals(2550,cuenta.getSaldo());
  }

  @Test
  void agregarMovimientoExtraccionACuenta() {
    cuenta.poner(1550);
    otroMovimiento.agregateA(cuenta);
    assertEquals(350,cuenta.getSaldo());
  }

  @Test
  void movimientoDepositadoEnFechaErronea() {
    assertFalse(movimiento.fueDepositado(LocalDate.of(2022,3,23)));
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(1500,cuenta.getSaldo());
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(1500 + 456 + 1900,cuenta.getSaldo());
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

}