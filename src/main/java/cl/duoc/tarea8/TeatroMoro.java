/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package cl.duoc.tarea8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Nataly Chacón
 */
public class TeatroMoro {

    static double DESCUENTO_ESTUDIANTE = 0.9;
    static double DESCUENTO_TERCERA_EDAD = 0.85;
    static String[] opcionesMenuPrincipal = {"Venta de entradas","Promociones","Busqueda de Entradas", "Eliminar Entrada", "Ver Asientos del Teatro", "Ventas Realizas", "Imprimir Boleta", "Salir"};
    static String[] tiposEntrada = {"VIP","Platea","General"};
    static int[] cantidadAsientos = {10,15,20};
    static List reservasVip = new ArrayList();
    static List reservasPlatea = new ArrayList();
    static List reservasGeneral = new ArrayList();
    static int[] tarifaPublicoGeneral = {25000,19000,7200};
    static String[] promociones = {"Tercerta Edad 15% descuento","Estudiante 10% descuento"};
    static Scanner leer = new Scanner(System.in);
    static List entradasCompradas = new ArrayList();
    
    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    public static String ingresoCliente(){
        String nombreCliente = "";
        String rutCliente = "";
        boolean ingresoNombreValido = false;
        boolean rutClienteValido = false;
        leer.reset();
        System.out.println("\r\rIngrese el Nombre del Cliente o escriba S para salir:");
        do {
            nombreCliente = leer.nextLine();
            if(nombreCliente!=null && nombreCliente.equals("S")){
                return null;
            }
            ingresoNombreValido = nombreCliente!=null && !nombreCliente.trim().equals("");
            if(!ingresoNombreValido){
                System.out.println("Debe ingresar un nombre o S para salir");
            }
        } while(!ingresoNombreValido);
        System.out.println("Ingrese el Rut del Cliente o S para salir:");
        do {
            rutCliente = leer.nextLine();
            if(rutCliente!=null && rutCliente.equals("S")){
                return null;
            }
            rutClienteValido = rutCliente!=null && !rutCliente.trim().equals("") && validarRut(rutCliente);
            if(!rutClienteValido){
                System.out.println("Debe ingresar un rut valido o S para salir");
            }
        } while(!rutClienteValido);        
        
        return "Rut: " + rutCliente + " Nombre: " + nombreCliente;
    }
    
    public static void promociones(){
        System.out.println("\r\rPromociones disponibles:");
        for(int i=0;i<promociones.length;i++){
            System.out.println(promociones[i]);
        }
        System.out.println("\r\r");
    }
    
    public static Boolean estaDisponibleAsiento(String tipoEntrada, int numeroAsiento){
         if(tipoEntrada.equals(tiposEntrada[0])){
            return !reservasVip.contains(numeroAsiento);
        } else if(tipoEntrada.equals(tiposEntrada[1])){
            return !reservasPlatea.contains(numeroAsiento);
        }  else if(tipoEntrada.equals(tiposEntrada[2])){
            return !reservasGeneral.contains(numeroAsiento);
        } 
        return Boolean.FALSE;
    }


    public static void reservarEntrada(String tipoEntrada, int numeroAsiento){
         if(tipoEntrada.equals(tiposEntrada[0])){
            reservasVip.add(numeroAsiento);
        } else if(tipoEntrada.equals(tiposEntrada[1])){
            reservasPlatea.add(numeroAsiento);
        }  else if(tipoEntrada.equals(tiposEntrada[2])){
            reservasGeneral.add(numeroAsiento);
        } 
    }

