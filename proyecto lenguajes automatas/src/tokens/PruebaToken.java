package tokens;
import java.util.HashMap;
import java.util.Vector;

import proyecto1.PilaE;

import java.io.*;
public class PruebaToken {
	public static String pReservada[]={"int","boolean","if","false","true","private","public","class"};
	public static String nOperador[][]={{"<=","OPERADOR DE MENOR IGUAL"},{">=","OPERADOR DE MAYOR IGUAL"},{"!=","OPERADOR DE DIFERENTE"},{"==","OPERADOR DE COMPARACION"},{"=","OPERADOR DE ASIGNACION"},{"+","OPERADOR DE SUMA"},{"-","OPERADOR DE RESTA"},{"*","OPERADOR DE MULTIPLICACION"},{"/","OPERADOR DE DIVISION"},{"<","OPERADOR DE MENOR QUE"},{">","OPERADOR DE MAYOR QUE"},{";","OPERADOR FIN DE INSTRUCCION"}};
	public static String nAgrupa[][]={{"(","ABRE PARENTESIS"},{")","CIERRA PARENTESIS"},{"{","ABRE LLAVES"},{"}","CIERRA LLAVES"}};
	public static String alfabetoNumeros="0123456789";
	public static String alfabetoLetras="abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ_";
	public static String operadoresaritmeticos[]={"+","-","*","/"};
	public static String operadoreslogicos[]={"<",">",">=","<=","==","!="};
	public static int error=0;
	public static int nToken=0;
	public static Vector<String>token=new Vector<String>();
	public static Vector<String>nomToken=new Vector<String>();
	public static Vector<String>tokenRenglon=new Vector<String>();
	public static Vector<String>nomTokenRenglon=new Vector<String>();
	public static Vector<String>listaErroresSemanticos=new Vector<String>();
	public static Vector<String>expresionesAritemeticas=new Vector<String>();
    private static HashMap<String,TablaSimbolo> tablaSimbolos = new HashMap<String,TablaSimbolo>();
    
