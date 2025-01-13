import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.project.model.Elettore;

class ElettoreTest {

	private Elettore e;
	private String nome = "Luigi";
	private String cognome = "Verdi";
	private String cf = "cf";
	private String password = "password";
			
	//controlla la correttezza del primo costruttore
	@Test
	void testElettoreStringStringString() {
		e = new Elettore(nome, cognome, cf);
		
		assertEquals(nome, e.getName());
		assertEquals(cognome, e.getCognome());
		assertEquals(cf, e.getCf());
	}

	//controlla la correttezza del secondo costruttore
	@Test
	void testElettoreStringString() {
		e = new Elettore(cf, password);
		
		assertEquals(cf, e.getCf());
		assertEquals(password, e.getPassword());
	}
	
	/*
	 * controlla il getter e il setter dell'attributo nome
	 */
	@Test
	void testSetGetName() {
		e = new Elettore();
		e.setName(nome);
		
		assertEquals(nome, e.getName());
	}

	/*
	 * controlla il getter e il setter dell'attributo cf
	 */
	@Test
	void testSetGetCf() {
		e = new Elettore();
		e.setCf(cf);
		
		assertEquals(cf, e.getCf());
	}

	/*
	 * controlla il getter e il setter dell'attributo cognome
	 */
	@Test
	void testSetGetCognome() {
		e = new Elettore();
		e.setCognome(cognome);
		
		assertEquals(cognome, e.getCognome());
	}

	/*
	 * controlla il getter e il setter dell'attributo password
	 */
	@Test
	void testSetGetPassword() {
		e = new Elettore();
		e.setPassword(password);
		
		assertEquals(password, e.getPassword());
	}

}
