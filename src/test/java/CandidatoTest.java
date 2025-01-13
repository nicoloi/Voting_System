
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.project.model.Candidato;
import com.project.model.Partito;

class CandidatoTest {
	
	private Candidato c;
	private String nome = "Mario";
	private String cognome = "Rossi";
	private Partito p = new Partito("partito");

	/*
	 * controlla che il metodo restituisca il cognome del candidato
	 */
	@Test
	void testGetCognome() {
		c = new Candidato(nome, cognome, p);
		
		System.out.println("\nRisultato atteso -> " + cognome);
		System.out.println("Risultato ottenuto -> " + c.getCognome());

		assertEquals(cognome, c.getCognome());
	}

	/*
	 * controlla che il metodo restituisca il partito in cui il candidato fa parte
	 */
	@Test
	void testGetPartito() {
		c = new Candidato(nome, cognome, p);
		
		System.out.println("\nRisultato atteso -> " + p);
		System.out.println("Risultato ottenuto -> " + c.getPartito());
		
		assertEquals(p, c.getPartito());
	}

	@Test
	void testToString() {
		c = new Candidato(nome, cognome, p);
		String expected = nome + " " + cognome;
		
		System.out.println("\nRisultato atteso -> " + expected);
		System.out.println("Risultato ottenuto -> " + c.toString());
		
		assertEquals(expected,  c.toString());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsObject() {
		c = new Candidato(nome, cognome, p);
		
		//controllo equals con un'istanza di una classe che non � Candidato, ad esempio una stringa.
		String s = "stringa";
		
		System.out.println("\nRisultato atteso -> false");
		System.out.println("Risultato ottenuto -> " + c.equals(s));
		
		assertFalse(c.equals(s));
		
		//controllo equals con un candidato diverso.
		Candidato other = new Candidato("Giovanni", "Bianchi", null);
		
		System.out.println("\nRisultato atteso -> false");
		System.out.println("Risultato ottenuto -> " + c.equals(other));
		
		assertFalse(c.equals(other));
		
		//controllo equals con un candidato avente stesso nome e cognome.
		other = new Candidato(nome, cognome, p);
		
		System.out.println("\nRisultato atteso -> true");
		System.out.println("Risultato ottenuto -> " + c.equals(other));
		
		assertTrue(c.equals(other));
	}

	/*
	 * controlla che il metodo della superclasse ElementoDaVotare restituisca il nome del candidato
	 */
	@Test
	void testGetNome() {
		c = new Candidato(nome, cognome, p);
		
		System.out.println("\nRisultato atteso -> " + nome);
		System.out.println("Risultato ottenuto -> " + c.getNome());
		
		assertEquals(nome, c.getNome());
	}

}
