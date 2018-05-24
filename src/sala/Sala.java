package sala;

import listaDesordenada.*;
import listaOrdenada.*;
import usuario.*;

public class Sala
{
    protected String                    nome;
    protected ListaDesordenada<Usuario> usuarios;

    public Sala(String n) throws Exception
    {
    	if(n == null)
    		throw new Exception("Nome da sala nulo!");
    	this.nome = n;
    	this.usuarios = new ListaDesordenada<Usuario>();
    }

    public void usuarioEntrar (Usuario usr) throws Exception
    {
    	if(usr==null)
    		throw new Exception("Usuario nulo!");

    	this.usuarios.inserirNoInicio(usr);
    }

    public void usuarioSair(String usr) throws Exception
    {
    	for(int i=0; i<this.usuarios.getQtd(); i++)
    	{
    		if(i==0)
    		{
    			Usuario h = this.usuarios.getInicio();
    			if(usr.equals(h.getNick()))
        		{
        			this.usuarios.jogarForaPrimeiro();
        			return;
        		}
    			this.usuarios.mudarAtualLista(0);
    		}else
    		{
    			if(this.usuarios.getProxAtualLista().getNick().equals(usr))
        		{
        			this.usuarios.jogarForaProxAtualLista();
        			break;
        		}
        		this.usuarios.andarAtualLista();
    		}
    	}
    }

    public ListaOrdenada<String> getNicks () throws Exception
    {
    	ListaOrdenada<String> ret = new ListaOrdenada<String>();
    	
    	this.usuarios.mudarAtualLista(0);
    	for(int i=0; i<this.usuarios.getQtd(); i++)
    	{
    		ret.inserir(this.usuarios.getAtualLista().getNick());
    		this.usuarios.andarAtualLista();
    	}

    	return ret;
    }

    public Usuario[] getUsuarios () throws Exception
    {
    	Usuario[] ret = new Usuario[this.usuarios.getQtd()];
    	
    	this.usuarios.mudarAtualLista(0);
    	for(int i=0; i<this.usuarios.getQtd(); i++)
    	{
    		ret[i] = this.usuarios.getAtualLista();
    		this.usuarios.andarAtualLista();
    	}

    	return ret;
    }

    public Usuario getUsuario (String nick) throws Exception
    {
    	this.usuarios.mudarAtualLista(0);
    	for(int i=0; i<this.usuarios.getQtd(); i++)
    	{
    		if(this.usuarios.getAtualLista().getNick().equals(nick))
    			return this.usuarios.getAtualLista();
    		this.usuarios.andarAtualLista();
    	}

    	throw new Exception("Esse nick nao esta na sala!");
    }
    
    public boolean temUsuario(String nick)
	{
    	try
    	{
    		this.usuarios.mudarAtualLista(0);
        	for(int i=0; i<this.usuarios.getQtd(); i++)
        	{
        		if(nick.equals(this.usuarios.getAtualLista().getNick()))
        			return true;
        		this.usuarios.andarAtualLista();
        	}

        	return false;
    	}catch(Exception e)
    	{
    		return false;
    	}
	}

    public String getNome()
    {
    	return this.nome;
    }

    // metodos para enviar mensagem para um usuario especifico
    public void enviarMsg(String us, String msg) throws Exception
    {
        if(us == null || msg == null)
            throw new Exception("Valor nulo!");

        Usuario usr = this.getUsuario(us);
        usr.envia(msg);
    }

    //mensagem para todos da sala (msg aberta)
    public void enviarMsg(String msg) throws Exception
    {
    	try {
	        this.usuarios.mudarAtualLista(0);
	        for(int i=0; i<this.usuarios.getQtd(); i++)
	        {
	            Usuario usr = this.usuarios.getAtualLista();
	            usr.envia(msg);
	            
	            this.usuarios.andarAtualLista();
	        }
    	}catch(Exception e)
    	{throw new Exception("Erro ao enviar mensagem");}
    }


    //metodos obrigatorios
    public String toString()
    {
    	String ret = "{Nome sala: " + this.nome + ", usuarios: " + this.usuarios + "}";
    	return ret;
    }

    public int hashCode()
    {
    	int ret = 7;
    	ret = ret*7 + this.nome.hashCode();
    	ret = ret*7 + this.usuarios.hashCode();
    	return ret;
    }

    public boolean equals(Object obj)
    {
    	if(obj==null)
    		return false;
    	if(this==obj)
    		return true;

    	if(obj.getClass() != this.getClass())
    		return false;

    	Sala sl = (Sala)obj;

    	if(this.nome.equals(sl.nome) && this.usuarios.equals(sl.usuarios))
    		return true;

    	return false;
    }
}
