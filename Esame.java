import java.io.*;
import java.util.Date;
public class Esame implements Serializable, Comparable{
		
		 protected int cfu;
		 protected String nome;
		 protected Date data;
		
		public Esame(String nome, int cfu, Date data) throws NumberFormatException, IllegalArgumentException{
			if(cfu>0)this.cfu = cfu;
			else throw new NumberFormatException("i cfu devono essere maggiori di 0");
			if(!nome.equals("")) this.nome = nome;
			else throw new IllegalArgumentException("il nome esame deve avere almeno un carattere");
			this.data = data;
		}
		
		public int compareTo(Object o){
			Esame e = (Esame) o;
			return (this.data).compareTo(e.data);
		}
		
		public String toString(){
			String z = "                              ";
			if (nome.length() < 30){
				int size = 30 - nome.length();
				z = nome + z.substring(0, size) + " SUP";
			}
			else z = nome.substring(0, 29) + " SUP";
			if (cfu < 10) z = z + " CUF: " + cfu + "  "+data;
			else z = z + " CUF: " + cfu + " "+data;
			return z;
		}
		
		public boolean faMedia(){return false;}
		
		public int getCFU(){return cfu;}
		
		public String getNome(){return nome;}
		
		public Date getData(){return data;}
		
	}