    public static int identificador(String cadena,int pos){
		int nPos=0;
		boolean d=letra(cadena.substring(pos, pos+1));
		if(d)
			nPos=1;
		for(int i=pos+nPos;i<cadena.length()&&d;i++){
			d=letra(cadena.substring(i, i+1));
			if(d){
				nPos++;
			}
		}
		return nPos;
	}
	public static boolean palReservada(String pal){
		boolean r=false;
		for(String p:pReservada){
			if(p.equals(pal)){
				r=true;
			}
		}
		return r;
	}
	public static String nomAgrupa(String cadena){
		String n="";
		for(String op[]:nAgrupa){
			if(op[0].equals(cadena))
				n=op[1];
		}
		return n;
	}
	public static int agrupa(String cadena,int pos){
		int nPos=0;
		boolean d=agrupadores(cadena.substring(pos, pos+1));
		if(d)
			nPos=1;
		return nPos;
	}
	public static boolean agrupadores(String cad){
		boolean existe=false;
		for(String c[]:nAgrupa){
			if(cad.equals(c[0])){
				existe=true;
			}
		}
		return existe;
	}
	public static int signoPuntuacion(String cadena,int pos){
		int nPos=0;
		String alfabeto=";";
		if(alfabeto.contains(cadena.substring(pos, pos+1))){
			nPos++;
		}		
		return nPos;
	}
	public static int numero(String cadena,int pos){
		int nPos=0;
		boolean d=digito(cadena.substring(pos, pos+1));
		if(d)
			nPos=1;
		for(int i=pos+nPos;i<cadena.length()&&d;i++){
			d=digito(cadena.substring(i, i+1));
			if(d){
				nPos++;
			}
		}
		return nPos;
	}
	public static String nomOperador(String cadena){
		String n="";
		for(String op[]:nOperador){
			if(op[0].equals(cadena))
				n=op[1];
		}
		return n;
	}
	public static int operadores(String cadena,int pos){
		int nPos=0;
		boolean d=operador(cadena.substring(pos, pos+1),false);
		for(int i=pos+nPos;i<cadena.length()&&d;i++){
			d=operador(cadena.substring(pos, i+1),false);
			if(d){
				nPos++;
			}
		}
		d=operador(cadena.substring(pos, pos+nPos),true);
		if(!d){
			nPos=0;
		}
		return nPos;
	}
	public static boolean operador(String cadena, boolean esta){
		boolean existe=false;
		for(String c[]:nOperador){
			if((!esta&&c[0].contains(cadena))||(esta&&c[0].equals(cadena))){
				existe=true;
			}
		}
		return existe;
	}
	public static boolean letra(String cadena){
		boolean si=false;
		if(alfabetoLetras.contains(cadena.substring(0, 1))){
			si=true;
		}
		return si;
	}
	
	
	public static boolean digito(String cadena){
		boolean si=false;
		if(alfabetoNumeros.contains(cadena.substring(0, 1))){
			si=true;
		}
		return si;
	}
	
	
	public static void lXl(String palabras){
		for(int i=0;i<palabras.length();i++){
			int n=signoPuntuacion(palabras,i);
			if(identificador(palabras,i)>0){
				int fin=identificador(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				i=fin-1;
				if(palReservada(pal)){
					token.addElement(pal);
					nomToken.addElement("PALABRA RESERVADA");
				}else{
					token.addElement(pal);
					nomToken.addElement("ID");
				}
			}else if(numero(palabras,i)>0){
				int fin=numero(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				token.addElement(pal);
				nomToken.addElement("ENTERO");
				i=fin-1;
			}else if(operadores(palabras,i)>0){
				int fin=operadores(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				String nOp=nomOperador(palabras.substring(i, fin));
				token.addElement(pal);
				nomToken.addElement(nOp);
				i+=operadores(palabras,i)-1;
			}else if(agrupa(palabras,i)>0){
				int fin=agrupa(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				String nAg=nomAgrupa(palabras.substring(i, fin));
				token.addElement(pal);
				nomToken.addElement(nAg);
				i+=agrupa(palabras,i)-1;
			}else if(palabras.charAt(i)!=' '&&palabras.charAt(i)!='\n'&&palabras.charAt(i)!='\t'){
				String car=palabras.substring(i, i+1);
				token.addElement(car);
				nomToken.addElement("ERROR");
				
			}
		}
	}
	
	
	public static boolean classDeclaracion(){
		boolean correcto=true;
		String palabra="";
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(modificador(palabra)){
			//correcto=true;
			nToken++;
		}
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("class")){
			System.out.println("Error token #"+nToken+": se esperaba la palabra reservada class");
			//correcto=true;
		}
		nToken++;
		String tipoToken="";
		if(nToken<=nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(tipoToken.equals("ID")){
			//correcto=true;
		}else{
			System.out.println("Error token #"+nToken+": se esperaba el nombre de la clase");
			correcto=false;
		}
		nToken++;
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(palabra.equals("{")){
			//correcto=true;
		}else{
			System.out.println("Error token #"+nToken+": se esperaba apertura de llaves");
			correcto=false;
		}
		nToken++;
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("}")&&(type(palabra)||modificador(palabra))&&field_declaration()){
			//correcto=true;
			//nToken++;
		}
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("}")){
			statement();
				//nToken++;
		}
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(palabra.equals("}")){
			//correcto=true;
		}else{
			System.out.println("Error token #"+nToken+": se esperaba cerrar llaves");
			correcto=false;
		}
		nToken++;
		if(nToken<=token.size()){
			System.out.println("Error token #"+nToken+": tokens fuera de la clase");
			correcto=false;
		}
		if(correcto){
			System.out.println("Los toquen estan de acuerdo a la gramatica.");
		}
		return correcto;
	}
	
	
	public static boolean field_declaration(){
		boolean correcto=true;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(modificador(palabra)||type(palabra)){
			if(!variable_declaration()){
				correcto=false;
			}
		}
		return correcto;
	}
	public static boolean variable_declaration(){
		boolean correcto=true;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(modificador(palabra)){
			//correcto=true;
			nToken++;
		}
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!type(palabra)){
			System.out.println("Error token #"+nToken+": se esperaba tipo de dato");
			return false;
		}
		nToken++;
		String tipoToken="";
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(tipoToken.equals("ID")){
			if(!variable_declarator()){
				System.out.println("Error token #"+nToken+": se esperaba declaracion de variable");
				return false;
			}
		}
		nToken++;
		if(nToken<=token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals(";")){
			System.out.println("Error token #"+nToken+": se esperaba punto y coma");
			correcto=false;
		}
		nToken++;
		field_declaration();
		return correcto;
	}
	public static boolean variable_declarator(){
		boolean correcto=true;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		String tipoToken="";
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(!tipoToken.equals("ID")){
			System.out.println("Error token #"+nToken+": se esperaba un identificador");
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("=")){
			System.out.println("Error token #"+nToken+": se esperaba un operador de asignacion");
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(!(tipoToken.equals("ENTERO")||palabra.equals("true")||palabra.equals("false"))){
			System.out.println("Error token #"+nToken+": se esperaba un tipo entero o booleano");
			correcto=false;
		}
		return correcto;
	}
	public static boolean statement(){
		boolean correcto=false;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(modificador(palabra)||type(palabra)){
			if(variable_declaration()){
				correcto=true;
				//nToken++;
			}else{
				System.out.println("Error: Se esperaba una declaracion de variable");				
			}
		}
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if((palabra.equals("if"))){
			if(if_statement()){
				correcto=true;
			}else{
				System.out.println("Error: Se esperaba una declaracion if");	
			}
		}
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if((palabra.equals("while"))){
			if(while_statement()){
				correcto=true;
			}else{
				System.out.println("Error: Se esperaba una declaracion de while");	
			}
		}
		String tipoToken="";
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(tipoToken.equals("ID")){
			if(aritmetica_expression()){
				correcto=true;
			}else{
				System.out.println("Error: Se esperaba una declaracion aritmetica logica");	
			}
		}
		return correcto;
	}
	public static boolean aritmetica_expression(){
		boolean correcto=true;
		String expresion="";
		String palabra="";
		String tipoToken="";
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(!tipoToken.equals("ID")){
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("=")){
			System.out.println("Error token #"+nToken+": se esperaba un signo igual");
			return false;
		}
		nToken++;
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(!(tipoToken.equals("ENTERO")||tipoToken.equals("ID"))){
			System.out.println("Error token #"+nToken+": se esperaba un identificador o un entero");
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		while(esOperadorLogico(palabra)||esOperadorAritmetico(palabra)){
			nToken++;
			if(nToken<nomToken.size())
				tipoToken=nomToken.elementAt(nToken);
			if(!(tipoToken.equals("ENTERO")||!tipoToken.equals("ID")||!palabra.equals("true")||!palabra.equals("false"))){
				System.out.println("Error token #"+nToken+": se esperaba un identificador o un valor");
				return false;
			}
			nToken++;
			if(nToken<token.size()) {
				palabra=token.elementAt(nToken);
			}else {
				palabra="";
			}
		}

		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals(";")){
			System.out.println("Error token #"+nToken+": se esperaba punto y coma.");
			return false;
		}
		nToken++;
		statement();
		return correcto;
		
		
		
	}
	public static boolean while_statement(){
		boolean correcto=true;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("while")){
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("(")){
			System.out.println("Error token #"+nToken+": se esperaba un parentesis abierto");
			return false;
		}
		nToken++;
		if(!expression()){
			System.out.println("Error token #"+nToken+": se esperaba una expresion");
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals(")")){
			System.out.println("Error token #"+nToken+": se esperaba un parentesis cerrado");
			return false;
		}
		nToken++;
		statement();
		return correcto;
	}
	public static boolean if_statement(){
		boolean correcto=true;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("if")){
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals("(")){
			System.out.println("Error token #"+nToken+": se esperaba un parentesis abierto");
			return false;
		}
		nToken++;
		if(!expression()){
			System.out.println("Error token #"+nToken+": se esperaba una expresion");
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!palabra.equals(")")){
			System.out.println("Error token #"+nToken+": se esperaba un parentesis cerrado");
			return false;
		}
		nToken++;
		statement();
		return correcto;
	}
	
	
	
