import java.util.Arrays;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class cambiodinamica {

    public static void main(String[] args) {
        
        // Se inicializa una bandera para trazar la ejecución del algoritmo.
        boolean trazar = false;

        if(args.length == 0){
            // Datos de entrada locales.
            int numeroDeMonedasLocal = 3;
            int[] denominacionesLocal = {1, 3, 5};
            int cantidadLocal = 4;

            // Se llama al método darCambio para calcular la tabla dinámica.
            int[][] tabla = darCambio(cantidadLocal, denominacionesLocal, trazar);

            // Se obtiene la cantidad mínima de monedas necesarias.
            int cantidadMinimaLocal = tabla[denominacionesLocal.length][cantidadLocal];

            // Se obtiene el conjunto de monedas utilizadas.
            int[] monedasUtilizadasLocal = encontrarMonedasUtilizadas(denominacionesLocal, tabla);

            // Se imprime la cantidad mínima de monedas necesarias.
            System.out.println("Cantidad mínima de monedas: " + cantidadMinimaLocal);

            // Se imprime el conjunto de monedas utilizadas para alcanzar la cantidad mínima.
            System.out.println("Monedas utilizadas: " + Arrays.toString(monedasUtilizadasLocal));

            // Se llama al método para escribir la salida en el archivo.
            escribirArchivoSalida("archivo_salida.txt", cantidadMinimaLocal, monedasUtilizadasLocal);

        }

        // Verifica si se proporciona la opción de traza en la línea de comandos.
        if (args.length > 0) {
            // Si la opción de traza está presente, se activa la bandera y se ajustan los argumentos.
            trazar = args[0].equals("-t");
            if (trazar) {
                System.out.println("Modo de traza activado.");
                args = Arrays.copyOfRange(args, 1, args.length);
            }
        }

        // Verifica si se proporciona la opción de ayuda en la línea de comandos.
        if (args.length > 0 && args[0].equals("-h")) {
            // Si se solicita ayuda, se imprime la información de ayuda y se sale del programa.
            imprimirAyuda();
        } else {
            // Se llama al método leerArchivoEntrada para obtener los datos del archivo.
            DatosEntrada datosEntrada = leerArchivoEntrada("archivo_entrada.txt");

            // Se obtienen los datos necesarios del objeto DatosEntrada.
            int numeroDeMonedas = datosEntrada.getNumeroDeMonedas();
            int[] denominaciones = datosEntrada.getDenominaciones();
            int cantidad = datosEntrada.getCantidad();

            // Se llama al método darCambio para calcular la tabla dinámica.
            int[][] tabla = darCambio(cantidad, denominaciones, trazar);
        }

        //archivos de entrada y salida
        if (args.length == 2 && esNombreArchivoValido(args[0]) && esNombreArchivoValido(args[1])){
            // Se llama al método leerArchivoEntrada para obtener los datos del archivo.
            DatosEntrada datosEntrada = leerArchivoEntrada(args[0]);

            // Se obtienen los datos necesarios del objeto DatosEntrada.
            int numeroDeMonedas = datosEntrada.getNumeroDeMonedas();
            int[] denominaciones = datosEntrada.getDenominaciones();
            int cantidad = datosEntrada.getCantidad();

            // Se llama al método darCambio para calcular la tabla dinámica.
            int[][] tabla = darCambio(cantidad, denominaciones, trazar);

            // Se obtiene la cantidad mínima de monedas necesarias.
            int cantidadMinima = tabla[denominaciones.length][cantidad];

            // Se obtiene el conjunto de monedas utilizadas.
            int[] monedasUtilizadas = encontrarMonedasUtilizadas(denominaciones, tabla);

            // Se imprime la cantidad mínima de monedas necesarias.
            System.out.println("Cantidad mínima de monedas: " + cantidadMinima);

            // Se imprime el conjunto de monedas utilizadas para alcanzar la cantidad mínima.
            System.out.println("Monedas utilizadas: " + Arrays.toString(monedasUtilizadas));

            // Se llama al método para escribir la salida en el archivo.
            escribirArchivoSalida(args[1], cantidadMinima, monedasUtilizadas);
        }else if(args.length == 1 && esNombreArchivoValido(args[0])){
            // Se llama al método leerArchivoEntrada para obtener los datos del archivo.
            DatosEntrada datosEntrada = leerArchivoEntrada(args[0]);

            // Se obtienen los datos necesarios del objeto DatosEntrada.
            int numeroDeMonedas = datosEntrada.getNumeroDeMonedas();
            int[] denominaciones = datosEntrada.getDenominaciones();
            int cantidad = datosEntrada.getCantidad();

            // Se llama al método darCambio para calcular la tabla dinámica.
            int[][] tabla = darCambio(cantidad, denominaciones, trazar);

            // Se obtiene la cantidad mínima de monedas necesarias.
            int cantidadMinima = tabla[denominaciones.length][cantidad];

            // Se obtiene el conjunto de monedas utilizadas.
            int[] monedasUtilizadas = encontrarMonedasUtilizadas(denominaciones, tabla);

            // Se imprime la cantidad mínima de monedas necesarias.
            System.out.println("Cantidad mínima de monedas: " + cantidadMinima);

            // Se imprime el conjunto de monedas utilizadas para alcanzar la cantidad mínima.
            System.out.println("Monedas utilizadas: " + Arrays.toString(monedasUtilizadas));
        }
    }

    public static int[][] darCambio(int cantidad, int[] monedas, boolean trazar) {
        // Se obtiene la cantidad de tipos de monedas disponibles.
        int N = monedas.length;
        
        // Se crea una tabla para almacenar los resultados intermedios del algoritmo.
        int[][] tabla = new int[N + 1][cantidad + 1];

        // Inicialización de la primera columna de la tabla.
        for (int i = 1; i <= N; i++) {
            tabla[i][0] = 0;
        }

        // Llenado de la tabla usando programación dinámica.
        for (int j = 1; j <= cantidad; j++) {
            for (int i = 1; i <= N; i++) {
                // Caso especial: primera fila y la moneda actual es mayor que la cantidad.
                if (i == 1 && monedas[i - 1] > j) {
                    tabla[i][j] = Integer.MAX_VALUE;
                } else {
                    // Caso general:
                    if (i == 1) {
                        // Si es la primera fila, se calcula la cantidad de monedas necesarias.
                        tabla[i][j] = 1 + tabla[1][j - monedas[i - 1]];
                    } else {
                        // Si no es la primera fila:
                        if (j < monedas[i - 1]) {
                            // Si la cantidad actual es menor que la moneda actual, se copia el valor de la fila anterior.
                            tabla[i][j] = tabla[i - 1][j];
                        } else {
                            // Si no, se toma el mínimo entre el valor de la fila anterior y el valor correspondiente a restar
                            // la moneda actual a la cantidad más 1.
                            tabla[i][j] = Math.min(tabla[i - 1][j], tabla[i][j - monedas[i - 1]] + 1);
                        }
                    }
                }

                // Si se activa la traza, se imprime información sobre la tabla en cada iteración.
                if (trazar) {
                    trazarAlgoritmo(i, j, tabla[i][j]);
                }
            }
        }

        // Se retorna la tabla completa.
        return tabla;
    }

    // Método para leer el archivo de entrada.
    public static DatosEntrada leerArchivoEntrada(String nombreArchivo) {
        // Lee el archivo .txt en el try
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            int numeroDeMonedas = Integer.parseInt(br.readLine());
            int[] denominaciones = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int cantidad = Integer.parseInt(br.readLine());

            return new DatosEntrada(numeroDeMonedas, denominaciones, cantidad);
        // Lanza error en caso de no hacer match con el archivo de entrada
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Clase para almacenar los datos de entrada y usarlo como tipo.
    public static class DatosEntrada {
        private int numeroDeMonedas;
        private int[] denominaciones;
        private int cantidad;

        // Constructor de los datos de entrada
        public DatosEntrada(int numeroDeMonedas, int[] denominaciones, int cantidad) {
            this.numeroDeMonedas = numeroDeMonedas;
            this.denominaciones = denominaciones;
            this.cantidad = cantidad;
        }

        // Devuelve el número de monedas
        public int getNumeroDeMonedas() {
            return numeroDeMonedas;
        }

        // Devuelve la denominación de las monedas
        public int[] getDenominaciones() {
            return denominaciones;
        }

        //Devuelve la cantidad a calcular
        public int getCantidad() {
            return cantidad;
        }
    }

    // Método que imprime en pantalla la traza de las operaciones que se están haciendo
    public static void trazarAlgoritmo(int i, int j, int valor) {
        // Se imprime información detallada sobre la tabla para cada iteración si la traza está activada.
        System.out.println("Tabla[" + i + "][" + j + "] = " + valor);
    }

    //Algoritmo en programación dinámica que resuelve el problema original del ejercicio
    public static int[] encontrarMonedasUtilizadas(int[] denominaciones, int[][] tabla) {
        // Se obtiene el número total de monedas utilizadas para alcanzar la cantidad mínima.
        int N = denominaciones.length;
        int cantidad = tabla[N][tabla[0].length - 1];
        int[] monedas = new int[cantidad];

        int i = N, j = tabla[0].length - 1;

        while (i > 0 && j > 0) {
            if (j >= denominaciones[i - 1] && tabla[i][j] == tabla[i][j - denominaciones[i - 1]] + 1) {
                // Si la cantidad actual es mayor o igual a la denominación actual, y el valor en la tabla indica
                // que se ha utilizado una moneda, se registra la denominación y se ajustan las variables.
                monedas[cantidad - 1] = denominaciones[i - 1];
                cantidad--;
                j -= denominaciones[i - 1];
            } else {
                // Si no se ha utilizado la moneda, se mueve hacia arriba en la tabla.
                i--;
            }
        }

        // Se retorna el conjunto de monedas utilizadas.
        return monedas;
    }

    // Método que devuelve la ayuda para usar el programa en la terminal
    public static void imprimirAyuda() {
        // Se imprime información de ayuda sobre cómo utilizar el programa.
        System.out.println("SINTAXIS: cambiodinamica [-t][-h] [fichero entrada]");
        System.out.println("SINTAXIS: cambiodinamica [-t][-h] [fichero entrada] [fichero salida]");
        System.out.println("-t Traza el algoritmo");
        System.out.println("-h Muestra esta ayuda");
        System.out.println("[fichero entrada] Nombre del fichero de entrada");
        System.out.println("[fichero salida] Nombre del fichero de salida");
    }

    // Método que crea y escribe los resultados en el archivo de salida en formato .txt
    public static void escribirArchivoSalida(String nombreArchivo, int cantidadMinima, int[] monedasUtilizadas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            // Se escribe la cantidad mínima en la primera línea.
            bw.write(String.valueOf(cantidadMinima));
            bw.newLine();

            // Se escribe el conjunto de monedas utilizadas en la segunda línea.
            for (int i = 0; i < monedasUtilizadas.length; i++) {
                bw.write(String.valueOf(monedasUtilizadas[i]));
                if (i < monedasUtilizadas.length - 1) {
                    bw.write(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Método en regex que valida el nombre y formato del archivo en *.txt
    private static boolean esNombreArchivoValido(String nombreArchivo) {
        return nombreArchivo.matches(".+\\.txt");
    }
}
