package com.mycompany.crudhibernatecomandos;

import java.util.ArrayList;
import java.util.Scanner;
import models.Carta;
import models.Pedidos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author chris
 */
public class CrudHibernate {

    private static SessionFactory sf = new Configuration().configure().buildSessionFactory();
    private static Session s = sf.openSession();
    
    public static void main(String[] args) {
        
        int opcion = 0;
        Scanner sc = new Scanner(System.in);
        while(opcion!=6) {
            System.out.println("---------------------");
            System.out.println("1.- Crear pedido");
            System.out.println("2.- Eliminar pedido");
            System.out.println("3.- Marcar pedido como recogido");
            System.out.println("4.- Listar pedidos pendientes");
            System.out.println("5.- Listar carta");
            System.out.println("6.- Salir");
            System.out.println("--------------------");
            opcion = sc.nextInt();
            switch (opcion){
            
                case 1: listarCarta();
                        crearPedido();
                break;
                case 2: listarPedidos();
                        eliminarPedido();
                break;
                case 3: listarPendientes();
                        marcarRecogido();
                break;
                case 4: listarPendientes();
                break;
                case 5: listarCarta();
                break;
                case 6: System.out.println("¡Esperemos que haya disfrutado de nuestro servicio!");
                break;
                default: System.out.println("Opción incorrecta, elija otra opción");
            }
        }
    }
    
    public static void crearPedido() {
        
        Pedidos p = new Pedidos();
        
        //Hago escaneres para meter los datos
        String cliente;
        Scanner sc = new Scanner(System.in);
        System.out.println("Escriba su nombre: ");
        cliente = sc.nextLine();
        
        //Converso la fecha
        java.util.Date ahora = new java.util.Date();
        java.sql.Date sqlFecha = new java.sql.Date(ahora.getTime());
        
        Integer productoId = 0;
        System.out.println("Escriba el id del producto de la carta que desea: ");
        productoId = sc.nextInt();
        
       p.setId(0L);
       p.setCliente(cliente);
       p.setFecha(sqlFecha);
       p.setEstado("Pendiente");
       p.setProductoId(productoId);
       
       Transaction tr = s.beginTransaction();
       s.save(p);
       tr.commit();
       
       System.out.println("Su pedido ha sido creado correctamente.");
    }
    
    public static void eliminarPedido() {
        
        //Escaner para preguntar el pedido a eliminar
        Long id;
        System.out.println("Inserte el id del pedido que desee eliminar: ");
        Scanner sc = new Scanner(System.in);
        id = sc.nextLong();
        
        Transaction tr = s.beginTransaction();
        Pedidos p = s.load(Pedidos.class, id);
        s.remove(p);
        tr.commit();
        
        System.out.println("Su pedido ha sido eliminado correctamente.");
    }
    
    public static void marcarRecogido() {
        
        //Escaner para el pedido a marcar como recogido
        Long id;
        Scanner sc = new Scanner(System.in);
        System.out.println("Inserte el id del pedido que desee marcar como recogido: ");
        id = sc.nextLong();
        
        Transaction tr = s.beginTransaction();
        Pedidos p = s.load(Pedidos.class, id);
        p.setEstado("Recogido");
        s.update(p);
        tr.commit();
        
        System.out.println("Su pedido ha sido marcado como recogido.");
    }
    
    public static void listarPendientes() {
        
        //Converso la fecha
        java.util.Date ahora = new java.util.Date();
        java.sql.Date sqlFecha = new java.sql.Date(ahora.getTime());
    
        //Realizo la query para seleccionar los pedidos pendientes y de la fecha de hoy
        Query q = s.createQuery("FROM Pedidos p WHERE p.estado='Pendiente' and p.fecha=:fecha");
        q.setParameter("fecha", sqlFecha);
        ArrayList<Pedidos> pedidos = (ArrayList<Pedidos>) q.list();
        pedidos.forEach((tar) -> System.out.println(tar));
    }
    
    public static void listarCarta() {
        
        //Query para listar la carta
        Query q = s.createQuery("FROM Carta");
        ArrayList<Carta> carta = (ArrayList<Carta>) q.list();
        
        carta.forEach((tar) -> System.out.println(tar));
    }
    
    public static void listarPedidos() {
        
        //Query para listar los pedidos
        Query q = s.createQuery("FROM Pedidos");
        ArrayList<Pedidos> pedidos = (ArrayList<Pedidos>) q.list();
        pedidos.forEach((tar) -> System.out.println(tar));
    }
}