    public static String buscarEntrada(Boolean devolverAsiento){
        
        System.out.println("\r\rIngrese el tipo de la entrada que desea: (VIP, Platea, General)");

        //Lectura del tipo de entrada
        Boolean tipoEntradaCorrecta = Boolean.FALSE;
        String tipoEntrada= null;
        do{
        tipoEntrada= leer.nextLine();    
        tipoEntradaCorrecta = (tipoEntrada!=null && (tipoEntrada.equals(tiposEntrada[0]) || tipoEntrada.equals(tiposEntrada[1]) || tipoEntrada.equals(tiposEntrada[2])));
        if(!tipoEntradaCorrecta){
            System.out.println("Tipo de entrada incorrecta. Ingrese VIP, Platea o General");
        }
        } while(!tipoEntradaCorrecta);
        //Mostrar entradas disponibles
        List reservas = null;
        int totalAsientos = 0;
        switch(tipoEntrada){
            case "VIP":
                reservas = reservasVip;
                totalAsientos = cantidadAsientos[0];
                break;
            case "Platea":  
                reservas = reservasPlatea;
                totalAsientos = cantidadAsientos[1];
                break;
            case "General":  
                reservas = reservasGeneral;
                totalAsientos = cantidadAsientos[2];
                break;
            default:
                //Este caso no se dadebido a la validacion previa
                break;
        }
        
        if(reservas.size()==totalAsientos){
          System.out.print("No quedan disponibles asientos de categoria: " + tipoEntrada);
          return buscarEntrada(devolverAsiento);
        } else {
          List asientosDisponibles = new ArrayList();
          System.out.print("Asientos "+ tipoEntrada +" disponibles: ");
          for(int i=0;i<totalAsientos;i++){
               if(estaDisponibleAsiento(tipoEntrada, i+1)){
                  asientosDisponibles.add(i+1);
                  System.out.print("\t"+ (i+1) + "\t");
               }
          }
          if(!devolverAsiento){
             //Es solo buscar asientos disponibles y no seleccionar uno
              return null;
          } else {
              String asientoSeleccionado = null;
              Boolean asientoCorrecto = Boolean.TRUE;
              do {
                System.out.println("\r\rIngrese el numero de asiento o un S para salir");
                asientoSeleccionado = leer.nextLine();
                asientoCorrecto = (esEntero(asientoSeleccionado) && asientosDisponibles.contains(Integer.valueOf(asientoSeleccionado)));
                if(asientoSeleccionado.equals("S")){
                    //Se sale sin seleccionar un asiento
                    return null;
                }
                if(!asientoCorrecto){
                  System.out.println("Asiento incorrecto, ingreso el numero de asiento");
                }
              } while(!asientoCorrecto);
          
              return tipoEntrada + ";" + asientoSeleccionado;  
          }
        }
    }
    
    public static void eliminarEntrada(){
        
        System.out.println("\r\rIngrese el tipo de la entrada que desea eliminar: (VIP, Platea, General)");

        //Lectura del tipo de entrada
        Boolean tipoEntradaCorrecta = Boolean.FALSE;
        String tipoEntrada= null;
        do{
        tipoEntrada= leer.nextLine();    
        tipoEntradaCorrecta = (tipoEntrada!=null && (tipoEntrada.equals(tiposEntrada[0]) || tipoEntrada.equals(tiposEntrada[1]) || tipoEntrada.equals(tiposEntrada[2])));
        if(!tipoEntradaCorrecta){
            System.out.println("Tipo de entrada incorrecta. Ingrese VIP, Platea o General");
        }
        } while(!tipoEntradaCorrecta);
        //Mostrar entradas disponibles
        List reservas = null;
        switch(tipoEntrada){
            case "VIP":
                reservas = reservasVip;
                break;
            case "Platea":  
                reservas = reservasPlatea;
                break;
            case "General":  
                reservas = reservasGeneral;
                break;
            default:
                //Este caso no se dadebido a la validacion previa
                break;
        }
        
        String asientoSeleccionado = null;
        Boolean asientoCorrecto = Boolean.TRUE;
        do {
            System.out.println("\r\rIngrese el numero de asiento a eliminar o un S para salir");
            asientoSeleccionado = leer.nextLine();
            asientoCorrecto = (esEntero(asientoSeleccionado) && reservas.contains(Integer.valueOf(asientoSeleccionado)));
            if(asientoCorrecto!=null && asientoCorrecto.equals("S")){
            return;
            }    
            if(!asientoCorrecto){
              System.out.println("Asiento incorrecto, ingreso el numero de asiento a eliminar reserva");
            } else {
              System.out.println("Reserva eliminada");
              reservas.remove(reservas.indexOf(Integer.valueOf(asientoSeleccionado)));
              eliminarCompra(tipoEntrada, asientoSeleccionado);
            }
        } while(!asientoCorrecto);        
    }
    
    
    public static void imprimirBoleta(String tipoEntrada, String asientoSeleccionado){
        if(tipoEntrada==null && asientoSeleccionado==null){
        System.out.println("\r\rIngrese el tipo de la entrada: (VIP, Platea, General)");

        //Lectura del tipo de entrada
        Boolean tipoEntradaCorrecta = Boolean.FALSE;
        tipoEntrada= null;
        do{
        tipoEntrada= leer.nextLine();    
        tipoEntradaCorrecta = (tipoEntrada!=null && (tipoEntrada.equals(tiposEntrada[0]) || tipoEntrada.equals(tiposEntrada[1]) || tipoEntrada.equals(tiposEntrada[2])));
        if(!tipoEntradaCorrecta){
            System.out.println("Tipo de entrada incorrecta. Ingrese VIP, Platea o General");
        }
        } while(!tipoEntradaCorrecta);
        
        asientoSeleccionado = null;
        Boolean asientoCorrecto = Boolean.TRUE;
        do {
            System.out.println("\r\rIngrese el numero de asiento o un S para salir");
            asientoSeleccionado = leer.nextLine();
            asientoCorrecto = (esEntero(asientoSeleccionado) && !estaDisponibleAsiento(tipoEntrada, Integer.valueOf(asientoSeleccionado)));
                
            if(!asientoCorrecto){
              System.out.println("Asiento incorrecto o sin compra asociada");
            }
            if(asientoSeleccionado!=null && asientoSeleccionado.equals("S")){
            return;
            }
        } while(!asientoCorrecto);
        }
        int indiceCompra = buscarIndiceCompra(tipoEntrada, asientoSeleccionado);
        if(indiceCompra!=-1){
         String[] entrada = (String[])entradasCompradas.get(indiceCompra);
         System.out.println("\r\r");
         System.out.println("-------------------------------------------");
         System.out.println("               Teatro Moro                 ");
         System.out.println("-------------------------------------------");
         
         System.out.println("Cliente " + entrada[5]);
         System.out.println("Ubicación " + entrada[0]);
         System.out.println("Costo base: $" + entrada[2]);
         System.out.println("Descuento aplicado: " + entrada[3] + "%");
         System.out.println("Costo final: $" + entrada[4]);
         System.out.println("Asiento: "+ entrada[1]);
         System.out.println("-------------------------------------------");
         System.out.println("Gracias por su visita al Teatro Moro");
         System.out.println("-------------------------------------------");         
         System.out.println("\r\r");
        }
        
        
    }    
    
