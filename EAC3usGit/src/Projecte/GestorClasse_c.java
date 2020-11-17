/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gestors;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import model.Classe;

/**
 * Classe que gestiona la persistencia dels objectes de la classe model.Classe
 * @author professor
 */
public class GestorClasse {

    protected static final String CLAU_DUPLICADA = "CLAU DUPLICADA";
    protected static final String CLAU_INEXISTENT = "CLAU INEXISTENT";
    protected static final String ARREL = "doc(\"estudis/estudis.xml\")/collection(\"estudis\")//"; //arrel del document
    
    private XQConnection con=null;

    /**
     * Crea un gestor de Classe que treballara amb la connexio conn
     * @param con connexio a traves de la qual es fan persistents de les classes
     */
    public GestorClasse(XQConnection con) {
       this.con = con;
    }
    
    /**
     * Dona d'alta una Classe en la base de dades. Si ja n'hi ha alguna amb el seu mateix codi, llenca una excepcio.
     * @param classe curs tipus classe a donar d'alta
     * @throws gestors.GestorException en cas d'error a la base de dades que pot ser, entre altres, clau duplicada.
     */
    public void inserir(Classe classe) throws GestorException  {
        try {
            if(obtenirClasse(classe.getCodi())==null){  // no hi es: correcte
                XQExpression expr=con.createExpression();
                expr.executeQuery("insert node "+Utilitats.formaClasseXML(classe)+" into "+ARREL+"estudis");
                expr.close();
            }
            else {
                throw new GestorException (CLAU_DUPLICADA);
            }
        } catch (XQException ex) {
                throw new GestorException (ex.getMessage());
        } 
    }

    
    /**
     * Esborra de la base de dades una classe amb un codi determinat
     * @param codiClasse codi de la classe a esborrar
     * @throws gestors.GestorException si el codi no correspon a cap classe de la base de dades
     * o hi ha un error en l'acces a la base de dades
     */
   
    public void eliminar(int codiClasse) throws GestorException {
       try {
            if(obtenirClasse(codiClasse)==null){
                throw new GestorException (CLAU_INEXISTENT);
            }
            
            // existeix: l'esborrem
            XQExpression expr=con.createExpression();            
            expr.executeQuery("delete node "+ARREL+"estudis/classe[@codi = \""+codiClasse+"\" ]");
            expr.close();
        } catch (XQException ex) {
            throw new GestorException (ex.getMessage());
        }
    }


    
    /**
     * Obte la classe de la base de dades amb un determinat codi.
     * @param codiClasse codi de la classe a obtenir
     * @return classe amb codiClasse o null si no hi ha cap classe a la base de dades
     * @throws gestors.GestorException en cas d'error a la base de dades
     */
   
    public Classe obtenirClasse(int codiClasse) throws GestorException  {
        try {
            XQExpression expr=con.createExpression();
            XQResultSequence query= expr.executeQuery("for $h in "+ARREL+"estudis/classe[@codi = \""+codiClasse+"\"] return $h");
            
            String primerText=null;
            while(query.next()){
                primerText=query.getItemAsString(null);
            }
            expr.close();
            if(primerText==null){
                return null;
            }else{
                return Utilitats.obteClasse(primerText);
            }} catch (XQException ex) {
            throw new GestorException (ex.getMessage());
        }
    }

    /**
     * Obté una llista amb les classes de la base de dades que tenen matriculat un alumne determinat
     * @param alumne alumne utilitzat per a obtenir la llista de classes
     * @return Llista amb les classes de la base de dades que contenen un alumne al seu array de llistes d'alumnes matriculats
     * @throws gestors.GestorException en cas d'error a la base de dades
     */
    
    public List<Classe> obtenirClassePerAlumne(String alumne) throws GestorException  {
        try {
            XQExpression expr=con.createExpression();
            XQResultSequence query=(XQResultSequence) expr.executeQuery("for $h in "+ARREL+"estudis/classe[alumnes/alumne = \""+alumne+"\"] return $h");

            List<Classe> llista = new ArrayList<>();
            
            while(query.next()){
                llista.add(Utilitats.obteClasse(query.getItemAsString(null)));
            }
            expr.close();
            return llista;
        } catch (XQException ex) {
            throw new GestorException (ex.getMessage());
        }
    }
}