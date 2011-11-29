import java.util.Date;
public class EsameConVoto extends Esame{
	protected int voto;
	protected boolean lode;
	
	public EsameConVoto(String nome, int v, int c, boolean lode, Date data) throws NumberFormatException, IllegalArgumentException{
			super(nome, c, data);
			if(v > 17 && v < 31) this.voto = v; 
			else throw new NumberFormatException("voto inserito errato");
			if(lode && v != 30) throw new IllegalArgumentException("un voto diverso da 30 non può avere lode");
			else this.lode = lode;
		}
		
	public String toString(){
			String z = "                              ";
			if (nome.length() < 30){
				int size = 30 - nome.length();
				z = nome + z.substring(0, size)+ " " + voto;
			}
			else z = nome.substring(0, 29) + " " + voto;
			if(lode) z = z + "L";
			else z = z + " ";
			if (cfu < 10) z = z + " CUF: " + cfu + "  "+data;
			else z = z + " CUF: " + cfu + " "+data;
			return z;
		}
	
	public boolean faMedia(){return true;}
	
	public int getVoto(){return voto;}
	
	public boolean getLode(){return lode;}

} 