    public static int buscarIndiceCompra(String tipoEntrada, String numeroAsiento){
     int indiceCompra = -1;
     for(int i=0;i<entradasCompradas.size();i++){
         String[] entrada = (String[])entradasCompradas.get(i); 
            if(entrada[0].equals(tipoEntrada) && entrada[1].equals(numeroAsiento)){
            indiceCompra = i;
            break;
         }
     }
     if(indiceCompra==-1){
     System.out.println("Tipo entrada " + tipoEntrada + " asiento " + numeroAsiento + " no ha sido comprada");
     }
     return indiceCompra;
    }
    
    public static void eliminarCompra(String tipoEntrada, String numeroAsiento){
        int compraAEliminar = -1;
     for(int i=0;i<entradasCompradas.size();i++){
         String[] entrada = (String[])entradasCompradas.get(i); 
            if(entrada[0].equals(tipoEntrada) && entrada[1].equals(numeroAsiento)){
            compraAEliminar = i;
            break;
         }
     }
     if(compraAEliminar==-1){
     System.out.println("Tipo entrada " + tipoEntrada + " asiento " + numeroAsiento + " no ha sido comprada");
     } else {
        System.out.println("Compra de tipo entrada " + tipoEntrada + " asiento " + numeroAsiento + " por un monto de " + ((String[])entradasCompradas.get(compraAEliminar))[4]  +"  ha sido eliminada");
        entradasCompradas.remove(compraAEliminar);
     }
    }
    
    public static void mostrarCompras(){
         double totalRecaudado = 0d;
         System.out.println("\r\r");
         if(entradasCompradas!=null && entradasCompradas.size()>0){
             System.out.println("Entradas vendidas: ");
         }
         for(int i=0;i<entradasCompradas.size();i++){
            String[] entrada = (String[])entradasCompradas.get(i); 
            System.out.println("Entrada " + entrada[0] + " asiento "+ entrada[1] + " precio base " + entrada[2] + " descuento aplicado " + entrada[3] + " precio pagado " + entrada[4] + " cliente " + entrada[5]);
            totalRecaudado+=Double.valueOf(entrada[4]);
         }
         if(totalRecaudado==0d){
         System.out.println("No se han comprado entradas ");
         } else {
         System.out.println("Total Recaudado " + totalRecaudado);
         }
         System.out.println("\r\r");
         
     }
    
    
    public static double obtenerTarifa(int posicionTipoEntradaAComprar, boolean esEstudiante, boolean esTerceraEdad){
     int tarifa = tarifaPublicoGeneral[posicionTipoEntradaAComprar];
     if(esTerceraEdad){
      return tarifa * DESCUENTO_TERCERA_EDAD; // 15% de descuento
     } else if (esEstudiante){
         return tarifa * DESCUENTO_ESTUDIANTE; //10% de descuento
     }
     return tarifa;
    }
       
