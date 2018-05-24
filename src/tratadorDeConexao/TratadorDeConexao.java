package tratadorDeConexao;

import usuario.*;
import sala.*;
import salas.Salas;
import listaOrdenada.*;

import java.io.IOException;
import java.net.*;

public class TratadorDeConexao extends Thread
{
    protected Sala                  sala;
    protected Salas                 salas;
    protected Usuario               usuario;
    protected boolean               fim=false;
    protected boolean               cadastrado=false;
    protected boolean               salaPronta=false;
    protected Socket				con;
    
    public TratadorDeConexao (Salas salas, Socket conexao) throws Exception
    {
    	try
        {   
		        this.usuario = new Usuario (salas.getNomesSalas(), conexao);
		        // construtor de usuario instancia BufferedReader e PrintWriter
		        // e interage com o usuario atraves deles para obter o nick
		        // e a sala (mande as salas para ele escolher)
		
		        this.con = conexao;
		        
		        this.salas = salas;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	throw new Exception("erro ao tratar conexao");
        }
    }

    public void pare ()
    {
        this.fim=true;
        
        try
        {
        	this.sala.enviarMsg("-"+usuario.getNick());
        	this.sala.usuarioSair(this.usuario.getNick());
        	this.con.close();
		} catch (Exception e) {}
    }

    public void run ()
    {
        while (!fim)
        {
try {
			if(!salaPronta)
			{
				String[] auxSalas = salas.getNomesSalas();
			    String nomesSalas = salas.qtd() + "";
			    
			    for(int i = 0; i<auxSalas.length; i++)
			    	nomesSalas += "/&*_/" + auxSalas[i];
			    
			    this.usuario.envia(nomesSalas);

			    salaPronta = true;
			}

          String texto = this.usuario.recebe();
          //faz o usuario sair da sala
          if(texto.charAt(0) == 'z')
            this.pare();
          else
          //faz usuario entrar numa sala
          if(texto.charAt(0) == 's')
          {
        	this.usuario.setSala(texto.substring(1));
        	this.sala = salas.getSala(usuario.getSala());
          }else
          //verifica nick
    	  if(texto.charAt(0) == 'n')
          {
        	  if(this.sala.temUsuario(texto.substring(1))) 
        		  this.usuario.envia("nick nOK");
        	  else
        	  {
        		  this.usuario.envia("nick OK");
        	  
        		  this.usuario.setNick(texto.substring(1));
        		  this.sala.usuarioEntrar(this.usuario);
        		  
        		  this.cadastrado = true;
        		  
        		  ListaOrdenada<String> us = sala.getNicks();
        		  us.mudarAtualLista(0);
        		  for(int i=0; i<us.getQtd(); i++)
        		  {
        			  if(!us.getAtualLista().equals(this.usuario.getNick()))
        				  this.usuario.envia("+"+us.getAtualLista());
        			  us.andarAtualLista();
        		  }
        			  
        		  sala.enviarMsg("+"+usuario.getNick());
        	  }
          }else
    	  //manda a mensagem do usuario para todos os remetentes
          if(texto.substring(0, 3).equals("env"))
          {
        	  String nickDest = texto.substring(3, texto.indexOf("/&*_/"));
        	  String msg = texto.substring(texto.indexOf("/&*_/") + 5);
        	  
        	  if(nickDest.equals("TODOS"))
        		  this.sala.enviarMsg("msg"+this.usuario.getNick() + ": " + msg);
			  else 
			  {
				  this.sala.enviarMsg(nickDest, "msg"+this.usuario.getNick() + ": " + msg);		
				  this.usuario.envia("msg"+this.usuario.getNick() + ": " + msg);
			  }
	     }
          
} catch (Exception e)
{
	e.printStackTrace();
}
        }
    }
}



//USUAIO.ENVIAR(MSG) VAI ENVIAR ESSA MENSAGEM PARA ESSE USUARIO
//USUARIO.RECEBE() VAI RECEBER A MSG Q ESSE USUARIO MANDOU