	public static boolean expression(){
		boolean correcto=false;
		String tipoToken="";
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if((tipoToken.equals("ENTERO")||tipoToken.equals("ID"))){
			if(testing_expression()){
				correcto=true;				
			}else{
				System.out.println("Error: Se esperaba una expresion");
			}
		}
		return correcto;
	}
	
	
	public static boolean testing_expression(){
		boolean correcto=true;
		String palabra="";
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		String tipoToken="";
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(!(tipoToken.equals("ENTERO")||tipoToken.equals("ID"))){
			return false;
		}
		nToken++;
		if(nToken<token.size())
			palabra=token.elementAt(nToken);
		if(!(palabra.equals(">")||palabra.equals("<")||palabra.equals(">=")||palabra.equals("<=")||palabra.equals("==")||palabra.equals("!="))){
			System.out.println("Error token #"+nToken+": se esperaba un operador de comparacion");
			return false;
		}
		nToken++;
		if(nToken<nomToken.size())
			tipoToken=nomToken.elementAt(nToken);
		if(!(tipoToken.equals("ENTERO")||tipoToken.equals("ID"))){
			System.out.println("Error token #"+nToken+": se esperaba un entero o identificador");
			return false;
		}
		return correcto;
	}
	
	
	public static boolean type(String palabra){
		boolean correcto=true;
		if(!type_specifier(palabra)){
			correcto=false;
		}
		return correcto;
	}
	public static boolean type_specifier(String palabra){
		boolean correcto=false;
		if(palabra.equals("boolean")||palabra.equals("int")){
			correcto=true;
		}
		return correcto;
	}
	public static boolean modificador(String t){
		boolean correcto=false;
		if(t.equals("public")||t.equals("private")){
			correcto=true;
		}
		return correcto;
	}
	
	
	public static String dameTexto(){
		String linea="",texto="";
		try{
			FileReader archivo=new FileReader("src/tokens/codigo fuente.txt"); // lee el archivo
			BufferedReader leeLinea=new BufferedReader(archivo); // filtra los daros de FileReader
			linea=leeLinea.readLine(); // otorga la primer linea del archivo
			while(linea!=null){ // si en la variable linea se guardo datos(no esta vacia) hara el ciclo
				texto+=linea+"\n";
				linea=leeLinea.readLine(); // lee la siguiente linea
			}
		}catch(FileNotFoundException exception){ // cacha el error de archivo no encontrado
			System.out.println(exception);
		}catch(Exception exception){ // cacha los otros errores
			System.out.println(exception);
		}
		return texto;
	}
	public static void main(String[] args) {
		String palabras=dameTexto();
		System.out.println(palabras);
		lXl(palabras);
		for(int i=0;i<token.size();i++){
			System.out.println("Token #"+(i)+": "+token.elementAt(i)+"\t"+nomToken.elementAt(i));
		}
		//classDeclaracion();
		dameTextoRenglon();
		imprimeErroresSemanticos();
		dameTextoRenglon2();
		if (error==0)
		{
			System.out.println("\n---Expresiones encontradas en el codigo---");
			for (int i = 0; i < expresionesAritemeticas.size(); i++) {
				
				System.out.println(expresionesAritemeticas.elementAt(i));
			}
		}
		
		codigoIntermedio();
	}
	public static String dameTextoRenglon(){
		String linea="",texto="";
		int nRenglon=0;
		try{
			FileReader archivo=new FileReader("src/tokens/codigo fuente.txt"); // lee el archivo
			BufferedReader leeLinea=new BufferedReader(archivo); // filtra los daros de FileReader
			linea=leeLinea.readLine(); // otorga la primer linea del archivo
			while(linea!=null){ // si en la variable linea se guardo datos(no esta vacia) hara el ciclo
				nRenglon++;
				lXlRenglon(linea,nRenglon);
				linea=leeLinea.readLine(); // lee la siguiente linea
			}
			imprimeTablaSimbolos();
			
		}catch(FileNotFoundException exception){ // cacha el error de archivo no encontrado
			System.out.println(exception);
		}catch(Exception exception){ // cacha los otros errores
			System.out.println(exception);
		}
		return texto;
	}
	
