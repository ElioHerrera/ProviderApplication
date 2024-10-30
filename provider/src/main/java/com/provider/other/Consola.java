package com.provider.other;

public class Consola {

    String mensaje;

    public static String textoNegro(){
        String negro = "\033[0;90m";
                return negro;
    }
    public static String textoRojo(){
        String amarillo = "\033[1;91m";
        return amarillo;
    }
    public static String textoVerde(){
        String verde = "\033[1;92m";
        return verde;
    }
    public static String textoAmarillo(){
        String amarillo = "\033[1;93m";
        return amarillo;
    }
    public static String textoAzul(){
        String amarillo = "\033[1;94m";
        return amarillo;
    }
    public static String textoRosa(){
        String rosa = "\033[1;95m";
        return rosa;
    }
    public static String textoCeleste(){
        String celeste = "\033[1;96m";
        return celeste;
    }
    public static String textoBlanco(){
        String blanco = "\033[1;97m";
        return blanco;
    }
    public static String finTexto(){
        String reset = "\033[0m";
        return reset;
    }
    public static void negro(String mensaje){
        System.out.println(textoNegro() + mensaje + finTexto());
    }
    public static void rojo(String mensaje){
        System.out.println(textoRojo() + mensaje + finTexto());
    }
    public static void verde(String mensaje){
        System.out.println(textoVerde() + mensaje + finTexto());
    }
    public static void amarillo(String mensaje){
        System.out.println(textoAmarillo() + mensaje + finTexto());
    }
    public static void azul(String mensaje){
        System.out.println(textoAzul() + mensaje + finTexto());
    }
    public static void rosa(String mensaje){
        System.out.println(textoRosa() + mensaje + finTexto());
    }
    public static void celeste(String mensaje){
        System.out.println(textoCeleste() + mensaje + finTexto());
    }
    public static void blanco(String mensaje){
        System.out.println(textoBlanco() + mensaje + finTexto());
    }
    public static void aletaVerde(String mensajeAlerta, String titulo, String mensaje){
        System.out.println("\033[0;102m\033[1;30m ¡ " + mensajeAlerta +" ! \033[0m\033[0;92m " + titulo + " \033[0m\033[1;35m " + mensaje + finTexto());
    }
    public static void aletaRoja(String mensajeAlerta, String titulo, String mensaje){
        System.out.println("\033[0;101m ¡ " + mensajeAlerta +" ! \033[0m\033[0;91m " + titulo + " \033[0m\033[1;35m " + mensaje + finTexto());
    }
    public static void aletaCeleste(String mensajeAlerta, String titulo, String mensaje){
        System.out.println("\033[0;106m\033[1;30m ¡ " + mensajeAlerta +" ! \033[0m\033[0;96m " + titulo + " \033[0m\033[1;35m " + mensaje + finTexto());
    }
}
