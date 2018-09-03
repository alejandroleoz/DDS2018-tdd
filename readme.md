
<!-- test -->
[![Build Status](https://travis-ci.org/alejandroleoz/DDS2018-tdd.svg?branch=develop)](https://travis-ci.org/alejandroleoz/DDS2018-tdd)


<!-- coverage -->
[![codecov](https://codecov.io/gh/alejandroleoz/DDS2018-tdd/branch/develop/graph/badge.svg)](https://codecov.io/gh/alejandroleoz/DDS2018-tdd)

# TDD

Ejemplo de TDD para la materia "Diseño de Sistemas" (UTN FRBA - 2018)

## Sistema: Cajero Automático

#### Sesión
- Una sesión se inicia cuando la tarjeta es leida.
- Finaliza al expulsar la tarjeta
      
#### Login
- La validación del PIN se hace mediante un sistema de autenticación externo    

#### Operaciones disponibles

- Extracción rápida
    1) Chequear si el cajero tiene dinero disponible
    2) Se solicita el importe a retirar
    3) Chequear que el cajero tenga esa cantidad disponible
    4) Intentar hacer la transacción con el sistema del banco (sistema externo)
    5) Entregar el dinero
    6) Imprime el comprobante
    7) fin de la operación

- Depósito
    1) Ingresar la cuenta de destino (CBU)
    2) Ingresar el importe a depositar
    3) Imprimir el ticket para el sobre
    4) Ingresar el sobre con el dinero y ticket al cajero
    5) Se imprime el comprobante
    6) fin de la operación

- Consulta de saldo
    1) Se solicita el saldo al sistema del banco (externo)
    2) Se imprime ticket
    3) fin de la operación
    
  