	public static String dameTextoRenglon2(){
		String linea="",texto="";
		int nRenglon=0;
		try{
			FileReader archivo=new FileReader("src/tokens/codigo fuente.txt"); // lee el archivo
			BufferedReader leeLinea=new BufferedReader(archivo); // filtra los daros de FileReader
			linea=leeLinea.readLine(); // otorga la primer linea del archivo
			while(linea!=null){ // si en la variable linea se guardo datos(no esta vacia) hara el ciclo
				nRenglon++;
				lXlRenglon(linea,nRenglon);
				
				linea=leeLinea.readLine(); // lee la siguiente linea
				for (int i = 0; i < linea.length(); i++) 
				{
					if (linea.charAt(1)=='=') {
						expresionesAritemeticas.addElement(linea);
						break;
					}
				}
			}
		}catch(FileNotFoundException exception){ // cacha el error de archivo no encontrado
			System.out.println(exception);
		}catch(Exception exception){ // cacha los otros errores
			//System.out.println(exception);
		}
		return texto;
	}
	
	
	public static void lXlRenglon(String palabras, int nRenglon){
		tokenRenglon.clear();
		nomTokenRenglon.clear();
		for(int i=0;i<palabras.length();i++){
			int n=signoPuntuacion(palabras,i);
			if(identificador(palabras,i)>0){
				int fin=identificador(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				i=fin-1;
				if(palReservada(pal)){
					tokenRenglon.addElement(pal);
					nomTokenRenglon.addElement("PALABRA RESERVADA");
				}else{
					tokenRenglon.addElement(pal);
					nomTokenRenglon.addElement("ID");
				}
			}else if(numero(palabras,i)>0){
				int fin=numero(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				tokenRenglon.addElement(pal);
				nomTokenRenglon.addElement("ENTERO");
				i=fin-1;
			}else if(operadores(palabras,i)>0){
				int fin=operadores(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				String nOp=nomOperador(palabras.substring(i, fin));
				tokenRenglon.addElement(pal);
				nomTokenRenglon.addElement(nOp);
				i+=operadores(palabras,i)-1;
			}else if(agrupa(palabras,i)>0){
				int fin=agrupa(palabras,i)+i;
				String pal=palabras.substring(i, fin);
				String nAg=nomAgrupa(palabras.substring(i, fin));
				tokenRenglon.addElement(pal);
				nomTokenRenglon.addElement(nAg);
				i+=agrupa(palabras,i)-1;
			}else if(palabras.charAt(i)!=' '&&palabras.charAt(i)!='\n'&&palabras.charAt(i)!='\t'){
				String car=palabras.substring(i, i+1);
				tokenRenglon.addElement(car);
				nomTokenRenglon.addElement("ERROR");
				
			}
		}
		validaVariablesEstenDeclaradas(nRenglon);
		llenaTablaSimbolos(nRenglon);
		validarVariables(nRenglon);
		validarOperacion(nRenglon);
	}
	public static void validarVariables(int nRenglon){
		if(tokenRenglon.size()<4)
			return;
		String sTipo=tokenRenglon.elementAt(0);
		if(!type(sTipo)){
			return;
		}
		
		String palabra="",descripcion="";
		for(int i=3;i<nomTokenRenglon.size();i++){
			descripcion=nomTokenRenglon.elementAt(i);
			palabra=tokenRenglon.elementAt(i);
			if(descripcion.equals("ID")){
				if(encontrarSimbolo(palabra)){
					if(!regresaTipoDato(palabra).equals(sTipo)){
						String variable=tokenRenglon.elementAt(1);
						listaErroresSemanticos.add("Error: La variable "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
						error++;
					}
				}
			}else if(descripcion.equals("ENTERO")){
				if(!sTipo.equals("int")){
					String variable=tokenRenglon.elementAt(1);
					listaErroresSemanticos.add("Error: el valor "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
					error++;
				}
			}else if(palabra.equals("true")||palabra.equals("false")){
				if(!sTipo.equals("boolean")){
					String variable=tokenRenglon.elementAt(1);
					listaErroresSemanticos.add("Error: el valor "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
					error++;
				}
			}else if(!esOperadorAritmetico(palabra)&&!esOperadorLogico(palabra)&&!palabra.equals(";")){
				String variable=tokenRenglon.elementAt(1);
				listaErroresSemanticos.add("Error: el valor "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
				error++;
			}
		}
	}
	public static void validarOperacion(int nRenglon){
		if(tokenRenglon.size()<3)
			return;
		String sTipo="";
		String palabra=tokenRenglon.elementAt(0),descripcion=nomTokenRenglon.elementAt(0);
		if(!descripcion.equals("ID")){
			return;
		}
		sTipo=regresaTipoDato(palabra);
		for(int i=2;i<nomTokenRenglon.size();i++){
			descripcion=nomTokenRenglon.elementAt(i);
			palabra=tokenRenglon.elementAt(i);
			if(descripcion.equals("ID")){
				if(encontrarSimbolo(palabra)){
					if(!regresaTipoDato(palabra).equals(sTipo)){
						String variable=tokenRenglon.elementAt(0);
						listaErroresSemanticos.add("Error: La variable "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
						error++;
					}
				}
			}else if(descripcion.equals("ENTERO")){
				if(!sTipo.equals("int")){
					String variable=tokenRenglon.elementAt(0);
					listaErroresSemanticos.add("Error: el valor "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
					error++;
				}
			}else if(palabra.equals("true")||palabra.equals("false")){
				if(!sTipo.equals("boolean")){
					String variable=tokenRenglon.elementAt(0);
					listaErroresSemanticos.add("Error: el valor "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
					error++;
				}
			}else if(esOperadorAritmetico(palabra)){
				if(!sTipo.equals("int")){
					String variable=tokenRenglon.elementAt(0);
					listaErroresSemanticos.add("Error: el operador "+palabra+" no corresponde al tipo de "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
					error++;
				}
			}else if(esOperadorLogico(palabra)){
				if(!sTipo.equals("boolean")){
					String variable=tokenRenglon.elementAt(0);
					listaErroresSemanticos.add("Error: el operador "+palabra+" no corresponde al tipo de "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
					error++;
				}
			}else if(!palabra.equals(";")){
				String variable=tokenRenglon.elementAt(0);
				listaErroresSemanticos.add("Error: el valor "+palabra+" no es del mismo tipo que "+variable+" de tipo de dato "+sTipo+", en el renglon "+nRenglon);
				error++;
			}
		}
	}
	public static boolean esOperadorAritmetico(String op){
		boolean si=false;
		for(String p:operadoresaritmeticos){
			if(p.equals(op)){
				si=true;
			}
		}
		return si;
	}
	public static boolean esOperadorLogico(String op){
		boolean si=false;
		for(String p:operadoreslogicos){
			if(p.equals(op)){
				si=true;
			}
		}
		return si;
	}
	public static String regresaTipoDato(String palabra){
		String tipo="";
        TablaSimbolo simboloAtributos; 
        simboloAtributos =  tablaSimbolos.get(palabra);
		tipo=simboloAtributos.getTipo();
		return tipo;
	}
	public static void validaVariablesEstenDeclaradas(int nRenglon){
		if(tokenRenglon.size()==0)
			return;
		String token=tokenRenglon.elementAt(0);
		if(type(token)){
			return;
		}
		if(token.equals("class")){
			return;
		}
		String palabra="",descripcion="";
		for(int i=0;i<nomTokenRenglon.size();i++){
			descripcion=nomTokenRenglon.elementAt(i);
			if(descripcion.equals("ID")){
				palabra=tokenRenglon.elementAt(i);
				if(!encontrarSimbolo(palabra)){
					listaErroresSemanticos.add("Error: La variable "+palabra+" no se encuentra declarada, en el renglon "+nRenglon);
				}
			}
		}
	}
	public static void llenaTablaSimbolos(int renglon){
		TablaSimbolo simboloAtributos;
		if(tokenRenglon.size()==0)
			return;
		String token=tokenRenglon.elementAt(0);
		if(!type(token)){
			return;
		}
		String tipo="",simbolo="",valor="";
		if(tokenRenglon.size()>1)
			tipo=tokenRenglon.elementAt(0);
		if(tokenRenglon.size()>2)
			simbolo=tokenRenglon.elementAt(1);
		if(tokenRenglon.size()>4)
			valor=tokenRenglon.elementAt(3);
		if(!encontrarSimbolo(simbolo)){
			tablaSimbolos.put(simbolo, new TablaSimbolo(simbolo, "Operando", tipo, renglon,valor));
		}else{
			simboloAtributos =  tablaSimbolos.get(simbolo);
			listaErroresSemanticos.add("Error: La variable "+simbolo+" de tipo "+tipo+" en el renglon "+renglon+", ya se encuentra declarada en el renglon "+simboloAtributos.getPosicion());
		}
	}
	public static void imprimeTablaSimbolos(){
		System.out.println("\nTABLA DE SIMBOLOS");
		System.out.println("-------------------------------------------------------");
		System.out.println("SIMBOLO\tVALOR\tPOSICION\tROL\t\tTIPO");
		for(TablaSimbolo simboloAtributos:tablaSimbolos.values()){
			System.out.println(simboloAtributos.getSimbolo()+"\t"+simboloAtributos.getValor()+"\t"+simboloAtributos.getPosicion()+"\t\t"+simboloAtributos.getRol()+"\t"+simboloAtributos.getTipo());
		}
	}
	public static void imprimeErroresSemanticos(){
		if(listaErroresSemanticos.size()==0)
			return;
		System.out.println("\nERRORES SEMANTICOS");
		System.out.println("-------------------------------------------------------");
		for(String error:listaErroresSemanticos){
			System.out.println(error);
		}
	}
	public static boolean encontrarSimbolo(String simbolo){
        boolean bRegresa = false;
         if (tablaSimbolos.containsKey(simbolo))
        {
            bRegresa = true;
        }
        return bRegresa;
    }
    
    public static TablaSimbolo obtenerDatosSimbolo(String simbolo){
         TablaSimbolo atributosSimbolos = new TablaSimbolo();
         return atributosSimbolos;
    }

    public static void codigoIntermedio()
    {
    	PilaE pila= new PilaE(50);
    	String aux1="";
		String aux2="";
    	String exp="";
    	char letra=' ';
    	int operadores=0;
    	System.out.println("\n------Codigo intermedio------");
    	System.out.println();
    	for (int i = 0; i < expresionesAritemeticas.size(); i++)
    	{
    		exp=expresionesAritemeticas.elementAt(i);
    		System.out.println("Expresion: "+exp);
    		for (int j = 0; j < exp.length(); j++) 
    		{
				if (exp.charAt(j)=='+' ||exp.charAt(j)=='-'||exp.charAt(j)=='*'||exp.charAt(j)=='/') 
				{
					operadores++;
				}
			}
    		
    		if (operadores==0) 
    		{
    			System.out.println("T1= "+exp.substring(2, exp.length()-1));
    			System.out.println(exp.substring(0, 1)+"= T1");
    			System.out.println();
			}
    		
    		if (operadores!=0)
    		{
    		aux1=exp.substring(2, exp.length()-1);
    		for (int k=aux1.length()-1; k>=0; k--)
    		{
    		letra= aux1.charAt(k);
    			System.out.println("insertar "+letra);
    				if (pila.vacia())
    				{
    						pila.push(letra);
    				} else
    				{
    						pila.push(letra);
    				}
    		}
    		
    		for (int l = 0; l < operadores; l++) 
    		{
    			for (int j = 0; j < pila.getTope(); j++)
    			{
    				if (aux1.charAt(j)=='/' ||aux1.charAt(j)=='*') 
    				{
    					System.out.println("letra "+aux1.charAt(j));
    					break;
    				}
				}
				System.out.println("T"+(l+1)+" = ");
			}
    		
    			System.out.println(exp.charAt(0)+"= T"+operadores);
			}
    		letra=' ';
    		operadores=0;
        	exp="";
		}
    	
    }
    
}
