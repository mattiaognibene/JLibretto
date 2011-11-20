import java.util.Date;
public class Esame implements Comparable{
		
		 int voto;
		 int cfu;
		 String nome;
		 Date data;
		 boolean lode; 
		
		public Esame(String nome, int v, int c, boolean lode, Date data) throws NumberFormatException, IllegalArgumentException{
			if(v > 17 && v < 31) this.voto = v; 
			else throw new NumberFormatException("voto inserito errato");
			if(c>0) cfu = c;
			else throw new NumberFormatException("i cfu devono essere maggiori di 0");
			if(!nome.equals("")) this.nome = nome;
			else throw new IllegalArgumentException("il nome esame deve avere almeno un carattere");
			this.data = data;
			if(lode && v != 30) throw new IllegalArgumentException("un voto diverso da 30 non può avere lode");
			else this.lode = lode;
		}
		
		public Esame(String nome, int cfu, Date data) throws NumberFormatException, IllegalArgumentException{
			voto = 0;
			if(cfu>0)this.cfu = cfu;
			else throw new NumberFormatException("i cfu devono essere maggiori di 0");
			if(!nome.equals("")) this.nome = nome;
			else throw new IllegalArgumentException("il nome esame deve avere almeno un carattere");
			this.data = data;
			lode = false;
		}
		
		public int compareTo(Object o){
			Esame e = (Esame) o;
			return (this.data).compareTo(e.data);
		}
		
	}