    public static void imprimirAsientos(){
        System.out.print("\r\rAsientos del Teatro Moro: (D)-Disponible (ND)-No Disponible");
        for(int i = 0; i< tiposEntrada.length; i++){                        
            System.out.println("\r\r" +tiposEntrada[i]);
            for(int j = 0; j< cantidadAsientos[i]; j++){
                Boolean disponible = estaDisponibleAsiento(tiposEntrada[i], j+1);
                String estaDisponible = "(D)";
                if(!disponible){
                    estaDisponible = "(ND)";
                }
                System.out.print("\t" + "Asiento_" + (j+1) + " " + estaDisponible);
            }
            
        }
    }
    
    public static boolean esEntero(String numero){
     try{
        Integer.valueOf(numero);
        return true;
     }
     catch(Exception e){
        System.out.println("Ingrese numero entero");
        return false;
     }
    }
    
    public static void comprarEntrada(){
        String asiento = buscarEntrada(Boolean.TRUE);
        if(asiento!=null){
            
            String edad = "";
            Boolean esTerceraEdad = Boolean.FALSE;
            System.out.println("Ingrese su edad:");        
            do {
                edad = leer.nextLine();
            }
            while(!esEntero(edad));   
            
            double tarifaACobrar = 0;
            esTerceraEdad = Integer.valueOf(edad)>=65;
            boolean esEstudiante = false;
            String tipoEntrada = asiento.split(";")[0];
            String numeroAsiento = asiento.split(";")[1];
            int posicionTipoEntradaAComprar = Arrays.asList(tiposEntrada).indexOf(tipoEntrada);
            if(!esTerceraEdad){ // Se consulta si es estudiante solo si no es tercera edad, considerando tercera edad mas de 65 años
                System.out.println("¿Es estudiante? si o no");            
                Boolean continuarCiclo = true;
                
                do {
                    String respuestaEsEstudiante = leer.nextLine();
                    if(respuestaEsEstudiante.equals("si")){
                        tarifaACobrar += obtenerTarifa(posicionTipoEntradaAComprar, true, esTerceraEdad);
                        continuarCiclo = false;
                        esEstudiante = true;
                    } else if(respuestaEsEstudiante.equals("no")) {
                        tarifaACobrar += obtenerTarifa(posicionTipoEntradaAComprar, false, esTerceraEdad);
                        continuarCiclo = false;
                        esEstudiante = false;
                    } else {
                        System.out.println("Debe escribir si o no");
                        continuarCiclo = true;
                        esEstudiante = false;
                    }
                } while(continuarCiclo);
            } else {
                tarifaACobrar += obtenerTarifa(posicionTipoEntradaAComprar, false, esTerceraEdad);
            }
            String cliente = ingresoCliente();
            if(cliente==null){
                return;
            }
            reservarEntrada(tipoEntrada, Integer.valueOf(numeroAsiento));
            int precioBase = tarifaPublicoGeneral[posicionTipoEntradaAComprar];
            String descuento = "0";
            if(esTerceraEdad){
                descuento = "15";
            }
            else if(esEstudiante){
                descuento = "10";
            }   
            
            String[] entrada={tipoEntrada, numeroAsiento, String.valueOf(precioBase), descuento, String.valueOf(tarifaACobrar), cliente};
            entradasCompradas.add(entrada);
            imprimirBoleta(tipoEntrada, numeroAsiento);
        }
    }
    
    
    public static void main(String[] args) {
        
        boolean mostrarMenu = true;
        String opcionMenuPrincipal = null;
        do {
            opcionMenuPrincipal = null;
            if(mostrarMenu){
                System.out.println("\r");            
                System.out.println("...::: Menu :::...");
                for(int i=0; i<opcionesMenuPrincipal.length;i++){
                System.out.println((i+1) + ") " + opcionesMenuPrincipal[i]);
                }
            }
            System.out.println("Ingrese el numero de su opcion de menú");
            opcionMenuPrincipal = leer.nextLine();
            if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("1")){
                comprarEntrada();
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("2")){
                promociones();
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("3")){
                buscarEntrada(Boolean.FALSE);
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("4")){
                eliminarEntrada();
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("5")){
                imprimirAsientos();
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("6")){
                mostrarCompras();
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && opcionMenuPrincipal.equals("7")){
                imprimirBoleta(null, null);
                mostrarMenu = true;
            }
            else if(opcionMenuPrincipal!=null && !opcionMenuPrincipal.equals("8")){
                System.out.println("Debe ingresar 1,2,3,4,5,6,7 o 8");
                mostrarMenu = false;
            } 
        }
        while(!opcionMenuPrincipal.equals("8"));//Se mantiene en el menu principal mientra no seleccione opcion 2) Salir
    
    }
}
