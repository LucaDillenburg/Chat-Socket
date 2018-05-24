package servidor;

import salas.*;

import java.io.IOException;
import java.net.*;
import tratadorDeConexao.*;

public class Servidor {

	public static void main (String[] args)
    {
        Salas        salas  = new Salas ();
        ServerSocket pedido;
		try 
		{
			pedido = new ServerSocket (12345);
	
	        System.out.println ("Servidor operante...");
	        System.out.println ("Porta:12345");
	        
	        try
	        {
		        salas.novaSala("geral");
		        salas.novaSala("pd17");
		        salas.novaSala("Malignoos");
	        }catch (Exception e) {}
	
	        for(;;)
	        {
	        	Socket conexao;
	            try {
	            	conexao = pedido.accept();
					new TratadorDeConexao (salas,conexao).start ();
				} catch (Exception e)
	            {
					e.printStackTrace();
				}
	        }
		} catch (IOException e)
		{
			e.printStackTrace();
		}

    }
}
